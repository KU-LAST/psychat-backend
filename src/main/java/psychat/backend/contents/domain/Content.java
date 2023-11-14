package psychat.backend.contents.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psychat.backend.contents.dto.request.FillContentRequest;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emotionType;
    private String videoUrl;
    private String thumbnail;
    private String uploadDate;
    private String title;

    public static Content createContent(FillContentRequest request) {
        return Content.builder()
                .emotionType(request.getEmotionType())
                .videoUrl(request.getVideoUrl())
                .thumbnail(request.getThumbnail())
                .uploadDate(request.getUploadDate())
                .title(request.getTitle())
                .build();
    }

    public static Content of(String emotionType, String videoUrl, String thumbnail, String uploadDate, String title) {
        return Content.builder()
                .emotionType(emotionType)
                .videoUrl(videoUrl)
                .thumbnail(thumbnail)
                .uploadDate(uploadDate)
                .title(title)
                .build();
    }
}
