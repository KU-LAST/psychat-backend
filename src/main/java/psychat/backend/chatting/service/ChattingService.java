package psychat.backend.chatting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import psychat.backend.chatting.domain.BotMessage;
import psychat.backend.chatting.domain.Session;
import psychat.backend.chatting.domain.UserMessage;
import psychat.backend.chatting.dto.request.ChatbotRequest;
import psychat.backend.chatting.dto.request.ChattingRequest;
import psychat.backend.chatting.dto.request.EndRequest;
import psychat.backend.chatting.dto.response.*;
import psychat.backend.chatting.repository.BotMessageRepository;
import psychat.backend.chatting.repository.SessionRepository;
import psychat.backend.chatting.repository.UserMessageRepository;
import psychat.backend.global.exception.JsonConvertException;
import psychat.backend.global.exception.NotFoundException;
import psychat.backend.member.domain.Member;
import psychat.backend.member.service.MemberService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingService {

    @Value("${CHATBOT_SERVER_URL}")
    private String chatBotUrl;

    //Utils
    private final ObjectMapper objectMapper;


    //Service
    private final MemberService memberService;
    private final EmotionService emotionService;

    //Repository
    private final SessionRepository sessionRepository;
    private final UserMessageRepository userMessageRepository;
    private final BotMessageRepository botMessageRepository;

    private static Map<Long, Map<Integer, Integer>> sessionIndexMap = new HashMap<>();

    public static Map<Long, Map<Integer, Integer>> getIndexMap() {
        return sessionIndexMap;
    }

    public SessionResponse start(String token) {
        Member findMember = memberService.findByToken(token);

        Session session = Session.init(findMember);
        Session savedSession = sessionRepository.save(session);

        Map<Integer, Integer> emotionAndFrequency = new HashMap<>();
        sessionIndexMap.put(savedSession.getId(), emotionAndFrequency);

        return SessionResponse.of(savedSession.getId());
    }

    @Transactional
    public ChattingResponse chat(String token, ChattingRequest request) {
        Session findSession = findBySessionId(request.getSessionId());
        ChatbotResponse chatbotResponse = send(request);

        UserMessage userMessage = UserMessage.of(findSession, request.getMessageContent());
        BotMessage botMessage = BotMessage.of(findSession, chatbotResponse.getResponseContent());

        UserMessage savedUM = userMessageRepository.save(userMessage);
        botMessageRepository.save(botMessage);

        emotionService.processEmotion(request, savedUM.getId());

        return ChattingResponse.of(chatbotResponse);
    }

    @Transactional
    public EmotionResponse end(String token, EndRequest request) {
        Session findSession = findBySessionId(request.getSessionId());

        int emotionResultIndex = getEmotionResult(findSession.getId());
        String emotion = emotionService.convert((long) emotionResultIndex);

        findSession.updateEmotion(emotion);
        sessionIndexMap.remove(findSession.getId());

        return EmotionResponse.of(emotion);
    }

    public ChattingListResponse chattingListBySessionId(Long sessionId) {
        Session findSession = findBySessionId(sessionId);
        List<UserMessage> userMessages = userMessageRepository.findAllBySession(findSession);
        List<BotMessage> botMessages = botMessageRepository.findAllBySession(findSession);

        List<MessageResponse> users = userMessages.stream()
                .map(MessageResponse::of)
                .collect(Collectors.toList());

        List<MessageResponse> bots = botMessages.stream()
                .map(MessageResponse::of)
                .collect(Collectors.toList());

        return ChattingListResponse.of(users, bots);
    }

    private int getEmotionResult(Long sessionId) {
        Map<Integer, Integer> emotionAndFrequency = sessionIndexMap.get(sessionId);

        Integer maxValue = -1;
        Integer maxKey = -1;

        for (Entry<Integer, Integer> entry : emotionAndFrequency.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();

            if(value > maxValue) {
                maxValue = value;
                maxKey = key;
            }
        }

        return maxKey;
    }

    private Session findBySessionId(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채팅입니다."));
    }

    private ChatbotResponse send(ChattingRequest request) {
        ChatbotRequest chatbotRequest = ChatbotRequest.of(request.getMessageContent());

        try {
            String jsonString = objectMapper.writeValueAsString(chatbotRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(chatBotUrl, HttpMethod.POST, requestEntity, String.class);

            return objectMapper.readValue(responseEntity.getBody(), ChatbotResponse.class);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException("JSON이 올바르지 않습니다.");
        }
    }

}
