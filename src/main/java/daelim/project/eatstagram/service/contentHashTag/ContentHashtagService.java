package daelim.project.eatstagram.service.contentHashTag;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentHashtagService extends BaseService<String, ContentHashtagEntity, ContentHashtagDTO, ContentHashtagRepository> {

    public void deleteByContentIds(List<String> contentIds) {
        getRepository().deleteByContentIds(contentIds);
    }
}
