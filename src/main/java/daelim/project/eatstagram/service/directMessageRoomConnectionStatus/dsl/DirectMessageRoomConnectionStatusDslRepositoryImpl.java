package daelim.project.eatstagram.service.directMessageRoomConnectionStatus.dsl;

import daelim.project.eatstagram.service.directMessageRoomConnectionStatus.QDirectMessageRoomConnectionStatusEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import static daelim.project.eatstagram.service.directMessageRoomConnectionStatus.QDirectMessageRoomConnectionStatusEntity.directMessageRoomConnectionStatusEntity;

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

    @Override
    @Modifying @Transactional(rollbackFor = Exception.class)
    public void updateConnectionYn(String directMessageRoomId, String username, String connectionYn) {
        update(directMessageRoomConnectionStatusEntity)
                .set(directMessageRoomConnectionStatusEntity.connectionYn, connectionYn)
                .where(
                        directMessageRoomConnectionStatusEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomConnectionStatusEntity.username.eq(username)
                )
                .execute();
    }
}
