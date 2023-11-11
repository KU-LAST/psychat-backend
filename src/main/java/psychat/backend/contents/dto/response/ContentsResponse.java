package psychat.backend.contents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psychat.backend.contents.domain.Content;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsResponse {

    private String date;
    private String title;
    private String thumbnailUrl;
    private String videoUrl;

    public static ContentsResponse of(Content content) {
        return ContentsResponse.builder()
                .date(content.getUploadDate())
                .title(content.getTitle())
                .thumbnailUrl(content.getThumbnail())
                .videoUrl(content.getVideoUrl())
                .build();
    }
}
