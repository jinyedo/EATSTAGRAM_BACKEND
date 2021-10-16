package daelim.project.eatstagram.service.directMessageRoom;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectMessageRoomService extends BaseService<String, DirectMessageRoom, DirectMessageRoomDTO, DirectMessageRoomRepository> {

    public void deleteByDirectMessageRoomIds(List<String> directMessageRoomIds) {
        getRepository().deleteByDirectMessageRoomIds(directMessageRoomIds);
    }
}
