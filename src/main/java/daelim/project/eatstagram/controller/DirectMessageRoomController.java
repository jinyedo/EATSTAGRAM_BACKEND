package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.biz.DirectMessageRoomBizService;
import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/directMessageRoom")
@RequiredArgsConstructor
public class DirectMessageRoomController {

    private final DirectMessageRoomBizService directMessageRoomBizService;

    @RequestMapping("/getList")
    @ResponseBody
    public List<DirectMessageRoomDTO> getList(String username) {
        return directMessageRoomBizService.getList(username);
    }
}
