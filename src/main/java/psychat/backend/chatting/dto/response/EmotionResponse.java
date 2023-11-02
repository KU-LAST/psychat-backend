package psychat.backend.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmotionResponse {

    private String emotion;

    public static EmotionResponse of(String emotion) {
        return EmotionResponse.builder()
                .emotion(emotion)
                .build();
    }
}
