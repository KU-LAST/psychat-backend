package psychat.backend.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import psychat.backend.chatting.domain.Session;
import psychat.backend.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findById(Long id);

    @Query("select s from Session s where s.member = :member order by s.id desc")
    List<Session> findAllByMember(@Param("member") Member member);
}
