package daelim.project.eatstagram.service.biz;

import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomDTO;
import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomService;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectMessageRoomBizService {

    private final DirectMessageRoomService directMessageRoomService;
    private final DirectMessageRoomMemberService directMessageRoomMemberService;

    public List<DirectMessageRoomDTO> getList(String username){
        List<DirectMessageRoomDTO> directMessageRoomDTOList = new ArrayList<>();
        List<DirectMessageRoomMemberDTO> findByUsernameResult
                = directMessageRoomMemberService.getRepository().findByUsername(username);
        for (DirectMessageRoomMemberDTO findByUsernameResultItem : findByUsernameResult) {
            DirectMessageRoomDTO directMessageRoomDTO = DirectMessageRoomDTO.builder()
                    .directMessageRoomId(findByUsernameResultItem.getDirectMessageRoomId())
                    .directMessageRoomType(findByUsernameResultItem.getDirectMessageRoomType())
                    .build();
            List<DirectMessageRoomMemberDTO> findByDirectMessageRoomIdJoinMemberResult
                    = directMessageRoomMemberService.getRepository().findByDirectMessageRoomIdJoinMember(findByUsernameResultItem.getDirectMessageRoomId());
            for (DirectMessageRoomMemberDTO findByDirectMessageRoomIdJoinMemberResultItem : findByDirectMessageRoomIdJoinMemberResult) {
                if (findByDirectMessageRoomIdJoinMemberResultItem.getUsername().equals(username)) continue;
                directMessageRoomDTO.addDirectMessageRoomMemberDTOList(findByDirectMessageRoomIdJoinMemberResultItem);
            }
            directMessageRoomDTOList.add(directMessageRoomDTO);
        }
        return directMessageRoomDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> add(List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList) {
        DirectMessageRoomDTO directMessageRoomDTO = DirectMessageRoomDTO.builder().build();
        if (directMessageRoomMemberDTOList.size() > 2) {
            return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.BAD_REQUEST);
        }else if (directMessageRoomMemberDTOList.size() == 2) { // 2인 톡방
            DirectMessageRoomMemberDTO result
                    = directMessageRoomMemberService.getRepository().findByUsernames(directMessageRoomMemberDTOList);
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
        directMessageRoomService.save(directMessageRoomDTO);
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
