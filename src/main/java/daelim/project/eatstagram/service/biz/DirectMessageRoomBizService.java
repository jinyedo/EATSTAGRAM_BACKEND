package daelim.project.eatstagram.service.biz;

import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomDTO;
import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomService;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberService;
import daelim.project.eatstagram.service.directMessageRoomMemberStatus.DirectMessageRoomMemberStatusService;
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
    private final DirectMessageRoomMemberStatusService directMessageRoomMemberStatusService;

    public List<DirectMessageRoomDTO> getList(String username){
        List<DirectMessageRoomDTO> directMessageRoomDTOList = new ArrayList<>();
        // 해당 회원이 참여해 있는 채팅방 리스트 가져오기
        List<DirectMessageRoomMemberDTO> findByUsernameResult
                = directMessageRoomMemberService.getRepository().findByUsername(username);
        // 해당 회원이 참여해 있는 채팅방 개수만큼 반복
        for (DirectMessageRoomMemberDTO findByUsernameResultItem : findByUsernameResult) {

            // 해당 회원이 참여해 있는 채팅방에 안읽은 메시지가 있어 알림이 왔는지
            String alertYn = directMessageRoomMemberStatusService.getAlertYn(
                    findByUsernameResultItem.getDirectMessageRoomId(),
                    username
            );

            // 회원이 가지고 있는 채팅방 정보로 채팅 DTO 만들기
            DirectMessageRoomDTO directMessageRoomDTO = DirectMessageRoomDTO.builder()
                    .directMessageRoomId(findByUsernameResultItem.getDirectMessageRoomId())
                    .directMessageRoomType(findByUsernameResultItem.getDirectMessageRoomType())
                    .alertYn(alertYn)
                    .build();

            // 채팅방 아이디로 채팅방에 참여해 있는 회원 리스트 가져오기
            List<DirectMessageRoomMemberDTO> findByDirectMessageRoomIdJoinMemberResult
                    = directMessageRoomMemberService.getRepository().findByDirectMessageRoomIdJoinMember(findByUsernameResultItem.getDirectMessageRoomId());
            // 채팅방에 속해있는 회원 수 만큼 반복
            for (DirectMessageRoomMemberDTO findByDirectMessageRoomIdJoinMemberResultItem : findByDirectMessageRoomIdJoinMemberResult) {
                // 채팅방 리스트를 그려줄 때 자신의 정보는 필요 없으므로 반복 요소가 자신일때는 건너뛴다.
                if (findByDirectMessageRoomIdJoinMemberResultItem.getUsername().equals(username)) continue;
                // 채팅방 DTO 에 회원 리스트 추가
                directMessageRoomDTO.addDirectMessageRoomMemberDTOList(findByDirectMessageRoomIdJoinMemberResultItem);
            }
            // 반환해줄 채팅방 리스트에 채팅방 추가
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
