package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.contentLike.ContentLikeDTO;
import daelim.project.eatstagram.service.contentLike.ContentLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contentLike")
@RequiredArgsConstructor
@Log4j2
public class ContentLikeController {

    private final ContentLikeService contentLikeService;

    @RequestMapping("/save")
    @ResponseBody
    public ResponseEntity<String> save(@ModelAttribute ContentLikeDTO contentLikeDTO) {
        log.info(contentLikeDTO);
        return contentLikeService.save(contentLikeDTO);
    }
}
