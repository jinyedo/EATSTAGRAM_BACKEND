package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.biz.DirectMessageRoomBizService;
import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/directMessageRoom")
@RequiredArgsConstructor
public class DirectMessageRoomController {

    private final DirectMessageRoomMemberService directMessageRoomMemberService;
    private final DirectMessageRoomBizService directMessageRoomBizService;

    // 채팅방 목록 리스트
    @RequestMapping("/getList")
    @ResponseBody
    public List<DirectMessageRoomDTO> getList(String username) {
        return directMessageRoomBizService.getList(username);
    }

    // 채팅방 나가기
    @RequestMapping("/out")
    public void out(String directMessageRoomId, String username) {
        directMessageRoomMemberService.updateInYn(directMessageRoomId, username, "N");
    }
}
