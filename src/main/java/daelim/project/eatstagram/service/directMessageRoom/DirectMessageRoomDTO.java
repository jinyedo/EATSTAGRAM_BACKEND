package daelim.project.eatstagram.service.directMessageRoom;

import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter @Setter
@ToString
public class DirectMessageRoomDTO extends DirectMessageRoom {

    private String alertYn;

    @Builder.Default
    private List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList = new ArrayList<>();

    public void addDirectMessageRoomMemberDTOList(DirectMessageRoomMemberDTO directMessageRoomMemberDTO) {
        this.directMessageRoomMemberDTOList.add(directMessageRoomMemberDTO);
    }
}
