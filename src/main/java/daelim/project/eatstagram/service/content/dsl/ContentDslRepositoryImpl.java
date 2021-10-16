package daelim.project.eatstagram.service.content.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.content.ContentDTO;
import daelim.project.eatstagram.service.content.ContentEntity;
import daelim.project.eatstagram.service.content.QContentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static daelim.project.eatstagram.service.content.QContentEntity.contentEntity;
import static daelim.project.eatstagram.service.contentHashTag.QContentHashtagEntity.contentHashtagEntity;
import static daelim.project.eatstagram.service.follow.QFollowEntity.followEntity;
import static daelim.project.eatstagram.service.member.QMember.member;

public class ContentDslRepositoryImpl extends QuerydslRepositorySupport implements ContentDslRepository {

    public ContentDslRepositoryImpl() {
        super(QContentEntity.class);
    }

    @Override
    public Page<ContentDTO> getFollowsPagingList(Pageable pageable, String username) {

        List<ContentDTO> content = from(contentEntity)
                .where(
                        contentEntity.username.eq(username)
                        .or(followEntity.username.eq(username)
                            .and(followEntity.follow.eq(contentEntity.username)))
                )
                .leftJoin(member).on(member.username.eq(contentEntity.username))
                .leftJoin(followEntity).on(followEntity.follow.eq(contentEntity.username))
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.contentId,
                        contentEntity.text,
                        contentEntity.location,
                        contentEntity.regDate,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .orderBy(contentEntity.contentId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(contentEntity)
                .where(
                        contentEntity.username.eq(username)
                        .or(followEntity.username.eq(username)
                        .and(followEntity.follow.eq(contentEntity.username)))
                )
                .leftJoin(member).on(member.username.eq(contentEntity.username))
                .leftJoin(followEntity).on(followEntity.follow.eq(contentEntity.username))
                .select(contentEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ContentDTO> getMyPagingList(Pageable pageable, String username) {
        List<ContentDTO> content = from(contentEntity)
                .where(
                        contentEntity.username.eq(username)
                )
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.contentId,
                        contentEntity.text,
                        contentEntity.location,
                        contentEntity.regDate,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .orderBy(contentEntity.contentId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(contentEntity)
                .where(contentEntity.username.eq(username))
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(contentEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ContentDTO> getSpecificPagingList(Pageable pageable, List<String> contentIds) {
        List<ContentDTO> content = from(contentEntity)
                .where(contentEntity.contentId.in(contentIds))
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.contentId,
                        contentEntity.text,
                        contentEntity.location,
                        contentEntity.regDate,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .orderBy(contentEntity.contentId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(contentEntity)
                .where(contentEntity.contentId.in(contentIds))
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(contentEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ContentDTO> getSearchPagingList(Pageable pageable, String condition) {
        List<ContentDTO> content = from(contentEntity)
                .where(
                        contentEntity.text.contains(condition)
                                .or(contentEntity.location.contains(condition)
                                        .or(contentHashtagEntity.hashtag.contains(condition)))
                )
                .leftJoin(contentHashtagEntity).on(contentHashtagEntity.contentId.eq(contentEntity.contentId))
                .leftJoin(member).on(member.username.eq(contentEntity.username))
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.contentId,
                        contentEntity.text,
                        contentEntity.location,
                        contentEntity.regDate,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .orderBy(contentEntity.contentId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        long total = from(contentEntity)
                .where(
                        contentEntity.text.contains(condition)
                                .or(contentEntity.location.contains(condition)
                                        .or(contentHashtagEntity.hashtag.contains(condition)))
                )
                .leftJoin(contentHashtagEntity).on(contentHashtagEntity.contentId.eq(contentEntity.contentId))
                .leftJoin(member).on(member.username.eq(contentEntity.username))
                .select(contentEntity)
                .distinct()
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<String> getContentIdsByUsername(String username) {
        return from(contentEntity)
                .where(contentEntity.username.eq(username))
                .select(contentEntity.contentId)
                .fetch();
    }

    @Override
    public ContentDTO findByContentId(String contentId) {
        return from(contentEntity)
                .where(contentEntity.contentId.eq(contentId))
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.text,
                        contentEntity.location,
                        contentEntity.regDate,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .fetchOne();
    }

    @Override
    public ContentEntity contentCheck(String contentId) {
        return from(contentEntity)
                .where(contentEntity.contentId.eq(contentId))
                .select(contentEntity)
                .fetchOne();
    }

    @Override
    @Transactional @Modifying
    public void deleteByUsername(String username) {
        delete(contentEntity)
                .where(contentEntity.username.eq(username))
                .execute();
    }

    @Override
    @Transactional @Modifying
    public void deleteByContentId(String contentId) {
        delete(contentEntity)
                .where(contentEntity.contentId.eq(contentId))
                .execute();
    }
}
