package psychat.backend.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psychat.backend.chatting.domain.Session;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreviousChatResponse {

    private Long sessionId;
    private String startDate;
    private String emotion;

    public static PreviousChatResponse of(Session session) {
        return PreviousChatResponse.builder()
                .sessionId(session.getId())
                .startDate(session.getStartTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .emotion(session.getEmotion() == null ? "분석 결과 없음" : session.getEmotion())
                .build();
    }
}
