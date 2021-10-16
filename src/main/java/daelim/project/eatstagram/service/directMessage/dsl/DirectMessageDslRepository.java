package daelim.project.eatstagram.service.directMessage.dsl;

import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DirectMessageDslRepository {

    Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId, String username);
    void deleteByDirectMessageRoomIds(List<String> directMessageRoomIds);
}
