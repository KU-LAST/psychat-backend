package psychat.backend.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psychat.backend.chatting.domain.Emotion;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
