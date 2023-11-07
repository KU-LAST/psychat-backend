package psychat.backend.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.chatting.domain.Session;
import psychat.backend.chatting.domain.UserMessage;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    List<UserMessage> findAllBySession(Session session);
}
