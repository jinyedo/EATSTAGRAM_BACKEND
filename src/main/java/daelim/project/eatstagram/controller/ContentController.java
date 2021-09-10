package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.content.ContentDTO;
import daelim.project.eatstagram.service.content.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/content")
@Slf4j
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @RequestMapping("/add")
    public ResponseEntity<Objects> add(@ModelAttribute ContentDTO contentDTO, MultipartFile[] uploadFiles) {
        return contentService.add(contentDTO, uploadFiles);
    }

    @RequestMapping(value = "/stream/{contentName}")
    public void stream(@PathVariable("contentName")String contentName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        contentService.videoStream(contentName, request, response);
    }
}
