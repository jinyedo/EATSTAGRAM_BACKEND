package daelim.project.eatstagram.service.subscription.dsl;

import daelim.project.eatstagram.service.subscription.SubscriptionDTO;
import daelim.project.eatstagram.service.subscription.SubscriptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionDslRepository {
    // 구독자 페이징 리스트
    Page<SubscriptionDTO> getPagingList(Pageable pageable, String condition);
    // 구독자 명단 리스트
    List<String> getSubscribersByUsername(String username);
    // 특정 구독 정보 가져오기
    SubscriptionEntity findByUsernameAndSubscriber(String username, String subscriber);
    // 총 구독자 수
    long getSubscriberTotalCount(String username);
}
