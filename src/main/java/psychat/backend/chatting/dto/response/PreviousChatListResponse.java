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
public class PreviousChatListResponse {

    private List<PreviousChatResponse> previousChatList;

    public static PreviousChatListResponse of(List<PreviousChatResponse> previousChatList) {
        return PreviousChatListResponse.builder()
                .previousChatList(previousChatList)
                .build();
    }
}
