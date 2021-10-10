package daelim.project.eatstagram.service.subscription;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService extends BaseService<String, SubscriptionEntity, SubscriptionDTO, SubscriptionRepository> {

    // 구독자 리스트
    public List<SubscriptionDTO> getList(String username, String condition) {
        List<SubscriptionDTO> getList = getRepository().getList(condition);
        for (SubscriptionDTO dto : getList) {
            String subscriptionYn = getRepository().findByUsernameAndSubscriber(username, dto.subscriber) == null ? "N" : "Y";
            dto.setSubscriptionYn(subscriptionYn);
        }
        return getList;
    }

    // 구독자 명단 리스트
    public List<String> getSubscribersByUsername(String username) {
        return getRepository().getSubscribersByUsername(username);
    }

    // 구독 여부
    public ResponseEntity<String> getSubscriptionYn(String username, String subscriber) {
        String subscriptionYn = getRepository().findByUsernameAndSubscriber(username, subscriber) == null ? "N" : "Y";
        return new ResponseEntity<>("{\"response\": \"ok\", \"subscriptionYn\": \"" + subscriptionYn + "\"," + "\"}", HttpStatus.OK);
    }

    // 구독한 사람들의 총 수
    public ResponseEntity<String> getSubscriberTotalCount(String username) {
        long subscriberTotalCount = getRepository().getSubscriberTotalCount(username);
        return new ResponseEntity<>("{\"response\": \"ok\", \"subscriberTotalCount\": \"" + subscriberTotalCount + "\"}", HttpStatus.OK);
    }

    // 구독 추가 및 삭제
    public ResponseEntity<String> save(String username, String subscriber) {
        SubscriptionEntity result = getRepository().findByUsernameAndSubscriber(username, subscriber);
        if (result == null) {
            SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder()
                    .username(username)
                    .subscriber(subscriber)
                    .build();
            super.save(subscriptionDTO);
            return new ResponseEntity<>("{\"response\": \"ok\", \"subscriptionYn\": \"Y\"}", HttpStatus.OK);
        } else {
            getRepository().delete(result);
            return new ResponseEntity<>("{\"response\": \"ok\", \"subscriptionYn\": \"N\"}", HttpStatus.OK);
        }
    }
}
