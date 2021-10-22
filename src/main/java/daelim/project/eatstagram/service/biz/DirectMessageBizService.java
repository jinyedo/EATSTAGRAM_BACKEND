package daelim.project.eatstagram.service.biz;

import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
import daelim.project.eatstagram.service.directMessage.DirectMessageService;
import daelim.project.eatstagram.service.member.MemberDTO;
import daelim.project.eatstagram.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectMessageBizService {

    private final DirectMessageService directMessageService;
    private final MemberService memberService;

    public Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId, String username) {
        Page<DirectMessageDTO> directMessagePagingList = directMessageService.getRepository().getDirectMessagePagingList(pageable, directMessageRoomId, username);
        for (DirectMessageDTO dto : directMessagePagingList) {
            if (dto.getDirectMessageType().equals("share")) {
                try {
                    JSONObject obj = (JSONObject) new JSONParser().parse(dto.getDirectMessage());
                    String contentUsername = (String) obj.get("contentUsername");
                    MemberDTO memberDTO = memberService.getMemberInfo(contentUsername);
                    obj.put("profileImgName", memberDTO.getProfileImgName());
                    dto.setDirectMessage(obj.toJSONString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return directMessagePagingList;
    }
}
