package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.directMessageRoomConnectionStatus.DirectMessageRoomConnectionStatusDTO;
import daelim.project.eatstagram.service.directMessageRoomConnectionStatus.DirectMessageRoomConnectionStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/directMessageRoomConnectionStatus")
@RequiredArgsConstructor
public class DirectMessageRoomConnectionStatusController {

    private final DirectMessageRoomConnectionStatusService directMessageRoomConnectionStatusService;

    @RequestMapping("/updateConnectionYn")
    @ResponseBody
    public void updateConnectionYn(@ModelAttribute DirectMessageRoomConnectionStatusDTO dto) {
        directMessageRoomConnectionStatusService.updateConnectionYn(dto);
    }
}
