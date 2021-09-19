package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomService;
import daelim.project.eatstagram.service.member.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/directMessageRoom")
@RequiredArgsConstructor
@Slf4j
public class DirectMessageRoomController {

    private final DirectMessageRoomService directMessageRoomService;

    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<String> add(@RequestBody ArrayList<MemberDTO> memberList) {
       return directMessageRoomService.add(memberList);
    }
}
