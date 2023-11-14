package psychat.backend.contents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import psychat.backend.chatting.domain.Emotion;
import psychat.backend.chatting.service.EmotionService;
import psychat.backend.contents.domain.Content;
import psychat.backend.contents.dto.request.ContentsRequest;
import psychat.backend.contents.dto.request.FillContentRequest;
import psychat.backend.contents.dto.response.ContentsListResponse;
import psychat.backend.contents.dto.response.ContentsResponse;
import psychat.backend.contents.repository.ContentRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final EmotionService emotionService;

    private final ResourceLoader resourceLoader;

    public ContentsListResponse recommended_contents(ContentsRequest request) {
        String emotionType = request.getEmotion();
        Emotion emotion = emotionService.getCategory(emotionType);

        List<ContentsResponse> contentsList = contentRepository.findAllByEmotionType(emotion.getCategory())
                .stream()
                .map(ContentsResponse::of)
                .collect(Collectors.toList());

        return ContentsListResponse.of(contentsList);
    }

    public void fill_content(FillContentRequest request) {
        Content content = Content.createContent(request);
        contentRepository.save(content);
    }

    public void fill_contents() {
        Resource resource = resourceLoader.getResource("classpath:content_list.txt");

        if (resource.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));

                String line;
                List<Content> contentList = new LinkedList<>();

                while ((line = reader.readLine()) != null) {

                    String[] parts = line.split("\t");
                    String emotionType = parts[0];
                    String videoUrl = parts[1];
                    String thumbnail = parts[2];
                    String uploadDate = parts[3];
                    String title = parts[4];

                    Content content = Content.of(emotionType, videoUrl, thumbnail, uploadDate, title);

                    contentList.add(content);
                }

                contentRepository.saveAll(contentList);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
