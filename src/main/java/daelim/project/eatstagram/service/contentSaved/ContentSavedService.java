package daelim.project.eatstagram.service.contentSaved;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentSavedService extends BaseService<String, ContentSavedEntity, ContentSavedDTO, ContentSavedRepository> {

    public String getSavedYn(String username, String contentId) {
        return getRepository().getSavedYn(username, contentId);
    }

    public List<String> getContentIdsByUsername(String username) {
        return getRepository().getContentIdsByUsername(username);
    }

    public void deleteByContentIds(List<String> contentIds) {
        getRepository().deleteByContentIds(contentIds);
    }

    public void deleteByContentId(String contentId) {
        getRepository().deleteByContentId(contentId);
    }
}
