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

    // 구독한 사람들의 콘텐츠 페이징 리스트
    public Page<ContentDTO> getSubscribedPagingList(Pageable pageable, List<String> subscribers) {
        return getRepository().getSubscribedPagingList(pageable, subscribers);
    }

    // 내 콘텐츠 페이징 리스트
    public Page<ContentDTO> getMyPagingList(Pageable pageable, String username) {
        return getRepository().getMyPagingList(pageable, username);
    }

    // 저장된 콘텐츠 페이징 리스트
    public Page<ContentDTO> getSavedPagingList(Pageable pageable, List<String> contentIds) {
        return getRepository().getSavedPagingListBy(pageable, contentIds);
    }

    public ContentDTO findByContentId(String contentId) {
        return getRepository().findByContentId(contentId);
    }
}
