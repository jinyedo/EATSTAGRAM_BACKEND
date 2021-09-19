package daelim.project.eatstagram.service.directMessageRoomMember.dsl;

import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.member.MemberDTO;

import java.util.List;

public interface DirectMessageRoomMemberDslRepository {

    DirectMessageRoomMemberDTO findByUsernames(List<MemberDTO> memberList);
}