package psychat.backend.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChattingResponse {

    private String responseContent;

    public static ChattingResponse of(ChatbotResponse response) {
        return ChattingResponse.builder()
                .responseContent(response.getResponseContent())
                .build();
    }
}
