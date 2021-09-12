package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.contentLike.ContentLikeDTO;
import daelim.project.eatstagram.service.contentLike.ContentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contentLike")
@RequiredArgsConstructor
public class ContentLikeController {

    private final ContentLikeService contentLikeService;

    @RequestMapping("/save")
    public ResponseEntity<String> save(@RequestBody ContentLikeDTO contentLikeDTO) {
        return contentLikeService.save(contentLikeDTO);
    }
}
