package psychat.backend.chatting.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import psychat.backend.chatting.dto.response.SessionResponse;
import psychat.backend.chatting.service.ChattingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ChattingApi {

    private final ChattingService chattingService;

    @GetMapping("/start-chat")
    public SessionResponse start(@RequestParam String token) {
        return chattingService.start(token);
    }
}
