package daelim.project.eatstagram.service.content.dsl;

import daelim.project.eatstagram.service.content.ContentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContentDslRepository {

    // 전체 콘텐츠 페이징 리스트
    Page<ContentDTO> getPagingList(Pageable pageable);
    // 자신이 올린 콘텐츠 페이징 리스트
    Page<ContentDTO> getMyPagingListBy(Pageable pageable, String username);
    ContentDTO findByContentId(String contentId);
}
