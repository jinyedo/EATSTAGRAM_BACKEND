package daelim.project.eatstagram.service.content;

import daelim.project.eatstagram.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContentService extends BaseService<String, ContentEntity, ContentDTO, ContentRepository> {

    public ContentDTO findByContentId(String contentId) {
        return getRepository().findByContentId(contentId);
    }
}
