package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.directMessageRoomMemberStatus.DirectMessageRoomMemberStatusDTO;
import daelim.project.eatstagram.service.directMessageRoomMemberStatus.DirectMessageRoomMemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/directMessageRoomMemberStatus")
@RequiredArgsConstructor
public class DirectMessageRoomMemberStatusController {

    private final DirectMessageRoomMemberStatusService directMessageRoomMemberStatusService;

    @RequestMapping("/updateConnectionYn")
    @ResponseBody
    public void updateConnectionYn(@ModelAttribute DirectMessageRoomMemberStatusDTO dto) {
        directMessageRoomMemberStatusService.updateConnectionYn(dto);
    }

    @RequestMapping("/unreadMessageTotalCountByUsername")
    @ResponseBody
    public long unreadMessageTotalCountByUsername(String username) {
        return directMessageRoomMemberStatusService.unreadMessageTotalCountByUsername(username);
    }
}