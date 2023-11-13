package psychat.backend.chatting.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import psychat.backend.chatting.dto.request.ChattingRequest;
import psychat.backend.chatting.dto.request.CheckRequest;
import psychat.backend.chatting.dto.request.EndRequest;
import psychat.backend.chatting.dto.response.*;
import psychat.backend.chatting.service.ChattingService;
import psychat.backend.chatting.service.EmotionService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ChattingApi {

    private final ChattingService chattingService;
    private final EmotionService emotionService;

    @GetMapping("/start-chat")
    public SessionResponse start(@RequestParam String token) {
        return chattingService.start(token);
    }

    @PostMapping("/actual-chat")
    public ChattingResponse chat(@RequestParam String token, @RequestBody ChattingRequest request) {
        return chattingService.chat(token, request);
    }

    @GetMapping("/fill-emotion")
    public void fill() {
        emotionService.fill();
    }

    @PostMapping("/end-chat")
    public EmotionResponse end(@RequestParam String token, @RequestBody EndRequest request) {
        return chattingService.end(token, request);
    }

    @GetMapping("/chat/{session-id}")
    public ChattingListResponse chattingListBySessionId(@PathVariable("session-id") Long sessionId) {
        return chattingService.chattingListBySessionId(sessionId);
    }

    @GetMapping("/previous-chats")
    public PreviousChatListResponse previousChats(@RequestParam String token) {
        return chattingService.previousChats(token);
    }

    @PostMapping("/check")
    public Map<String, Boolean> check(@RequestParam String token, @RequestBody CheckRequest request) {
        return chattingService.check(token, request);
    }
}
