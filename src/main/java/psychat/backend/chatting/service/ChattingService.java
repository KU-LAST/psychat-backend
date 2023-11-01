package psychat.backend.chatting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import psychat.backend.chatting.SessionRepository;
import psychat.backend.chatting.domain.Session;
import psychat.backend.chatting.dto.request.ChatbotRequest;
import psychat.backend.chatting.dto.request.ChattingRequest;
import psychat.backend.chatting.dto.response.ChatbotResponse;
import psychat.backend.chatting.dto.response.ChattingResponse;
import psychat.backend.chatting.dto.response.SessionResponse;
import psychat.backend.global.exception.JsonConvertException;
import psychat.backend.global.exception.NotFoundException;
import psychat.backend.member.domain.Member;
import psychat.backend.member.service.MemberService;

@Service
@RequiredArgsConstructor
public class ChattingService {

    @Value("${CHATBOT_SERVER_URL}")
    private String chatBotUrl;

    private final MemberService memberService;
    private final SessionRepository sessionRepository;

    public SessionResponse start(String token) {
        Member findMember = memberService.findByToken(token);

        Session session = Session.init(findMember);
        Session savedSession = sessionRepository.save(session);

        return SessionResponse.of(savedSession.getId());
    }

    public ChattingResponse chat(String token, ChattingRequest request) {
        Session findSession = findBySessionId(request.getSessionId());

        ChatbotResponse chatbotResponse = send(request);

        return ChattingResponse.of(chatbotResponse);
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
