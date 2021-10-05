package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/directMessageRoomMember")
@RequiredArgsConstructor
public class DirectMessageRoomMemberController {

    private final DirectMessageRoomMemberService directMessageRoomMemberService;

    @RequestMapping("/updateConnectionYn")
    @ResponseBody
    public void updateConnectionYn(@ModelAttribute DirectMessageRoomMemberDTO dto) {
        directMessageRoomMemberService.updateConnectionYn(dto);
    }

    @RequestMapping("/unreadMessageTotalCountByUsername")
    @ResponseBody
    public long unreadMessageTotalCountByUsername(String username) {
        return directMessageRoomMemberService.unreadMessageTotalCountByUsername(username);
    }
}
