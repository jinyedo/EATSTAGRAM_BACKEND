package daelim.project.eatstagram.service.subscription.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.subscription.QSubscriptionEntity;
import daelim.project.eatstagram.service.subscription.SubscriptionDTO;
import daelim.project.eatstagram.service.subscription.SubscriptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.member.QMember.member;
import static daelim.project.eatstagram.service.subscription.QSubscriptionEntity.subscriptionEntity;

public class SubscriptionDslRepositoryImpl extends QuerydslRepositorySupport implements SubscriptionDslRepository {

    public SubscriptionDslRepositoryImpl() {
        super(QSubscriptionEntity.class);
    }

    @Override
    public Page<SubscriptionDTO> getPagingList(Pageable pageable, SubscriptionDTO subscriptionDTO) {
        List<SubscriptionDTO> content = from(subscriptionEntity)
                .where(subscriptionEntity.username.eq(subscriptionDTO.getUsername()))
                .leftJoin(member)
                .on(member.username.eq(subscriptionEntity.subscriber))
                .select(Projections.bean(SubscriptionDTO.class,
                        subscriptionEntity.subscriber,
                        member.nickname,
                        member.name,
                        member.profileImgName
                ))
                .orderBy(subscriptionEntity.subscriber.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(subscriptionEntity)
                .where(subscriptionEntity.username.eq(subscriptionDTO.getUsername()))
                .leftJoin(member)
                .on(member.username.eq(subscriptionEntity.subscriber))
                .select(subscriptionEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    public List<String> getSubscribersByUsername(String username) {
        return from(subscriptionEntity)
                .where(subscriptionEntity.username.eq(username))
                .select(subscriptionEntity.subscriber)
                .fetch();
    }

    @Override
    public SubscriptionEntity findByUsernameAndSubscriber(String username, String subscriber) {
        return from(subscriptionEntity)
                .where(
                        subscriptionEntity.username.eq(username),
                        subscriptionEntity.subscriber.eq(subscriber)
                )
                .select(subscriptionEntity)
                .fetchOne();
    }

    @Override
    public long getSubscriberTotalCount(String username) {
        return from(subscriptionEntity)
                .where(subscriptionEntity.username.eq(username))
                .select(subscriptionEntity)
                .fetchCount();
    }
}
