package daelim.project.eatstagram.service.directMessageRoomReadStatus;

import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoom;
import daelim.project.eatstagram.service.directMessageRoomConnectionStatus.DirectMessageRoomConnectionStatus;
import daelim.project.eatstagram.service.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "direct_message_room_read_status")
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class DirectMessageRoomReadStatusEntity extends DirectMessageRoomReadStatus {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directMessageRoomId", insertable = false, updatable = false)
    private DirectMessageRoom directMessageRoom;
}
