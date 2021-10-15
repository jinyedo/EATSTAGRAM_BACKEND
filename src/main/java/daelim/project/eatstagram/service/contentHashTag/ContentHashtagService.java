package daelim.project.eatstagram.service.contentHashTag;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ContentHashtagService extends BaseService<String, ContentHashtagEntity, ContentHashtagDTO, ContentHashtagRepository> {

    public void deleteByContentId(String contentId) {
        getRepository().deleteByContentId(contentId);
    }
}
