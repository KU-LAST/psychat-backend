package psychat.backend.member.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psychat.backend.member.dto.response.LoginResponse;
import psychat.backend.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApi {

    private final MemberService memberService;

    @GetMapping("/login")
    public LoginResponse login() {
        return memberService.login();
    }
}
