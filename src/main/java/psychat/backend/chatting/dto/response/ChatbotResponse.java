package psychat.backend.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponse {

    private String responseContent;
    private int emotion;
}
