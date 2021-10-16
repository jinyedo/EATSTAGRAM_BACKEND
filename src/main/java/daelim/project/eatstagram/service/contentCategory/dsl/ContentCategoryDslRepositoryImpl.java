package daelim.project.eatstagram.service.contentCategory.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.contentCategory.ContentCategoryDTO;
import daelim.project.eatstagram.service.contentCategory.QContentCategoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static daelim.project.eatstagram.service.contentCategory.QContentCategoryEntity.*;

public class ContentCategoryDslRepositoryImpl extends QuerydslRepositorySupport implements ContentCategoryDslRepository {

    public ContentCategoryDslRepositoryImpl() {
        super(QContentCategoryEntity.class);
    }

    @Override
    public List<ContentCategoryDTO> getListByContentId(String contentId) {
        return from(contentCategoryEntity)
                .where(contentCategoryEntity.contentId.eq(contentId))
                .select(Projections.bean(ContentCategoryDTO.class,
                        contentCategoryEntity.contentCategoryId,
                        contentCategoryEntity.category))
                .fetch();
    }

    @Override
    public List<String> getContentIdsByCategory(String category) {
        return from(contentCategoryEntity)
                .where(contentCategoryEntity.category.eq(category))
                .select(contentCategoryEntity.contentId)
                .fetch();
    }

    @Override
    @Transactional @Modifying
    public void deleteByContentIds(List<String> contentIds) {
        delete(contentCategoryEntity)
                .where(contentCategoryEntity.contentId.in(contentIds))
                .execute();
    }
}
