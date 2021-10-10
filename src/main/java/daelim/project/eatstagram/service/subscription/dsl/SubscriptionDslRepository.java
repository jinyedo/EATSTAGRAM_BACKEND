package daelim.project.eatstagram.service.subscription.dsl;

import daelim.project.eatstagram.service.subscription.SubscriptionDTO;
import daelim.project.eatstagram.service.subscription.SubscriptionEntity;

import java.util.List;

public interface SubscriptionDslRepository {
    // 구독자 리스트
    List<SubscriptionDTO> getList(String username);
    // 구독자 명단 리스트
    List<String> getSubscribersByUsername(String username);
    // 특정 구독 정보 가져오기
    SubscriptionEntity findByUsernameAndSubscriber(String username, String subscriber);
    // 총 구독자 수
    long getSubscriberTotalCount(String username);
}
