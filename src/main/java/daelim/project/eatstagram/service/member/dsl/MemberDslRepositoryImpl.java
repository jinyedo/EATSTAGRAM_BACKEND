package daelim.project.eatstagram.service.member.dsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import daelim.project.eatstagram.service.member.Member;
import daelim.project.eatstagram.service.member.MemberDTO;
import daelim.project.eatstagram.service.member.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.follow.QFollowEntity.followEntity;
import static daelim.project.eatstagram.service.member.QMember.member;

public class MemberDslRepositoryImpl extends QuerydslRepositorySupport implements MemberDslRepository {

    public MemberDslRepositoryImpl() {
        super(QMember.class);
    }

    @Override
    public Page<MemberDTO> getRankingPagingList(Pageable pageable) {
        List<MemberDTO> content = from(member)
                .leftJoin(followEntity)
                .on(followEntity.follow.eq(member.username))
                .select(Projections.bean(MemberDTO.class,
                        member.username,
                        member.nickname,
                        member.name,
                        member.profileImgName,
                        followEntity.username.count().as("followerCount")
                ))
                .groupBy(
                        member.username,
                        followEntity.follow
                )
                .having(followEntity.username.count().gt(0))
                .orderBy(Expressions.numberPath(
                        Long.class,
                        "followerCount").desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Member> total = from(member)
                .leftJoin(followEntity)
                .on(followEntity.follow.eq(member.username))
                .select(member)
                .groupBy(
                        member.username,
                        followEntity.follow
                )
                .having(followEntity.username.count().gt(0))
                .fetch();

        return new PageImpl<>(content, pageable, total.size());
    }

    @Override
    public Page<MemberDTO> getSearchPagingList(Pageable pageable, String username, String condition) {
        List<MemberDTO> content = from(member)
                .where(
                        member.username.eq(username).not(),
                        member.name.contains(condition).or(member.nickname.contains(condition))
                )
                .select(Projections.bean(MemberDTO.class,
                        member.username,
                        member.name,
                        member.nickname,
                        member.profileImgName
                ))
                .orderBy(member.username.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(member)
                .where(
                        member.username.eq(username).not(),
                        member.name.contains(condition).or(member.nickname.contains(condition))
                )
                .select(member)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override // ??????
    public List<MemberDTO> getSearchList(String username, String condition) {
        return from(member)
                .where(
                        member.username.eq(username).not(),
                        member.name.contains(condition).or(member.nickname.contains(condition))
                )
                .select(Projections.bean(MemberDTO.class,
                        member.username,
                        member.name,
                        member.nickname,
                        member.profileImgName
                ))
                .fetch();
    }

    @Override // ???????????? ?????? ??????
    public MemberDTO getMemberInfo(String username) {
        return from(member)
                .where(member.username.eq(username))
                .select(Projections.bean(MemberDTO.class,
                        member.username,
                        member.name,
                        member.nickname,
                        member.profileImgName,
                        member.introduce,
                        member.formSocial
                ))
                .fetchOne();
    }
}
