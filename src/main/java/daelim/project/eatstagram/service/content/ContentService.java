package daelim.project.eatstagram.service.content;

import daelim.project.eatstagram.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContentService extends BaseService<String, ContentEntity, ContentDTO, ContentRepository> {

    // 팔로우한 사람들의 콘텐츠 페이징 리스트
    public Page<ContentDTO> getFollowsPagingList(Pageable pageable, String username) {
        return getRepository().getFollowsPagingList(pageable, username);
    }

    // 내 콘텐츠 페이징 리스트
    public Page<ContentDTO> getMyPagingList(Pageable pageable, String username) {
        return getRepository().getMyPagingList(pageable, username);
    }

    // 특정 콘텐츠 페이징 리스트
    public Page<ContentDTO> getSpecificPagingList(Pageable pageable, List<String> contentIds) {
        return getRepository().getSpecificPagingList(pageable, contentIds);
    }

    // 검색 조건을 통한 페이징 리스트
    public Page<ContentDTO> getSearchPagingList(Pageable pageable, String condition) {
        return getRepository().getSearchPagingList(pageable, condition);
    }

    public List<String> getContentIdsByUsername(String username) {
        return getRepository().getContentIdsByUsername(username);
    }

    public ContentDTO findByContentId(String contentId) {
        return getRepository().findByContentId(contentId);
    }

    public void deleteByUsername(String username) {
        getRepository().deleteByUsername(username);
    }

    public void deleteByContentId(String contentId) {
        getRepository().deleteByContentId(contentId);
    }
}
