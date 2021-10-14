package daelim.project.eatstagram.service.biz;

import daelim.project.eatstagram.service.member.MemberDTO;
import daelim.project.eatstagram.service.member.MemberService;
import daelim.project.eatstagram.service.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberBizService {

    private final MemberService memberService;
    private final SubscriptionService subscriptionService;

    // 랭킹별 사용자 리스트 가져오기
    public Page<MemberDTO> getRankingList(Pageable pageable, String username) {
        Page<MemberDTO> rankingList = memberService.getRepository().getRankingList(pageable);
        for (MemberDTO memberDTO : rankingList) {
            String subscriptionYn = subscriptionService. getRepository().findByUsernameAndSubscriber(username, memberDTO.getUsername()) == null ? "N" : "Y";
            memberDTO.setSubscriptionYn(subscriptionYn);
        }
        return rankingList;
    }

    // 검색시 사용자 페이징 리스트 가져오기
    public Page<MemberDTO> getSearchPagingList(Pageable pageable, String username, String condition) {
        Page<MemberDTO> searchPagingList = memberService.getRepository().getSearchPagingList(pageable, username, condition);
        for (MemberDTO memberDTO : searchPagingList) {
            String subscriptionYn = subscriptionService. getRepository().findByUsernameAndSubscriber(username, memberDTO.getUsername()) == null ? "N" : "Y";
            memberDTO.setSubscriptionYn(subscriptionYn);
        }
        return searchPagingList;
    }
}
