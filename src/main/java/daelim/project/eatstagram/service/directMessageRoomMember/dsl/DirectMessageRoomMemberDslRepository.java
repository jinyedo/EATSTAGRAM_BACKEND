package daelim.project.eatstagram.service.directMessageRoomMember.dsl;

import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;

import java.util.List;

public interface DirectMessageRoomMemberDslRepository {

    DirectMessageRoomMemberDTO findByUsernames(List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList);
}