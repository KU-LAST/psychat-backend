package psychat.backend.contents.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/fill-contents")
    public void fill_contents() {
        contentService.fill_contents();
    }
}
