package daelim.project.eatstagram.service.subscription;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService extends BaseService<String, SubscriptionEntity, SubscriptionDTO, SubscriptionRepository> {

    public List<SubscriptionDTO> getList(String username) {
        return getRepository().getList(username);
    }

    public ResponseEntity<String> getSubscriptionYn(String username, String subscriber) {
        String subscriptionYn = getRepository().findByUsernameAndSubscriber(username, subscriber) == null ? "N" : "Y";
        return new ResponseEntity<>("{\"response\": \"ok\", \"subscriptionYn\": \"" + subscriptionYn + "\"," + "\"}", HttpStatus.OK);
    }

    public ResponseEntity<String> getSubscriberTotalCount(String username) {
        long subscriberTotalCount = getRepository().getSubscriberTotalCount(username);
        return new ResponseEntity<>("{\"response\": \"ok\", \"subscriberTotalCount\": \"" + subscriberTotalCount + "\"}", HttpStatus.OK);
    }

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
