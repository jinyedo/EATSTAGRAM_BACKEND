package daelim.project.eatstagram.service.subscription.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.subscription.QSubscriptionEntity;
import daelim.project.eatstagram.service.subscription.SubscriptionDTO;
import daelim.project.eatstagram.service.subscription.SubscriptionEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.subscription.QSubscriptionEntity.subscriptionEntity;
import static daelim.project.eatstagram.service.member.QMember.*;

public class SubscriptionDslRepositoryImpl extends QuerydslRepositorySupport implements SubscriptionDslRepository {

    public SubscriptionDslRepositoryImpl() {
        super(QSubscriptionEntity.class);
    }

    @Override
    public List<SubscriptionDTO> getList(String username) {
        return from(subscriptionEntity)
                .where(subscriptionEntity.username.eq(username))
                .leftJoin(member)
                .on(member.username.eq(subscriptionEntity.subscriber))
                .select(Projections.bean(SubscriptionDTO.class,
                        subscriptionEntity.subscriber,
                        member.nickname,
                        member.name,
                        member.profileImgName
                ))
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
