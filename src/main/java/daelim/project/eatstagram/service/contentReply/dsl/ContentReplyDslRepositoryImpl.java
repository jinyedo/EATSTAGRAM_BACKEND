package daelim.project.eatstagram.service.contentReply.dsl;

import daelim.project.eatstagram.service.contentReply.QContentReplyEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ContentReplyDslRepositoryImpl extends QuerydslRepositorySupport implements ContentReplyDslRepository {

    public ContentReplyDslRepositoryImpl() {
        super(QContentReplyEntity.class);
    }
}
