package daelim.project.eatstagram.service.contentLike.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.contentLike.ContentLikeDTO;
import daelim.project.eatstagram.service.like.QLikeEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static daelim.project.eatstagram.service.like.QLikeEntity.*;

public class ContentLikeDslRepositoryImpl extends QuerydslRepositorySupport implements ContentLikeDslRepository {

    public ContentLikeDslRepositoryImpl() {
        super(QLikeEntity.class);
    }

    @Override
    public ContentLikeDTO findByUsernameAndContentId(String username, String contentId) {
        return from(likeEntity)
                .where(
                        likeEntity.username.eq(username),
                        likeEntity.contentId.eq(contentId)
                )
                .select(Projections.bean(ContentLikeDTO.class))
                .fetchOne();
    }

    @Override
    public long countByContentId(String contentId) {
        return from(likeEntity)
                .where(likeEntity.contentId.eq(contentId))
                .fetchCount();
    }

}
