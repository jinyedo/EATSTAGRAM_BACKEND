package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.biz.ContentBizService;
import daelim.project.eatstagram.service.content.ContentDTO;
import daelim.project.eatstagram.service.contentFile.ContentFileService;
import daelim.project.eatstagram.service.contentLike.ContentLikeDTO;
import daelim.project.eatstagram.service.contentLike.ContentLikeService;
import daelim.project.eatstagram.service.contentReply.ContentReplyDTO;
import daelim.project.eatstagram.service.contentReply.ContentReplyService;
import daelim.project.eatstagram.service.contentSaved.ContentSavedService;
import daelim.project.eatstagram.service.subscription.SubscriptionDTO;
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

    private final ContentBizService contentBizService;
    private final ContentFileService contentFileService;
    private final ContentLikeService contentLikeService;
    private final ContentReplyService contentReplyService;
    protected final ContentSavedService contentSavedService;

    // 구독한 사람들의 콘텐츠 페이징 리스트
    @RequestMapping("/getPagingList")
    @ResponseBody
    public Page<ContentDTO> getSubscribedPagingList(Pageable pageable, String username) {
        return contentBizService.getSubscribedPagingList(pageable, username);
    }

    // 내 콘텐츠 페이징 리스트
    @RequestMapping("/getMyPagingList")
    @ResponseBody
    public Page<ContentDTO> getMyContentPagingList(Pageable pageable, String username) {
        return contentBizService.getMyPagingList(pageable, username);
    }

    // 저장된 콘텐츠 페이징 리스트
    @RequestMapping("/getSavedPagingList")
    @ResponseBody
    public Page<ContentDTO> getSavedPagingList(Pageable pageable, String username) {
        return contentBizService.getSavedPagingList(pageable, username);
    }

    // 저장된 콘텐츠 페이징 리스트
    @RequestMapping("/getCategoryPagingList")
    @ResponseBody
    public Page<ContentDTO> getCategoryPagingList(Pageable pageable, String username, String category) {
        return contentBizService.getCategoryPagingList(pageable, username, category);
    }

    // 콘텐츠 추가
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<String> add(@ModelAttribute ContentDTO contentDTO, MultipartFile[] uploadFiles) {
        return contentBizService.add(contentDTO, uploadFiles);
    }

    // 내 저장 목록에 콘텐츠 저장하거나 삭제하기
    @RequestMapping("/save")
    @ResponseBody
    public ResponseEntity<String> save(String username, String contentId) {
        return contentSavedService.save(username, contentId);
    }

    // 비디오 스트림
    @RequestMapping(value = "/stream/{contentName}")
    @ResponseBody
    public void stream(@PathVariable("contentName")String contentName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        contentFileService.videoStream(contentName, request, response);
    }

    // 콘테츠 좋아요
    @RequestMapping("/like/save")
    @ResponseBody
    public ResponseEntity<String> likeSave(@ModelAttribute ContentLikeDTO contentLikeDTO) {
        return contentLikeService.save(contentLikeDTO);
    }

    // 댓글 페이징 리스트
    @RequestMapping("/reply/getPagingList")
    @ResponseBody
    public Page<ContentReplyDTO> getPagingList(Pageable pageable, String contentId) {
        return contentReplyService.getPagingList(pageable, contentId);
    }
}
