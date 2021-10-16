package daelim.project.eatstagram.service.biz;

import daelim.project.eatstagram.service.content.ContentService;
import daelim.project.eatstagram.service.contentCategory.ContentCategoryService;
import daelim.project.eatstagram.service.contentFile.ContentFileService;
import daelim.project.eatstagram.service.contentHashTag.ContentHashtagService;
import daelim.project.eatstagram.service.contentLike.ContentLikeService;
import daelim.project.eatstagram.service.contentReply.ContentReplyService;
import daelim.project.eatstagram.service.contentSaved.ContentSavedService;
import daelim.project.eatstagram.service.directMessage.DirectMessageService;
import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomService;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberService;
import daelim.project.eatstagram.service.follow.FollowService;
import daelim.project.eatstagram.service.member.MemberDTO;
import daelim.project.eatstagram.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberBizService {

    private final MemberService memberService;
    private final FollowService followService;
    private final ContentService contentService;
    private final ContentCategoryService contentCategoryService;
    private final ContentHashtagService contentHashtagService;
    private final ContentFileService contentFileService;
    private final ContentSavedService contentSavedService;
    private final ContentLikeService contentLikeService;
    private final ContentReplyService contentReplyService;
    private final DirectMessageService directMessageService;
    private final DirectMessageRoomService directMessageRoomService;
    private final DirectMessageRoomMemberService directMessageRoomMemberService;

    public void deleteAccount(String username) {
        List<String> contentIds = contentService.getContentIdsByUsername(username);
        contentCategoryService.deleteByContentIds(contentIds);
        contentHashtagService.deleteByContentIds(contentIds);
        contentFileService.deleteByContentIds(contentIds);
        contentSavedService.deleteByContentIds(contentIds);
        contentLikeService.deleteByContentIds(contentIds);
        contentReplyService.deleteByContentIds(contentIds);

        List<String> directMessageRoomIds = directMessageRoomMemberService.getDirectMessageRoomIdsByUsername(username);
        directMessageService.deleteByDirectMessageRoomIds(directMessageRoomIds);
        directMessageRoomMemberService.deleteByDirectMessageRoomIds(directMessageRoomIds);
        directMessageRoomService.deleteByDirectMessageRoomIds(directMessageRoomIds);

        followService.deleteByUsername(username);
        
        memberService.getRepository().deleteById(username);
    }

    // 랭킹별 사용자 리스트 가져오기
    public Page<MemberDTO> getRankingPagingList(Pageable pageable, String username) {
        Page<MemberDTO> rankingList = memberService.getRepository().getRankingPagingList(pageable);
        return getDataRelatedToMember(rankingList, username);
    }

    // 검색시 사용자 페이징 리스트 가져오기
    public Page<MemberDTO> getSearchPagingList(Pageable pageable, String username, String condition) {
        Page<MemberDTO> searchPagingList = memberService.getRepository().getSearchPagingList(pageable, username, condition);
        return getDataRelatedToMember(searchPagingList, username);
    }

    private Page<MemberDTO> getDataRelatedToMember(Page<MemberDTO> memberList, String username) {
        for (MemberDTO memberDTO : memberList) {
            String followYn = followService.followCheck(username, memberDTO.getUsername()) == null ? "N" : "Y";
            String followerYn = followService.followerCheck(username, memberDTO.getUsername()) == null ? "N" : "Y";
            memberDTO.setFollowYn(followYn);
            memberDTO.setFollowerYn(followerYn);
        }
        return memberList;
    }
}
