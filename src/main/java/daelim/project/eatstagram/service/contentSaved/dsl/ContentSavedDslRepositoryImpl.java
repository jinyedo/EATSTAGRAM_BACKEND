package daelim.project.eatstagram.service.contentSaved.dsl;

import daelim.project.eatstagram.service.contentSaved.ContentSavedEntity;
import daelim.project.eatstagram.service.contentSaved.QContentSavedEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static daelim.project.eatstagram.service.contentSaved.QContentSavedEntity.contentSavedEntity;

public class ContentSavedDslRepositoryImpl extends QuerydslRepositorySupport implements ContentSavedDslRepository {

    public ContentSavedDslRepositoryImpl() {
        super(QContentSavedEntity.class);
    }

    @Override
    public ContentSavedEntity findByUsernameAndContentId(String username, String contentId) {
        return from(contentSavedEntity)
                .where(
                        contentSavedEntity.username.eq(username),
                        contentSavedEntity.contentId.eq(contentId)
                )
                .select(contentSavedEntity)
                .fetchOne();
    }

    @Override
    public String getSavedYn(String username, String contentId) {
        ContentSavedEntity result = from(contentSavedEntity)
                .where(
                        contentSavedEntity.username.eq(username),
                        contentSavedEntity.contentId.eq(contentId)
                )
                .select(contentSavedEntity)
                .fetchOne();
        return result == null ? "N" : "Y";
    }

    @Override
    public List<String> getContentIdsByUsername(String username) {
        return from(contentSavedEntity)
                .where(contentSavedEntity.username.eq(username))
                .select(contentSavedEntity.contentId)
                .fetch();
    }

    @Override
    @Transactional @Modifying
    public void deleteByContentIds(List<String> contentIds) {
        delete(contentSavedEntity)
                .where(contentSavedEntity.contentId.in(contentIds))
                .execute();
    }

    @Override
    @Transactional @Modifying
    public void deleteByContentId(String contentId) {
        delete(contentSavedEntity)
                .where(contentSavedEntity.contentId.eq(contentId))
                .execute();
    }
}
