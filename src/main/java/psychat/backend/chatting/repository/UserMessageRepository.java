package psychat.backend.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.chatting.domain.UserMessage;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
}
