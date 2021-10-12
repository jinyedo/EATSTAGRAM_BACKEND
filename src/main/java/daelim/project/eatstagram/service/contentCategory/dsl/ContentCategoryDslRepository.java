package daelim.project.eatstagram.service.contentCategory.dsl;

import daelim.project.eatstagram.service.contentCategory.ContentCategoryDTO;

import java.util.List;

public interface ContentCategoryDslRepository {

    List<ContentCategoryDTO> getListByContentId(String contentId);
    List<String> getContentIdsByCategory(String category);
}
