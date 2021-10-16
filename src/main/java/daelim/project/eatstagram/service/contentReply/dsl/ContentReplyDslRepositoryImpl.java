package daelim.project.eatstagram.service.contentReply.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.contentReply.ContentReplyDTO;
import daelim.project.eatstagram.service.contentReply.QContentReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static daelim.project.eatstagram.service.contentReply.QContentReplyEntity.contentReplyEntity;
import static daelim.project.eatstagram.service.member.QMember.member;

public class ContentReplyDslRepositoryImpl extends QuerydslRepositorySupport implements ContentReplyDslRepository {

    public ContentReplyDslRepositoryImpl() {
        super(QContentReplyEntity.class);
    }

    @Override
    public Page<ContentReplyDTO> getPagingList(Pageable pageable, String contentId) {
        List<ContentReplyDTO> content = from(contentReplyEntity)
                .where(
                        contentReplyEntity.contentId.eq(contentId)
                )
                .leftJoin(member)
                .on(member.username.eq(contentReplyEntity.username))
                .select(Projections.bean(ContentReplyDTO.class,
                        contentReplyEntity.contentReplyId,
                        contentReplyEntity.reply,
                        contentReplyEntity.contentId,
                        contentReplyEntity.regDate,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .orderBy(contentReplyEntity.contentReplyId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(contentReplyEntity)
                .where(contentReplyEntity.contentId.eq(contentId))
                .leftJoin(member)
                .on(member.username.eq(contentReplyEntity.username))
                .select(contentReplyEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public long getTotalCountByContentId(String contentId){
        return from(contentReplyEntity)
                .where(contentReplyEntity.contentId.eq(contentId))
                .select(contentReplyEntity)
                .fetchCount();
    }

    @Override
    @Transactional @Modifying
    public void deleteByContentIds(List<String> contentIds) {
        delete(contentReplyEntity)
                .where(contentReplyEntity.contentId.in(contentIds))
                .execute();
    }

    @Override
    @Transactional @Modifying
    public void deleteByContentId(String contentId) {
        delete(contentReplyEntity)
                .where(contentReplyEntity.contentId.eq(contentId))
                .execute();
    }
}
