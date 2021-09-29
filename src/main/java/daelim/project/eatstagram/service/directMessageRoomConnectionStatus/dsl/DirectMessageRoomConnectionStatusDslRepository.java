package daelim.project.eatstagram.service.directMessageRoomConnectionStatus.dsl;

public interface DirectMessageRoomConnectionStatusDslRepository {

    public String getConnectionStatusYn(String directMessageRoomId, String username);
    public void updateConnectionYn(String directMessageRoomId, String username, String connectionYn);
}
