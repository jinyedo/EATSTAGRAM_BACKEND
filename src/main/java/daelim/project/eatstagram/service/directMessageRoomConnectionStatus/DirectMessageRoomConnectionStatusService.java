package daelim.project.eatstagram.service.directMessageRoomConnectionStatus;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageRoomConnectionStatusService
        extends BaseService<String, DirectMessageRoomConnectionStatusEntity, DirectMessageRoomConnectionStatusDTO, DirectMessageRoomConnectionStatusRepository> {

    public String getConnectionStatusYn(String directMessageRoomId, String username) {
        return getRepository().getConnectionStatusYn(directMessageRoomId, username);
    }

    public void updateConnectionYn(DirectMessageRoomConnectionStatusDTO dto) {
        getRepository().updateConnectionYn(dto.getDirectMessageRoomId(), dto.getUsername(), dto.getConnectionYn());
    }
}
