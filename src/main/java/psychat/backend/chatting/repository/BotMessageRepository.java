package psychat.backend.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.chatting.domain.BotMessage;

public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {
}
