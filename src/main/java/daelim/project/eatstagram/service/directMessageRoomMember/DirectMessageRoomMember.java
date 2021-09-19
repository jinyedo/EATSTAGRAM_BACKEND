package daelim.project.eatstagram.service.directMessageRoomMember;

import daelim.project.eatstagram.service.base.BaseEntity;
import daelim.project.eatstagram.service.base.DTOKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class DirectMessageRoomMember extends BaseEntity {

    @Id @DTOKey("DMRM")
    protected String directMessageRoomMemberId;
    protected String username;
    protected String directMessageRoomId;
    protected String directMessageRoomType; // 개인톡인지 단톡방인지
}
