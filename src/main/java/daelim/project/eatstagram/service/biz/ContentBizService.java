package daelim.project.eatstagram.service.biz;

import daelim.project.eatstagram.service.content.ContentDTO;
import daelim.project.eatstagram.service.content.ContentEntity;
import daelim.project.eatstagram.service.content.ContentService;
import daelim.project.eatstagram.service.contentCategory.ContentCategoryDTO;
import daelim.project.eatstagram.service.contentCategory.ContentCategoryService;
import daelim.project.eatstagram.service.contentFile.ContentFileDTO;
import daelim.project.eatstagram.service.contentFile.ContentFileService;
import daelim.project.eatstagram.service.contentHashTag.ContentHashtagDTO;
import daelim.project.eatstagram.service.contentHashTag.ContentHashtagService;
import daelim.project.eatstagram.service.contentLike.ContentLikeService;
import daelim.project.eatstagram.service.contentReply.ContentReplyService;
import daelim.project.eatstagram.service.contentSaved.ContentSavedDTO;
import daelim.project.eatstagram.service.contentSaved.ContentSavedEntity;
import daelim.project.eatstagram.service.contentSaved.ContentSavedService;
import daelim.project.eatstagram.service.follow.FollowService;
import daelim.project.eatstagram.storage.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentBizService {

    private final ContentService contentService;
    private final ContentFileService contentFileService;
    private final ContentHashtagService contentHashtagService;
    private final ContentCategoryService contentCategoryService;
    private final ContentLikeService contentLikeService;
    private final ContentReplyService contentReplyService;
    private final ContentSavedService contentSavedService;
    private final FollowService followService;
    private final StorageRepository storageRepository;

    // 팔로우한 사람들의 콘텐츠 페이징 리스트
    public Page<ContentDTO> getFollowsPagingList(Pageable pageable, String username) {
        return getDataRelatedToContent(contentService.getFollowsPagingList(pageable, username), username);
    }

    // 내 콘텐츠 페이징 리스트
    public Page<ContentDTO> getMyPagingList(Pageable pageable, String username) {
        return getDataRelatedToContent(contentService.getMyPagingList(pageable, username), username);
    }

    // 저장된 콘텐츠 페이징 리스트
    public Page<ContentDTO> getSavedPagingList(Pageable pageable, String username) {
        List<String> contentIds = contentSavedService.getContentIdsByUsername(username);
        return getDataRelatedToContent(contentService.getSpecificPagingList(pageable, contentIds), username);
    }

    // 저장된 콘텐츠 페이징 리스트
    public Page<ContentDTO> getCategoryPagingList(Pageable pageable, String username, String category) {
        List<String> contentIds = contentCategoryService.getContentIdsByCategory(category);
        return getDataRelatedToContent(contentService.getSpecificPagingList(pageable, contentIds), username);
    }

    // 검색한 콘텐츠 페이징 리스트
    public Page<ContentDTO> getSearchPagingList(Pageable pageable, String username, String condition) {
        return getDataRelatedToContent(contentService.getSearchPagingList(pageable, condition), username);
    }

    public ContentDTO findByContentId(String contentId) {
        ContentDTO contentDTO = contentService.findByContentId(contentId);
        List<ContentFileDTO> contentFileList = contentFileService.getRepository().getListByContentId(contentId);
        contentDTO.setContentFileDTOList(contentFileList);
        return contentDTO;
    }

    // 콘텐츠 추가
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> add(ContentDTO contentDTO, MultipartFile[] uploadFiles) {
        if (uploadFiles.length <= 0 || contentDTO.getContentCategoryDTOList().size() <= 0 || StringUtils.isEmpty(contentDTO.getText()))
            return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.OK);

        ContentDTO result = contentService.save(contentDTO);
        for (ContentHashtagDTO contentHashtagDTO : contentDTO.getContentHashtagDTOList()) {
            contentHashtagDTO.setContentId(result.getContentId());
            contentHashtagService.save(contentHashtagDTO);
        }
        for (ContentCategoryDTO contentCategoryDTO : contentDTO.getContentCategoryDTOList()) {
            contentCategoryDTO.setContentId(result.getContentId());
            contentCategoryService.save(contentCategoryDTO);
        }

        Path folderPath = storageRepository.makeFolder(ContentFileService.CONTENT_FILE_FOLDER_NAME);

        for (MultipartFile uploadFile : uploadFiles) {

            String fileType = uploadFile.getContentType();
            assert fileType != null;
            // "이미지" 와 "동영상" 파일만 업로드 가능
            if (!(fileType.startsWith("image") || fileType.startsWith("video"))) {
                log.warn("this file is not image or video type");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.OK);
            }

            // 실제 파일 이름 IE 나 Edge 는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            assert originalName != null;
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            String uuid = UUID.randomUUID().toString();
            fileName =  uuid + "_" + fileName;
            String saveName = folderPath + File.separator + fileName;
            Path savePath = Paths.get(saveName);

            ContentFileDTO contentFileDTO = ContentFileDTO.builder()
                    .name(fileName)
                    .type(fileType)
                    .path(savePath.normalize().toAbsolutePath().toString())
                    .contentId(result.getContentId())
                    .build();
            contentFileService.save(contentFileDTO);

            try {
                uploadFile.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("{\"response\": \"ok\"}", HttpStatus.OK);
    }

    // 콘텐츠 삭제
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> delete(String contentId) {
        try {
            Optional<ContentEntity> result = contentService.getRepository().findById(contentId);
            if (result.isEmpty()) return new ResponseEntity<>("{\"response\": \"fail\", \"msg\": \"이미 삭제된 게시글입니다.\"}", HttpStatus.OK);
            contentFileService.deleteByContentId(contentId);
            contentHashtagService.deleteByContentId(contentId);
            contentCategoryService.deleteByContentId(contentId);
            contentLikeService.deleteByContentId(contentId);
            contentReplyService.deleteByContentId(contentId);
            contentSavedService.deleteByContentId(contentId);
            contentService.deleteByContentId(contentId);
            return new ResponseEntity<>("{\"response\": \"ok\", \"msg\": \"삭제를 완료했습니다.\"}", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"response\": \"error\", \"msg\": \"서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.\"}", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> save(String username, String contentId) {
        if (contentService.findByContentId(contentId) != null) {
            ContentSavedEntity result = contentSavedService.getRepository().findByUsernameAndContentId(username, contentId);
            if (result == null) {
                ContentSavedDTO contentSavedDTO = ContentSavedDTO.builder()
                        .username(username)
                        .contentId(contentId)
                        .build();
                contentSavedService.save(contentSavedDTO);
                return new ResponseEntity<>("{\"response\": \"ok\", \"savedYn\": \"Y\"}", HttpStatus.OK);
            } else {
                contentSavedService.getRepository().delete(result);
                return new ResponseEntity<>("{\"response\": \"ok\", \"savedYn\": \"N\"}", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("{\"response\": \"fail\", \"msg\": \"해당 게시글이 삭제되어 저장하실 수 없습니다.\"}", HttpStatus.OK);
        }
    }

    // 콘텐츠와 관련된 데이터 가져오기
    private Page<ContentDTO> getDataRelatedToContent(Page<ContentDTO> contentList, String username) {
        for (ContentDTO contentDTO : contentList) {
            List<ContentFileDTO> contentFileList = contentFileService.getRepository().getListByContentId(contentDTO.getContentId());
            List<ContentHashtagDTO> contentHashtagList = contentHashtagService.getRepository().getListByContentId(contentDTO.getContentId());
            List<ContentCategoryDTO> contentCategoryList = contentCategoryService.getRepository().getListByContentId(contentDTO.getContentId());
            long likeCount = contentLikeService.getRepository().countByContentId(contentDTO.getContentId());
            boolean likeCheck = contentLikeService.getRepository().findByUsernameAndContentId(username, contentDTO.getContentId()) != null;
            long replyCount = contentReplyService.getTotalCountByContentId(contentDTO.getContentId());
            String savedYn = contentSavedService.getSavedYn(username, contentDTO.getContentId());
            contentDTO.setContentFileDTOList(contentFileList);
            contentDTO.setContentHashtagDTOList(contentHashtagList);
            contentDTO.setContentCategoryDTOList(contentCategoryList);
            contentDTO.setLikeCount(likeCount);
            contentDTO.setLikeCheck(likeCheck);
            contentDTO.setReplyCount(replyCount);
            contentDTO.setSavedYn(savedYn);
        }
        return contentList;
    }

}
