package daelim.project.eatstagram.service.contentLike.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.contentLike.ContentLikeDTO;
import daelim.project.eatstagram.service.contentLike.QContentLikeEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static daelim.project.eatstagram.service.contentLike.QContentLikeEntity.contentLikeEntity;

public class ContentLikeDslRepositoryImpl extends QuerydslRepositorySupport implements ContentLikeDslRepository {

    public ContentLikeDslRepositoryImpl() {
        super(QContentLikeEntity.class);
    }

    @Override
    public ContentLikeDTO findByUsernameAndContentId(String username, String contentId) {
        return from(contentLikeEntity)
                .where(
                        contentLikeEntity.username.eq(username),
                        contentLikeEntity.contentId.eq(contentId)
                )
                .select(Projections.bean(ContentLikeDTO.class))
                .fetchOne();
    }

    @Override
    public long countByContentId(String contentId) {
        return from(contentLikeEntity)
                .where(contentLikeEntity.contentId.eq(contentId))
                .fetchCount();
    }

}
