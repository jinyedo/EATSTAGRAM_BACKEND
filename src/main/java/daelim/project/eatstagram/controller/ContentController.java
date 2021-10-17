package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.biz.ContentBizService;
import daelim.project.eatstagram.service.content.ContentDTO;
import daelim.project.eatstagram.service.content.ContentService;
import daelim.project.eatstagram.service.contentFile.ContentFileService;
import daelim.project.eatstagram.service.contentLike.ContentLikeDTO;
import daelim.project.eatstagram.service.contentLike.ContentLikeService;
import daelim.project.eatstagram.service.contentReply.ContentReplyDTO;
import daelim.project.eatstagram.service.contentReply.ContentReplyService;
import daelim.project.eatstagram.service.contentSaved.ContentSavedService;
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
    private final ContentService contentService;
    private final ContentFileService contentFileService;
    private final ContentLikeService contentLikeService;
    private final ContentReplyService contentReplyService;
    protected final ContentSavedService contentSavedService;

    // 팔로우한 사람들의 콘텐츠 페이징 리스트
    @RequestMapping("/getPagingList")
    @ResponseBody
    public Page<ContentDTO> getFollowsPagingList(Pageable pageable, String username) {
        return contentBizService.getFollowsPagingList(pageable, username);
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

    // 검색한 콘텐츠 페이징 리스트
    @RequestMapping("/getSearchPagingList")
    @ResponseBody
    public Page<ContentDTO> getSearchPagingList(Pageable pageable, String username, String condition) {
        return contentBizService.getSearchPagingList(pageable, username, condition);
    }

    // 콘텐츠 추가
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<String> add(@ModelAttribute ContentDTO contentDTO, MultipartFile[] uploadFiles) {
        return contentBizService.add(contentDTO, uploadFiles);
    }

    // 콘텐츠 삭제
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> delete(String contentId) {
        return contentBizService.delete(contentId);
    }

    // 내 저장 목록에 콘텐츠 저장하거나 삭제하기
    @RequestMapping("/save")
    @ResponseBody
    public ResponseEntity<String> save(String username, String contentId) {
        return contentBizService.save(username, contentId);
    }

    // 콘텐츠가 존재하는지 체크
    @RequestMapping("/contentCheck")
    @ResponseBody
    public ResponseEntity<String> contentCheck(String contentId) {
        return contentService.contentCheck(contentId);
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
        return contentBizService.likeSave(contentLikeDTO);
    }

    // 댓글 페이징 리스트
    @RequestMapping("/reply/getPagingList")
    @ResponseBody
    public Page<ContentReplyDTO> getPagingList(Pageable pageable, String contentId) {
        return contentReplyService.getPagingList(pageable, contentId);
    }
}
