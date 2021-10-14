package daelim.project.eatstagram.service.member.dsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import daelim.project.eatstagram.service.member.MemberDTO;
import daelim.project.eatstagram.service.member.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.member.QMember.member;
import static daelim.project.eatstagram.service.subscription.QSubscriptionEntity.subscriptionEntity;

public class MemberDslRepositoryImpl extends QuerydslRepositorySupport implements MemberDslRepository {

    public MemberDslRepositoryImpl() {
        super(QMember.class);
    }

    @Override
    public Page<MemberDTO> getRankingList(Pageable pageable) {
        List<MemberDTO> content = from(member)
                .leftJoin(subscriptionEntity)
                .on(subscriptionEntity.username.eq(member.username))
                .select(Projections.bean(MemberDTO.class,
                        member.username,
                        member.nickname,
                        member.name,
                        member.profileImgName,
                        subscriptionEntity.subscriber.count().as("subscriberCount")
                ))
                .groupBy(subscriptionEntity.username)
                .orderBy(Expressions.numberPath(
                        Long.class,
                        "subscriberCount").desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(member)
                .leftJoin(subscriptionEntity)
                .on(subscriptionEntity.username.eq(member.username))
                .select(member)
                .groupBy(subscriptionEntity.username)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
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

    @Override // 검색
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

    @Override // 아이디로 회원 조회
    public MemberDTO getMemberInfo(String username) {
        return from(member)
                .where(member.username.eq(username))
                .select(Projections.bean(MemberDTO.class,
                        member.username,
                        member.name,
                        member.nickname,
                        member.profileImgName,
                        member.introduce
                ))
                .fetchOne();
    }
}
