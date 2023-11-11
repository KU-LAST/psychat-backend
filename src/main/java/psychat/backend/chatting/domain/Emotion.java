package psychat.backend.chatting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Emotion {

    @Id
    private Long id;

    private String type;
    private String category;

    public static Emotion of(Long id, String type) {
        return Emotion.builder()
                .id(id)
                .type(type)
                .build();
    }

    public static Emotion createEmotion(Long id, String type, String category) {
        return Emotion.builder()
                .id(id)
                .type(type)
                .category(category)
                .build();
    }
}
