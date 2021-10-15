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

    public List<DirectMessageRoomDTO> getList(String username) {
        // 해당 회원이 참여중인 채팅방 아이디들을 담을 리스트
        List<String> directMessageRoomIdList = new ArrayList<>();

        // 해당 회원이 참여해 있는 채팅방 리스트 가져오기
        List<DirectMessageRoomMemberDTO> directMessageRoomMemberList
                = directMessageRoomMemberService.getRepository().findByUsername(username);

        // 해당 회원이 참여해 있는 채팅방 개수만큼 반복하며, 반복 요소의 채팅방 아이디를 배열에 추가
        for (DirectMessageRoomMemberDTO dto : directMessageRoomMemberList) {
            directMessageRoomIdList.add(dto.getDirectMessageRoomId());
        }

        // 배열에 담은 채팅방 아이디들로 해당 회원이 참여해 있는 채팅방 리스트 가져오기 (67번째 줄과 차이점은 가장 최신 댓글이 달린 순을로 정렬을 해서 가져온다.)
        List<DirectMessageRoomDTO> directMessageRoomDTOList = directMessageRoomService.getRepository().getList(directMessageRoomIdList);
        for (DirectMessageRoomDTO dto : directMessageRoomDTOList) {
            // 해당 회원이 참여해 있는 채팅방에 안읽은 메시지가 있어 알림이 왔는지
            String alertYn = directMessageRoomMemberService.getAlertYn(dto.getDirectMessageRoomId(), username);
            dto.setAlertYn(alertYn);
            dto.setDirectMessageRoomMemberDTOList(
                    directMessageRoomMemberService.getRepository().findByDirectMessageRoomIdAndNotUsernameJoinMember(
                            dto.getDirectMessageRoomId(),
                            username
                    ));
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
                returnMap.put("type", "createDirectMessageRoom");
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
                    .connectionYn("N")
                    .alertYn("N")
                    .inYn("Y")
                    .build());
        }

        returnMap.put("response", "ok");
        returnMap.put("newYn", "Y");
        returnMap.put("directMessageRoomId", directMessageRoomDTO.getDirectMessageRoomId());
        returnMap.put("directMessageRoomType", directMessageRoomDTO.getDirectMessageRoomType());
        returnMap.put("userList", getMemberListByDirectMessageRoomId(directMessageRoomDTO.getDirectMessageRoomId()));
        returnMap.put("type", "createDirectMessageRoom");
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
            userinfo.put("profileImgName", dto.getProfileImgName());
            userList.add(userinfo);
        }
        return userList;
    }
}
