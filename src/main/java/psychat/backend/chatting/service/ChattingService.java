package psychat.backend.chatting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import psychat.backend.chatting.domain.BotMessage;
import psychat.backend.chatting.domain.Session;
import psychat.backend.chatting.domain.UserMessage;
import psychat.backend.chatting.dto.request.ChatbotRequest;
import psychat.backend.chatting.dto.request.ChattingRequest;
import psychat.backend.chatting.dto.request.EndRequest;
import psychat.backend.chatting.dto.response.ChatbotResponse;
import psychat.backend.chatting.dto.response.ChattingResponse;
import psychat.backend.chatting.dto.response.EmotionResponse;
import psychat.backend.chatting.dto.response.SessionResponse;
import psychat.backend.chatting.repository.BotMessageRepository;
import psychat.backend.chatting.repository.SessionRepository;
import psychat.backend.chatting.repository.UserMessageRepository;
import psychat.backend.global.exception.JsonConvertException;
import psychat.backend.global.exception.NotFoundException;
import psychat.backend.member.domain.Member;
import psychat.backend.member.service.MemberService;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Service
@RequiredArgsConstructor
public class ChattingService {

    @Value("${CHATBOT_SERVER_URL}")
    private String chatBotUrl;

    //Service
    private final MemberService memberService;
    private final EmotionService emotionService;

    //Repository
    private final SessionRepository sessionRepository;
    private final UserMessageRepository userMessageRepository;
    private final BotMessageRepository botMessageRepository;

    private Map<Long, Map<Integer, Integer>> sessionIndexMap = new HashMap<>();

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

        String emotionResult = emotionService.convert((long) chatbotResponse.getEmotion());

        UserMessage userMessage = UserMessage.of(findSession, request.getMessageContent(), emotionResult);
        BotMessage botMessage = BotMessage.of(findSession, chatbotResponse.getResponseContent());

        userMessageRepository.save(userMessage);
        botMessageRepository.save(botMessage);

        Map<Integer, Integer> emotionAndFrequency = sessionIndexMap.get(findSession.getId());
        int key = chatbotResponse.getEmotion();
        emotionAndFrequency.computeIfPresent(key, (k, v) -> v + 1);
        emotionAndFrequency.putIfAbsent(key, 1);

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

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(chatbotRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(chatBotUrl, HttpMethod.POST, requestEntity, String.class);

            return mapper.readValue(responseEntity.getBody(), ChatbotResponse.class);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException("JSON이 올바르지 않습니다.");
        }
    }

}
