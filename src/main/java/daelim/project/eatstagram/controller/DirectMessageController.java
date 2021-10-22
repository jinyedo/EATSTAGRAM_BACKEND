package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.biz.DirectMessageBizService;
import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
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

    private final DirectMessageBizService directMessageBizService;

    // 채팅 페이징 리스트
    @RequestMapping("/getPagingList")
    @ResponseBody
    public Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId, String username) {
        return directMessageBizService.getDirectMessagePagingList(pageable, directMessageRoomId, username);
    }
}
