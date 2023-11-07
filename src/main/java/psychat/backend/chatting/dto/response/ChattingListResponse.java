package psychat.backend.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChattingListResponse {

    private List<MessageResponse> userMessages;
    private List<MessageResponse> botMessages;

    public static ChattingListResponse of(List<MessageResponse> userMessages, List<MessageResponse> botMessages) {
        return ChattingListResponse.builder()
                .userMessages(userMessages)
                .botMessages(botMessages)
                .build();
    }
}
