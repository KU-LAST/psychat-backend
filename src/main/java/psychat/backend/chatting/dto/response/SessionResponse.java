package psychat.backend.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SessionResponse {
    private Long sessionId;

    public static SessionResponse of(Long sessionId) {
        return SessionResponse.builder()
                .sessionId(sessionId)
                .build();
    }
}
