package daelim.project.eatstagram.service.biz;

import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomDTO;
import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomService;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> add(List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList) {
        Map<String, Object> returnMap = new HashMap<>();
        DirectMessageRoomDTO directMessageRoomDTO = DirectMessageRoomDTO.builder().build();

        if (directMessageRoomMemberDTOList.size() < 2) {
            returnMap.put("response", "error");
            return returnMap;

        }else if (directMessageRoomMemberDTOList.size() == 2) { // 2인 톡방
            DirectMessageRoomMemberDTO result
                    = directMessageRoomMemberService.getRepository().findByUsernames(directMessageRoomMemberDTOList);
            if (result != null) {
                returnMap.put("response", "ok");
                returnMap.put("newYn", "N");
                returnMap.put("directMessageRoomId", result.getDirectMessageRoomId());
                returnMap.put("directMessageRoomType", result.getDirectMessageRoomType());
                returnMap.put("userList", getMemberListByDirectMessageRoomId(result.getDirectMessageRoomId()));
                return returnMap;
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

        returnMap.put("response", "ok");
        returnMap.put("newYn", "Y");
        returnMap.put("directMessageRoomId", directMessageRoomDTO.getDirectMessageRoomId());
        returnMap.put("directMessageRoomType", directMessageRoomDTO.getDirectMessageRoomType());
        returnMap.put("userList", getMemberListByDirectMessageRoomId(directMessageRoomDTO.getDirectMessageRoomId()));
        return returnMap;
    }

    private List<Map<String, String>> getMemberListByDirectMessageRoomId(String directMessageRoomId) {
        List<Map<String, String>> userList = new ArrayList<>();
        List<DirectMessageRoomMemberDTO> dtoList = directMessageRoomMemberService.getRepository().findByDirectMessageRoomIdJoinMember(directMessageRoomId);
        for (DirectMessageRoomMemberDTO dto : dtoList) {
            Map<String, String> userinfo = new HashMap<>();
            userinfo.put("username", dto.getUsername());
            userinfo.put("name", dto.getName());
            userinfo.put("nickname", dto.getNickname());
            userList.add(userinfo);
        }
        return userList;
    }
}
