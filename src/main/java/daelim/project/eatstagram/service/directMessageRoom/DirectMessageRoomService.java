package daelim.project.eatstagram.service.directMessageRoom;

import daelim.project.eatstagram.service.base.BaseService;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectMessageRoomService extends BaseService<String, DirectMessageRoom, DirectMessageRoomDTO, DirectMessageRoomRepository> {

    private final DirectMessageRoomMemberService directMessageRoomMemberService;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> add(List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList) {
        DirectMessageRoomDTO directMessageRoomDTO = DirectMessageRoomDTO.builder().build();
        if (directMessageRoomMemberDTOList.size() == 2) { // 2인 톡방
            DirectMessageRoomMemberDTO result = getRepository().findByUsernames(directMessageRoomMemberDTOList);
            if (result != null) {
                return new ResponseEntity<>(
                        "{\"response\": \"ok\", " +
                                "\"newYn\": \"N\", " +
                                "\"directMessageRoomId\": \"" + result.getDirectMessageRoomId() + "\", " +
                                "\"directMessageRoomType\": \"" + result.getDirectMessageRoomType() + "\"}"
                        , HttpStatus.OK);
            }
            directMessageRoomDTO.setDirectMessageRoomType("private");
        } else { // 단톡방(3인 이상)
            directMessageRoomDTO.setDirectMessageRoomType("public");
        }
        save(directMessageRoomDTO);
        for (DirectMessageRoomMemberDTO directMessageRoomMemberDTO : directMessageRoomMemberDTOList) {
            directMessageRoomMemberService.save(DirectMessageRoomMemberDTO.builder()
                    .username(directMessageRoomMemberDTO.getUsername())
                    .directMessageRoomId(directMessageRoomDTO.getDirectMessageRoomId())
                    .directMessageRoomType(directMessageRoomDTO.getDirectMessageRoomType())
                    .build());
        }
        return new ResponseEntity<>(
                "{\"response\": \"ok\", " +
                        "\"newYn\": \"Y\", " +
                        "\"directMessageRoomId\": \"" + directMessageRoomDTO.getDirectMessageRoomId() + "\", " +
                        "\"directMessageRoomType\": \"" + directMessageRoomDTO.getDirectMessageRoomType() + "\"}"
                , HttpStatus.OK);
    }
}
