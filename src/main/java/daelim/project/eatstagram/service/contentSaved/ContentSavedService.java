package daelim.project.eatstagram.service.contentSaved;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentSavedService extends BaseService<String, ContentSavedEntity, ContentSavedDTO, ContentSavedRepository> {

    public ResponseEntity<String> save(String username, String contentId) {
        ContentSavedEntity result = getRepository().findByUsernameAndContentId(username, contentId);
        if (result == null) {
            ContentSavedDTO contentSavedDTO = ContentSavedDTO.builder()
                    .username(username)
                    .contentId(contentId)
                    .build();
            super.save(contentSavedDTO);
            return new ResponseEntity<>("{\"response\": \"ok\", \"savedYn\": \"Y\"}", HttpStatus.OK);
        } else {
            getRepository().delete(result);
            return new ResponseEntity<>("{\"response\": \"ok\", \"savedYn\": \"N\"}", HttpStatus.OK);
        }
    }

    public String getSavedYn(String username, String contentId) {
        return getRepository().getSavedYn(username, contentId);
    }

    public List<String> getContentIdsByUsername(String username) {
        return getRepository().getContentIdsByUsername(username);
    }

    public void deleteByContentIds(List<String> contentIds) {
        getRepository().deleteByContentIds(contentIds);
    }
}
