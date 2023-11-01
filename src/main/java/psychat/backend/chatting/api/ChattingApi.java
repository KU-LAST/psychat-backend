package psychat.backend.chatting.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import psychat.backend.chatting.dto.request.ChattingRequest;
import psychat.backend.chatting.dto.response.ChattingResponse;
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

    @PostMapping("/actual-chat")
    public ChattingResponse chat(@RequestParam String token, @RequestBody ChattingRequest request) {
        return chattingService.chat(token, request);
    }

}
