package daelim.project.eatstagram.service.directMessageRoomMember.dsl;

import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;

import java.util.List;

public interface DirectMessageRoomMemberDslRepository {

    List<DirectMessageRoomMemberDTO> findByDirectMessageRoomIdJoinMember(String directMessageRoomId);
    List<DirectMessageRoomMemberDTO> findByUsername(String username);
    DirectMessageRoomMemberDTO findByUsernames(List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList);
}