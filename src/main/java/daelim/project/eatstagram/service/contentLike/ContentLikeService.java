package daelim.project.eatstagram.service.contentLike;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentLikeService extends BaseService<String, ContentLikeEntity, ContentLikeDTO, ContentLikeRepository> {

    public void deleteByContentIds(List<String> contentIds) {
        getRepository().deleteByContentIds(contentIds);
    }

    public void deleteByContentId(String contentId) {
        getRepository().deleteByContentId(contentId);
    }
}
