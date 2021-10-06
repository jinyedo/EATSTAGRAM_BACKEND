package daelim.project.eatstagram.service.content.dsl;

import daelim.project.eatstagram.service.content.ContentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContentDslRepository {

    Page<ContentDTO> getPagingList(Pageable pageable);
    ContentDTO findByContentId(String contentId);
}
