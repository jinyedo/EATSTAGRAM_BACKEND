package daelim.project.eatstagram.service.directMessageRoomConnectionStatus.dsl;

import daelim.project.eatstagram.service.directMessageRoomConnectionStatus.QDirectMessageRoomConnectionStatusEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static daelim.project.eatstagram.service.directMessageRoomConnectionStatus.QDirectMessageRoomConnectionStatusEntity.*;

public class DirectMessageRoomConnectionStatusDslRepositoryImpl
        extends QuerydslRepositorySupport implements DirectMessageRoomConnectionStatusDslRepository {

    public DirectMessageRoomConnectionStatusDslRepositoryImpl() {
        super(QDirectMessageRoomConnectionStatusEntity.class);
    }

    @Override
    public String getConnectionStatusYn(String directMessageRoomId, String username) {
        return from(directMessageRoomConnectionStatusEntity)
                .where(
                        directMessageRoomConnectionStatusEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomConnectionStatusEntity.username.eq(username)
                )
                .select(
                        directMessageRoomConnectionStatusEntity.connectionYn
                )
                .fetchOne();
    }
}
