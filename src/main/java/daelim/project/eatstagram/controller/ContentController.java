package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.content.ContentDTO;
import daelim.project.eatstagram.service.content.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/content")
@Slf4j
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // 방 정보 가져오기
    @RequestMapping("/getPagingList")
    @ResponseBody
    public Page<ContentDTO> getPagingList(Pageable pageable) {
        return contentService.getPagingList(pageable);
    }

    @RequestMapping("/add")
    public ResponseEntity<String> add(@ModelAttribute ContentDTO contentDTO, MultipartFile[] uploadFiles) {
        return contentService.add(contentDTO, uploadFiles);
    }

    @RequestMapping(value = "/stream/{contentName}")
    public void stream(@PathVariable("contentName")String contentName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        contentService.videoStream(contentName, request, response);
    }
}
