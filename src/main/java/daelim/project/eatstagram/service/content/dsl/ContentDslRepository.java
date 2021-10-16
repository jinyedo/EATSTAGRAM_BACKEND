package daelim.project.eatstagram.service.content.dsl;

import daelim.project.eatstagram.service.content.ContentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContentDslRepository {

    // 팔로우한 사람들의 콘텐츠 페이징 리스트
    Page<ContentDTO> getFollowsPagingList(Pageable pageable, String username);
    // 자신이 올린 콘텐츠 페이징 리스트
    Page<ContentDTO> getMyPagingList(Pageable pageable, String username);
    // 특정 콘텐츠 페이징 리스트
    Page<ContentDTO> getSpecificPagingList(Pageable pageable, List<String> contentIds);
    // 검색 조건을 통한 페이징 리스트
    Page<ContentDTO> getSearchPagingList(Pageable pageable, String condition);
    List<String> getContentIdsByUsername(String username);
    ContentDTO findByContentId(String contentId);
    void deleteByUsername(String username);
    void deleteByContentId(String contentId);
}
