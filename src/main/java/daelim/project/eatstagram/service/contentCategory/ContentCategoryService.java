package daelim.project.eatstagram.service.contentCategory;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentCategoryService extends BaseService<String, ContentCategoryEntity, ContentCategoryDTO, ContentCategoryRepository> {

    public List<String> getContentIdsByCategory(String category) {
        return getRepository().getContentIdsByCategory(category);
    }

    public void deleteByContentIds(List<String> contentIds) {
        getRepository().deleteByContentIds(contentIds);
    }
}
