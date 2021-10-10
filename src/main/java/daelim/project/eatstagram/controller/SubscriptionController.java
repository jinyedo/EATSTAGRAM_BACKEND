package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.subscription.SubscriptionDTO;
import daelim.project.eatstagram.service.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // 구독자 리스트
    @RequestMapping("/getList")
    @ResponseBody
    public List<SubscriptionDTO> getList(String username, String condition) {
        return subscriptionService.getList(username, condition);
    }

    // 구독 여부
    @RequestMapping("/getSubscriptionYn")
    @ResponseBody
    public ResponseEntity<String> getSubscriptionYn(String username, String subscriber) {
        return subscriptionService.getSubscriptionYn(username, subscriber);
    }

    // 구독한 사람들의 총 수
    @RequestMapping("/getSubscriberTotalCount")
    @ResponseBody
    public ResponseEntity<String> getSubscriberTotalCount(String username) {
        return subscriptionService.getSubscriberTotalCount(username);
    }

    // 구독 추가 및 삭제
    @RequestMapping("/save")
    @ResponseBody
    public ResponseEntity<String> save(String username, String subscriber) {
        return subscriptionService.save(username, subscriber);
    }
}
