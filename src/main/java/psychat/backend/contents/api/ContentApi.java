package psychat.backend.contents.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psychat.backend.contents.dto.request.ContentsRequest;
import psychat.backend.contents.dto.request.FillContentRequest;
import psychat.backend.contents.dto.response.ContentsListResponse;
import psychat.backend.contents.service.ContentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ContentApi {

    private final ContentService contentService;

    @PostMapping("/recommended-contents")
    public ContentsListResponse recommended_contents(@RequestBody ContentsRequest request) {
        return contentService.recommended_contents(request);
    }

    @PostMapping("/fill-content")
    public void fill_content(@RequestBody FillContentRequest request) {
        contentService.fill_content(request);
    }

}
