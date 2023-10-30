package psychat.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
