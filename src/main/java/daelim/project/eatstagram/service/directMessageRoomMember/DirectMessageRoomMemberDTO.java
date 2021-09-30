package daelim.project.eatstagram.service.directMessageRoomMember;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter @Setter
@ToString
public class DirectMessageRoomMemberDTO extends DirectMessageRoomMember {

    private String username;
    private String name;
    private String nickname;
    private long count;

    private String readYn;
}
