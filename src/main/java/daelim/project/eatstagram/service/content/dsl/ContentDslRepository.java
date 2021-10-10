package daelim.project.eatstagram.service.content.dsl;

import daelim.project.eatstagram.service.content.ContentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContentDslRepository {

    // 전체 콘텐츠 페이징 리스트
    Page<ContentDTO> getPagingList(Pageable pageable);
    // 자신이 올린 콘텐츠 페이징 리스트
    Page<ContentDTO> getMyPagingList(Pageable pageable, String username);
    // 저장된 콘텐츠 페이징 리스트
    Page<ContentDTO> getSavedPagingListBy(Pageable pageable, List<String> contentIds);
    ContentDTO findByContentId(String contentId);
}
