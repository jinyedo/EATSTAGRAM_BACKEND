package daelim.project.eatstagram.service.contentReply;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentReplyService extends BaseService<String, ContentReplyEntity, ContentReplyDTO, ContentReplyRepository> {

    public Page<ContentReplyDTO> getPagingList(Pageable pageable, String contentId) {
        return getRepository().getPagingList(pageable, contentId);
    }

    public long getTotalCountByContentId(String contentId) {
        return getRepository().getTotalCountByContentId(contentId);
    }

    public void deleteByContentIds(List<String> contentIds) {
        getRepository().deleteByContentIds(contentIds);
    }
}
