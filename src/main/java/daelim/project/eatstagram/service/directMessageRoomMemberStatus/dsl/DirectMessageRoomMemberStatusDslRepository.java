package daelim.project.eatstagram.service.directMessageRoomMemberStatus.dsl;

public interface DirectMessageRoomMemberStatusDslRepository {

    String getConnectionYn(String directMessageRoomId, String username);
    String getAlertYn(String directMessageRoomId, String username);
    void updateConnectionYn(String directMessageRoomId, String username, String connectionYn);
    void updateReadYn(String directMessageRoomId, String username, String readYn);
    void updateAlertYn(String directMessageRoomId, String username, String alertYn);
    long unreadMessageTotalCountByUsername(String username);
}
