package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomDTO;
import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/directMessageRoom")
@RequiredArgsConstructor
public class DirectMessageRoomController {

    private final DirectMessageRoomService directMessageRoomService;

    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<String> add(@ModelAttribute DirectMessageRoomDTO directMessageRoomDTO) {
       return directMessageRoomService.add(directMessageRoomDTO.getDirectMessageRoomMemberDTOList());
    }
}
