package daelim.project.eatstagram.service.directMessageRoom.dsl;

import daelim.project.eatstagram.service.directMessageRoom.QDirectMessageRoom;
import daelim.project.eatstagram.service.directMessageRoomMember.QDirectMessageRoomMemberEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static daelim.project.eatstagram.service.directMessageRoom.QDirectMessageRoom.*;
import static daelim.project.eatstagram.service.directMessageRoomMember.QDirectMessageRoomMemberEntity.*;

public class DirectMessageRoomDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageRoomDslRepository {

    public DirectMessageRoomDslRepositoryImpl() {
        super(QDirectMessageRoom.class);
    }

    public void a() {

    }
}
