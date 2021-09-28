package daelim.project.eatstagram.service.directMessageRoomReadStatus;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageRoomReadStatusService
        extends BaseService<String, DirectMessageRoomReadStatusEntity, DirectMessageRoomReadStatusDTO, DirectMessageRoomReadStatusRepository> {

    public void updateReadYn(String directMessageRoomId, String username, String readYn) {
        getRepository().updateReadYn(directMessageRoomId, username, readYn);
    }
}
