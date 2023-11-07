package psychat.backend.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.chatting.domain.BotMessage;
import psychat.backend.chatting.domain.Session;

import java.util.List;

public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {

    List<BotMessage> findAllBySession(Session session);
}
