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

    public ContentDTO findByContentId(String contentId) {
        return getRepository().findByContentId(contentId);
    }

    public Page<ContentDTO> getPagingList(Pageable pageable) {
        return getRepository().getPagingList(pageable);
    }

    public Page<ContentDTO> getMyPagingList(Pageable pageable, String username) {
        return getRepository().getMyPagingList(pageable, username);
    }

    public Page<ContentDTO> getSavedPagingList(Pageable pageable, List<String> contentIds) {
        return getRepository().getSavedPagingListBy(pageable, contentIds);
    }
}
