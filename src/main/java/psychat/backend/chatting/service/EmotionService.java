package psychat.backend.chatting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import psychat.backend.chatting.domain.Emotion;
import psychat.backend.chatting.domain.UserMessage;
import psychat.backend.chatting.dto.request.ChatbotRequest;
import psychat.backend.chatting.dto.request.ChattingRequest;
import psychat.backend.chatting.dto.response.EmotionJudgeResponse;
import psychat.backend.chatting.repository.EmotionRepository;
import psychat.backend.chatting.repository.UserMessageRepository;
import psychat.backend.global.exception.JsonConvertException;
import psychat.backend.global.exception.NotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmotionService {

    @Value("${EMOTION_JUDGE_URL}")
    private String emotionUrl;

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private final UserMessageRepository userMessageRepository;
    private final EmotionRepository emotionRepository;

    public String convert(Long emotionIndex) {
        Emotion emotion = emotionRepository.findById(emotionIndex)
                .orElseThrow(() -> new NotFoundException("id와 맞는 감정이 없습니다."));

        return emotion.getType();
    }

    @Async
    @Transactional
    public void processEmotion(ChattingRequest request, Long userMessageId) {
        ChatbotRequest emotionRequest = ChatbotRequest.of(request.getMessageContent());
        Map<Long, Map<Integer, Integer>> sessionIndexMap = ChattingService.getIndexMap();
        try {
            String jsonString = objectMapper.writeValueAsString(emotionRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(emotionUrl, HttpMethod.POST, requestEntity, String.class);

            EmotionJudgeResponse response = objectMapper.readValue(responseEntity.getBody(), EmotionJudgeResponse.class);

            String emotionResult = convert((long) response.getEmotion());
            log.info("emotion : {}", emotionResult);

            Map<Integer, Integer> emotionAndFrequency = sessionIndexMap.get(request.getSessionId());
            int key = response.getEmotion();
            emotionAndFrequency.computeIfPresent(key, (k, v) -> v + 1);
            emotionAndFrequency.putIfAbsent(key, 1);

            UserMessage findUserMessage = userMessageRepository.findById(userMessageId)
                    .orElseThrow(() -> new NotFoundException("유저 메시지가 존재하지 않습니다."));

            findUserMessage.update(emotionResult);

        } catch (JsonProcessingException e) {
            throw new JsonConvertException("JSON이 올바르지 않습니다.");
        }
    }
    public void fill() {
        Resource resource = resourceLoader.getResource("classpath:wellness_dialog_sentiment_index.txt");

        if (resource.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));

                String line;
                List<Emotion> emotionList = new LinkedList<>();

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("    ");
                    Long index = Long.parseLong(parts[0]);
                    String emotionType = parts[1];
                    String category = parts[2];

                    Emotion emotion = Emotion.createEmotion(index, emotionType, category);
                    emotionList.add(emotion);
                }

                emotionRepository.saveAll(emotionList);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Emotion getCategory(String type) {
        return emotionRepository.findByType(type)
                .orElseThrow(() -> new NotFoundException("맞는 감정이 없습니다."));
    }
}
