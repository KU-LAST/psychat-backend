package psychat.backend.chatting;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.chatting.domain.Session;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findById(Long id);
}
