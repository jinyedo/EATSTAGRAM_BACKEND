package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
import daelim.project.eatstagram.service.directMessage.DirectMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/directMessage")
@RequiredArgsConstructor
public class DirectMessageController {

    private final DirectMessageService directMessageService;

    @RequestMapping("/getPagingList")
    @ResponseBody
    public Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId) {
        return directMessageService.getDirectMessagePagingList(pageable, directMessageRoomId);
    }
}
