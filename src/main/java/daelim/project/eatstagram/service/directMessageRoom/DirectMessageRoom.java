package daelim.project.eatstagram.service.directMessageRoom;

import daelim.project.eatstagram.service.base.BaseEntity;
import daelim.project.eatstagram.service.base.DTOKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class DirectMessageRoom extends BaseEntity {

    @Id @DTOKey("DMR")
    private String directMessageRoomId;
    private String directMessageRoomType; // 개인톡인지 단톡방인지
}
