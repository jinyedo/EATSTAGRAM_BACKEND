package daelim.project.eatstagram.service.biz;

import daelim.project.eatstagram.service.member.MemberDTO;
import daelim.project.eatstagram.service.member.MemberService;
import daelim.project.eatstagram.service.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberBizService {

    private final MemberService memberService;
    private final SubscriptionService subscriptionService;

    // 검색시 사용자 페이징 리스트 가져오기
    public Page<MemberDTO> getSearchPagingList(Pageable pageable, String username, String condition) {
        Page<MemberDTO> searchPagingList = memberService.getRepository().getSearchPagingList(pageable, username, condition);
        for (MemberDTO memberDTO : searchPagingList) {
            String subscriptionYn = subscriptionService. getRepository().findByUsernameAndSubscriber(username, memberDTO.getUsername()) == null ? "N" : "Y";
            memberDTO.setSubscriptionYn(subscriptionYn);
        }
        return searchPagingList;
    }

    // 구독자 수가 상위 10프로인 사용자 리스트 가져오기
    public List<MemberDTO> getTopTenList(String username) {
        List<MemberDTO> topTenList = memberService.getRepository().getTopTenList();
        for (MemberDTO memberDTO : topTenList) {
            String subscriptionYn = subscriptionService. getRepository().findByUsernameAndSubscriber(username, memberDTO.getUsername()) == null ? "N" : "Y";
            memberDTO.setSubscriptionYn(subscriptionYn);
        }
        return topTenList;
    }
}
