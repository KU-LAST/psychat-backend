package psychat.backend.chatting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    private String messageContent;

    private String emotion;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime startTime;

    public static UserMessage of(Session session, String messageContent, String emotion) {
        return UserMessage.builder()
                .session(session)
                .messageContent(messageContent)
                .emotion(emotion)
                .build();
    }
}
