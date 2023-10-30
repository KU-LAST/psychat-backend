package psychat.backend.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String token;

    public static LoginResponse of(String token) {
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
