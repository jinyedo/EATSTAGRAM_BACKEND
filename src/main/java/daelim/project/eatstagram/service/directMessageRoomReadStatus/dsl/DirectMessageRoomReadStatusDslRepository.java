package daelim.project.eatstagram.service.directMessageRoomReadStatus.dsl;

public interface DirectMessageRoomReadStatusDslRepository {

    void updateReadYn(String directMessageRoomId, String username, String readYn);
}
