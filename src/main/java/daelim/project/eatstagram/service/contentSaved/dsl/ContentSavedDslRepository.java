package daelim.project.eatstagram.service.contentSaved.dsl;

import daelim.project.eatstagram.service.contentSaved.ContentSavedEntity;

import java.util.List;

public interface ContentSavedDslRepository {

    ContentSavedEntity findByUsernameAndContentId(String username, String contentId);
    String getSavedYn(String username, String contentId);
    List<String> getContentIdsByUsername(String username);
    void deleteByContentIds(List<String> contentIds);
    void deleteByContentId(String contentId);
}
