package daelim.project.eatstagram.service.directMessage;

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
public class DirectMessage extends BaseEntity {

    @Id @DTOKey("DM")
    protected String directMessageId;
    protected String directMessageType;
    protected String directMessage;
    protected String directMessageRoomId;
    protected String username;
}
