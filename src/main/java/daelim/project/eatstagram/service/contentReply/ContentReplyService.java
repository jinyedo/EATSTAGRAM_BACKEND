package daelim.project.eatstagram.service.contentReply;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContentReplyService extends BaseService<String, ContentReplyEntity, ContentReplyDTO, ContentReplyRepository> {

    public Page<ContentReplyDTO> getContentReplyPagingList(Pageable pageable, String contentId) {
        return getRepository().getContentReplyPagingList(pageable, contentId);
    }
}
