package daelim.project.eatstagram.service.contentReply.dsl;

import daelim.project.eatstagram.service.contentReply.ContentReplyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContentReplyDslRepository {

    Page<ContentReplyDTO> getPagingList(Pageable pageable, String contentId);
    long getTotalCountByContentId(String contentId);
    void deleteByContentIds(List<String> contentIds);
}
