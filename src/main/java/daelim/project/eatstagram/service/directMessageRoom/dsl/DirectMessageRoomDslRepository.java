package daelim.project.eatstagram.service.directMessageRoom.dsl;

import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomDTO;

import java.util.List;

public interface DirectMessageRoomDslRepository {

    List<DirectMessageRoomDTO> getList(List<String> directMessageRoomIdList);
}
