package daelim.project.eatstagram.service.directMessageRoom;

import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter @Setter
@ToString
public class DirectMessageRoomDTO extends DirectMessageRoom {

    private String alertYn;
    private LocalDateTime maxRegDate;
    @Builder.Default
    private List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList = new ArrayList<>();
}
