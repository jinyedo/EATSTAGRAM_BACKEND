package daelim.project.eatstagram.service.contentSaved.dsl;

import daelim.project.eatstagram.service.contentSaved.ContentSavedEntity;
import daelim.project.eatstagram.service.contentSaved.QContentSavedEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.contentSaved.QContentSavedEntity.*;

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


    public List<String> getContentIdsByUsername(String username) {
        return from(contentSavedEntity)
                .where(contentSavedEntity.username.eq(username))
                .select(contentSavedEntity.contentId)
                .fetch();
    }
}
