package daelim.project.eatstagram.service.directMessage.dsl;

import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DirectMessageDslRepository {

    Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId);
}
