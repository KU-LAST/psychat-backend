package psychat.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping("/connection-test")
    public Map<String, String> check() {
        Map<String, String> payload = new HashMap<>();
        payload.put("connection", "success");

        return payload;
    }
}
