package daelim.project.eatstagram.service.content.dsl;

import daelim.project.eatstagram.service.content.ContentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContentDslRepository {

    // 구독한 사람들의 콘텐츠 페이징 리스트
    Page<ContentDTO> getSubscribedPagingList(Pageable pageable, List<String> subscribers);
    // 자신이 올린 콘텐츠 페이징 리스트
    Page<ContentDTO> getMyPagingList(Pageable pageable, String username);
    // 특정 콘텐츠 페이징 리스트
    Page<ContentDTO> getSpecificPagingList(Pageable pageable, List<String> contentIds);
    ContentDTO findByContentId(String contentId);
}
