package daelim.project.eatstagram.service.directMessageRoomMemberStatus.dsl;

import daelim.project.eatstagram.service.directMessageRoomMemberStatus.QDirectMessageRoomMemberStatusEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import static daelim.project.eatstagram.service.directMessageRoomMemberStatus.QDirectMessageRoomMemberStatusEntity.directMessageRoomMemberStatusEntity;

public class DirectMessageRoomMemberStatusDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageRoomMemberStatusDslRepository {

    public DirectMessageRoomMemberStatusDslRepositoryImpl() {
        super(QDirectMessageRoomMemberStatusEntity.class);
    }

    @Override
    public String getConnectionYn(String directMessageRoomId, String username) {
        return from(directMessageRoomMemberStatusEntity)
                .where(
                        directMessageRoomMemberStatusEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberStatusEntity.username.eq(username)
                )
                .select(
                        directMessageRoomMemberStatusEntity.connectionYn
                )
                .fetchOne();
    }

    @Override
    public String getAlertYn(String directMessageRoomId, String username) {
        return from(directMessageRoomMemberStatusEntity)
                .where(
                        directMessageRoomMemberStatusEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberStatusEntity.username.eq(username)
                )
                .select(
                        directMessageRoomMemberStatusEntity.alertYn
                )
                .fetchOne();
    }

    @Override
    @Transactional(rollbackFor = Exception.class) @Modifying
    public void updateConnectionYn(String directMessageRoomId, String username, String connectionYn) {
        update(directMessageRoomMemberStatusEntity)
                .set(directMessageRoomMemberStatusEntity.connectionYn, connectionYn)
                .where(
                        directMessageRoomMemberStatusEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberStatusEntity.username.eq(username)
                )
                .execute();
    }

    @Override
    @Transactional(rollbackFor = Exception.class) @Modifying
    public void updateAllConnectionYn(String username, String connectionYn) {
        update(directMessageRoomMemberStatusEntity)
                .set(directMessageRoomMemberStatusEntity.connectionYn, connectionYn)
                .where(
                        directMessageRoomMemberStatusEntity.username.eq(username)
                )
                .execute();
    }

    @Override
    @Transactional(rollbackFor = Exception.class) @Modifying
    public void updateAlertYn(String directMessageRoomId, String username, String alertYn) {
        update(directMessageRoomMemberStatusEntity)
                .set(directMessageRoomMemberStatusEntity.alertYn, alertYn)
                .where(
                        directMessageRoomMemberStatusEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberStatusEntity.username.eq(username)
                )
                .execute();
    }

    @Override
    public long unreadMessageTotalCountByUsername(String username) {
        return from(directMessageRoomMemberStatusEntity)
                .where(
                        directMessageRoomMemberStatusEntity.username.eq(username),
                        directMessageRoomMemberStatusEntity.connectionYn.eq("N"),
                        directMessageRoomMemberStatusEntity.alertYn.eq("Y")
                )
                .fetchCount();
    }
}
