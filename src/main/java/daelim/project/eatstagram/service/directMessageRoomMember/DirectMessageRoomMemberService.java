package daelim.project.eatstagram.service.directMessageRoomMember;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageRoomMemberService
        extends BaseService<String, DirectMessageRoomMemberEntity, DirectMessageRoomMemberDTO, DirectMessageRoomMemberRepository> {

    public String getConnectionYn(String directMessageRoomId, String username) {
        return getRepository().getConnectionYn(directMessageRoomId, username);
    }

    public String getAlertYn(String directMessageRoomId, String username) {
        return getRepository().getAlertYn(directMessageRoomId, username);
    }

    public String getInYn(String directMessageRoomId, String username) {
        return getRepository().getInYn(directMessageRoomId, username);
    }

    public void updateConnectionYn(DirectMessageRoomMemberDTO dto) {
        getRepository().updateConnectionYn(dto.getDirectMessageRoomId(), dto.getUsername(), dto.getConnectionYn());
        if (dto.getConnectionYn().equals("N")) {
            updateAlertYn(dto.getDirectMessageRoomId(), dto.getUsername(), "N");
        }
    }

    public void updateAllConnectionYn(String username, String connectionYn) {
        getRepository().updateAllConnectionYn(username, connectionYn);
    }

    public void updateAlertYn(String directMessageRoomId, String username, String alertYn) {
        getRepository().updateAlertYn(directMessageRoomId, username, alertYn);
    }

    public void updateInYn(String directMessageRoomId, String username, String inYn) {
        getRepository().updateInYn(directMessageRoomId, username, inYn);
    }

    public long unreadMessageTotalCountByUsername(String username) {
        return getRepository().unreadMessageTotalCountByUsername(username);
    }
}
