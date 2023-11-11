package psychat.backend.contents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psychat.backend.contents.domain.Content;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findAllByEmotionType(String emotionType);
}
