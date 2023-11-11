package psychat.backend.contents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychat.backend.chatting.domain.Emotion;
import psychat.backend.chatting.service.EmotionService;
import psychat.backend.contents.domain.Content;
import psychat.backend.contents.dto.request.ContentsRequest;
import psychat.backend.contents.dto.request.FillContentRequest;
import psychat.backend.contents.dto.response.ContentsListResponse;
import psychat.backend.contents.dto.response.ContentsResponse;
import psychat.backend.contents.repository.ContentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final EmotionService emotionService;

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
}
