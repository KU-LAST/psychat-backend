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

    public static Emotion of(Long id, String type) {
        return Emotion.builder()
                .id(id)
                .type(type)
                .build();
    }
}
