package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.content.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping(value = "/stream/{contentName}")
    public void stream(@PathVariable("contentName")String contentName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        contentService.videoStream(contentName, request, response);
        log.info("테스트");
    }
}
