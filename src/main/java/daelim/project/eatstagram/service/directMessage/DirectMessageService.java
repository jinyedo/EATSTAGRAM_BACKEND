package daelim.project.eatstagram.service.directMessage;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageService extends BaseService<String, DirectMessageEntity, DirectMessageDTO, DirectMessageRepository> {

    public Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId) {
        return getRepository().getDirectMessagePagingList(pageable, directMessageRoomId);
    }
}
