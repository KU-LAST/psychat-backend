package psychat.backend.contents.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FillContentRequest {

    private String emotionType;
    private String videoUrl;
    private String thumbnail;
    private String uploadDate;
    private String title;
}
