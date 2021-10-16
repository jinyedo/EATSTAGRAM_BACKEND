package daelim.project.eatstagram.service.directMessageRoomMember.dsl;

import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;

import java.util.List;

public interface DirectMessageRoomMemberDslRepository {

    List<DirectMessageRoomMemberDTO> findByDirectMessageRoomIdJoinMember(String directMessageRoomId);
    List<DirectMessageRoomMemberDTO> findByDirectMessageRoomId(String directMessageRoomId, String username);
    List<DirectMessageRoomMemberDTO> findByDirectMessageRoomIdAndNotUsernameJoinMember(String directMessageRoomId, String username);
    List<DirectMessageRoomMemberDTO> findByUsername(String username);
    List<String> getDirectMessageRoomIdsByUsername(String username);
    DirectMessageRoomMemberDTO findByUsernames(List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList);
    String getConnectionYn(String directMessageRoomId, String username);
    String getAlertYn(String directMessageRoomId, String username);
    String getInYn(String directMessageRoomId, String username);
    void updateConnectionYn(String directMessageRoomId, String username, String connectionYn);
    void updateAllConnectionYn(String username, String connectionYn);
    void updateAlertYn(String directMessageRoomId, String username, String alertYn);
    void updateInYn(String directMessageRoomId, String username, String inYn);
    void updateConditionDate(String directMessageRoomId, String username);
    long unreadMessageTotalCountByUsername(String username);
    void deleteByDirectMessageRoomIds(List<String> directMessageRoomIds);
}