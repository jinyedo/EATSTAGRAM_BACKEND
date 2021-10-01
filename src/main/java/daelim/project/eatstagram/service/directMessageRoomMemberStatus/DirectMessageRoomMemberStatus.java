package daelim.project.eatstagram.service.directMessageRoomMemberStatus;

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
public class DirectMessageRoomMemberStatus extends BaseEntity {

    @Id
    @DTOKey("DMRMS")
    protected String directMessageRoomMemberStatusId;
    protected String connectionYn;
    protected String alertYn;
    protected String username;
    protected String directMessageRoomId;
}
