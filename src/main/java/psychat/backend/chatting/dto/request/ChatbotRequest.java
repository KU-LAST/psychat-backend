package psychat.backend.chatting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequest {

    private String messageContent;

    public static ChatbotRequest of(String messageContent) {
        return ChatbotRequest.builder()
                .messageContent(messageContent)
                .build();
    }
}
