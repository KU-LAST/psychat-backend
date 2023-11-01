package psychat.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByToken(String token);
}
