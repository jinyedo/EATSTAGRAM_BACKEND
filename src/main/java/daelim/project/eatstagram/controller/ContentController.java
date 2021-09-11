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

@RestController
@RequestMapping("/content")
@Slf4j
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // 콘텐츠 페이징 리스트
    @RequestMapping("/getPagingList")
    @ResponseBody
    public Page<ContentDTO> getPagingList(Pageable pageable) {
        return contentService.getPagingList(pageable);
    }

    // 콘텐츠 추가
    @RequestMapping("/add")
    public ResponseEntity<String> add(@ModelAttribute ContentDTO contentDTO, MultipartFile[] uploadFiles) throws Exception {
        return contentService.add(contentDTO, uploadFiles);
    }

    // 비디오 스트림
    @RequestMapping(value = "/stream/{contentName}")
    public void stream(@PathVariable("contentName")String contentName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        contentService.videoStream(contentName, request, response);
    }
}
