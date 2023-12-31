package psychat.backend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychat.backend.global.exception.NotFoundException;
import psychat.backend.member.domain.Member;
import psychat.backend.member.dto.response.LoginResponse;
import psychat.backend.member.repository.MemberRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public LoginResponse login() {
        String token = getToken();

        Member member = Member.of(token);
        memberRepository.save(member);

        return LoginResponse.of(token);
    }

    private String getToken() {
        return UUID.randomUUID().toString();
    }

    public Member findByToken(String token) {
        return memberRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }
}
