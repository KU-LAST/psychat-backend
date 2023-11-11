package psychat.backend.contents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsListResponse {

    private List<ContentsResponse> contentsList;

    public static ContentsListResponse of(List<ContentsResponse> contentsList) {
        return ContentsListResponse.builder()
                .contentsList(contentsList)
                .build();
    }
}
