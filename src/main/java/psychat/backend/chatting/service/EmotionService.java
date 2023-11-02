package psychat.backend.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import psychat.backend.chatting.domain.Emotion;
import psychat.backend.chatting.repository.EmotionRepository;
import psychat.backend.global.exception.NotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final ResourceLoader resourceLoader;

    private final EmotionRepository emotionRepository;

    public String convert(Long emotionIndex) {
        Emotion emotion = emotionRepository.findById(emotionIndex)
                .orElseThrow(() -> new NotFoundException("id와 맞는 감정이 없습니다."));

        return emotion.getType();
    }

    public void fill() {
        Resource resource = resourceLoader.getResource("classpath:wellness_dialog_category.txt");

        if (resource.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));

                String line;
                List<Emotion> emotionList = new LinkedList<>();

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("    ");
                    String emotionType = parts[0];
                    Long index = Long.parseLong(parts[1]);

                    Emotion emotion = Emotion.of(index, emotionType);
                    emotionList.add(emotion);
                }

                emotionRepository.saveAll(emotionList);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
