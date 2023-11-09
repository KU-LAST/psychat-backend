package psychat.backend.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psychat.backend.chatting.domain.BotMessage;
import psychat.backend.chatting.domain.UserMessage;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String messageContent;
    private String timestamp;

    public static MessageResponse of(UserMessage userMessage) {
        return MessageResponse.builder()
                .messageContent(userMessage.getMessageContent())
                .timestamp(userMessage.getStartTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
                .build();
    }

    public static MessageResponse of(BotMessage botMessage) {
        return MessageResponse.builder()
                .messageContent(botMessage.getMessageContent())
                .timestamp(botMessage.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
                .build();
    }
}
