package daelim.project.eatstagram.service.directMessageRoomReadStatus;

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
public class DirectMessageRoomReadStatus extends BaseEntity {

    @Id @DTOKey("DMRRS")
    protected String directMessageRoomReadStatusId;
    protected String readYn;
    protected String username;
    protected String directMessageRoomId;
}
