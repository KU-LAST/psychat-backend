package psychat.backend.chatting;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.chatting.domain.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
