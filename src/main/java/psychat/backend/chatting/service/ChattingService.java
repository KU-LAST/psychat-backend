package psychat.backend.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychat.backend.chatting.SessionRepository;
import psychat.backend.chatting.domain.Session;
import psychat.backend.chatting.dto.response.SessionResponse;
import psychat.backend.member.domain.Member;
import psychat.backend.member.service.MemberService;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final MemberService memberService;
    private final SessionRepository sessionRepository;

    public SessionResponse start(String token) {
        Member findMember = memberService.findByToken(token);

        Session session = Session.init(findMember);
        Session savedSession = sessionRepository.save(session);

        return SessionResponse.of(savedSession.getId());
    }
}
