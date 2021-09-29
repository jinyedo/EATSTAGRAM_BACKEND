package daelim.project.eatstagram.service.directMessageRoomMemberStatus;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageRoomMemberStatusService
        extends BaseService<String, DirectMessageRoomMemberStatusEntity, DirectMessageRoomMemberStatusDTO, DirectMessageRoomMemberStatusRepository> {

    public String getAlertYn(String directMessageRoomId, String username) {
        return getRepository().getAlertYn(directMessageRoomId, username);
    }

    public String getConnectionYn(String directMessageRoomId, String username) {
        return getRepository().getConnectionYn(directMessageRoomId, username);
    }

    public void updateConnectionYn(DirectMessageRoomMemberStatusDTO dto) {
        getRepository().updateConnectionYn(dto.getDirectMessageRoomId(), dto.getUsername(), dto.getConnectionYn());
        if (dto.getConnectionYn().equals("Y")) {
            updateReadYn(dto.getDirectMessageRoomId(), dto.getUsername(), "Y");
            updateAlertYn(dto.getDirectMessageRoomId(), dto.getUsername(), "N");
        }
    }

    public void updateReadYn(String directMessageRoomId, String username, String readYn) {
        getRepository().updateReadYn(directMessageRoomId, username, readYn);
    }

    public void updateAlertYn(String directMessageRoomId, String username, String alertYn) {
        getRepository().updateAlertYn(directMessageRoomId, username, alertYn);
    }
}
