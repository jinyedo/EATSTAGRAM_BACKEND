package daelim.project.eatstagram.service.follow.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.follow.FollowDTO;
import daelim.project.eatstagram.service.follow.FollowEntity;
import daelim.project.eatstagram.service.follow.QFollowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static daelim.project.eatstagram.service.follow.QFollowEntity.*;
import static daelim.project.eatstagram.service.member.QMember.*;

public class FollowDslRepositoryImpl extends QuerydslRepositorySupport implements FollowDslRepository {

    public FollowDslRepositoryImpl() {
        super(QFollowEntity.class);
    }

/* 팔로우 */
    @Override
    public Page<FollowDTO> getFollowPagingList(Pageable pageable, String target) {
        List<FollowDTO> content = from(followEntity)
                .where(followEntity.username.eq(target))
                .leftJoin(member)
                .on(member.username.eq(followEntity.follow))
                .select(Projections.bean(FollowDTO.class,
                        followEntity.follow,
                        member.nickname,
                        member.name,
                        member.profileImgName
                ))
                .orderBy(followEntity.follow.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(followEntity)
                .where(followEntity.username.eq(target))
                .leftJoin(member)
                .on(member.username.eq(followEntity.follow))
                .select(followEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public FollowEntity followCheck(String username, String target) {
        return from(followEntity)
                .where(
                        followEntity.username.eq(username),
                        followEntity.follow.eq(target)
                )
                .select(followEntity)
                .fetchOne();
    }

    @Override
    public long getFollowCount(String target) {
        return from(followEntity)
                .where(followEntity.username.eq(target))
                .select(followEntity)
                .fetchCount();
    }


/* 팔로워 */
    @Override
    public Page<FollowDTO> getFollowerPagingList(Pageable pageable, String target) {
        List<FollowDTO> content = from(followEntity)
                .where(followEntity.follow.eq(target))
                .leftJoin(member)
                .on(member.username.eq(followEntity.username))
                .select(Projections.bean(FollowDTO.class,
                        followEntity.username.as("follower"),
                        member.nickname,
                        member.name,
                        member.profileImgName
                ))
                .orderBy(followEntity.username.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(followEntity)
                .where(followEntity.follow.eq(target))
                .leftJoin(member)
                .on(member.username.eq(followEntity.username))
                .select(followEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public FollowEntity followerCheck(String username, String target) {
        return from(followEntity)
                .where(
                        followEntity.username.eq(target),
                        followEntity.follow.eq(username)
                )
                .select(followEntity)
                .fetchOne();
    }

    @Override
    public long getFollowerCount(String target) {
        return from(followEntity)
                .where(followEntity.follow.eq(target))
                .select(followEntity)
                .fetchCount();
    }

    @Override
    @Transactional @Modifying
    public void delete(String username, String target) {
        delete(followEntity)
                .where(
                    followEntity.username.eq(target),
                    followEntity.follow.eq(username)
                )
                .execute();
    }


}
