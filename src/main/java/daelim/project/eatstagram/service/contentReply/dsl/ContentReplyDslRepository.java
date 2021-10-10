package daelim.project.eatstagram.service.contentReply.dsl;

import daelim.project.eatstagram.service.contentReply.ContentReplyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContentReplyDslRepository {

    Page<ContentReplyDTO> getPagingList(Pageable pageable, String contentId);
    public long getTotalCountByContentId(String contentId);
}
