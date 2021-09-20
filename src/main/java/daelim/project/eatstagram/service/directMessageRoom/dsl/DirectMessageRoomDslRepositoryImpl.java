package daelim.project.eatstagram.service.directMessageRoom.dsl;

import daelim.project.eatstagram.service.directMessageRoom.QDirectMessageRoom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class DirectMessageRoomDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageRoomDslRepository {

    public DirectMessageRoomDslRepositoryImpl() {
        super(QDirectMessageRoom.class);
    }
}
