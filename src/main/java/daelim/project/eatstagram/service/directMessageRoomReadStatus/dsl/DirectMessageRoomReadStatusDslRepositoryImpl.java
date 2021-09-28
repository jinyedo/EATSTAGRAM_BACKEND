package daelim.project.eatstagram.service.directMessageRoomReadStatus.dsl;

import daelim.project.eatstagram.service.directMessageRoomReadStatus.QDirectMessageRoomReadStatusEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import static daelim.project.eatstagram.service.directMessageRoomReadStatus.QDirectMessageRoomReadStatusEntity.*;

public class DirectMessageRoomReadStatusDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageRoomReadStatusDslRepository {

    public DirectMessageRoomReadStatusDslRepositoryImpl() {
        super(QDirectMessageRoomReadStatusEntity.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) @Modifying
    public void updateReadYn(String directMessageRoomId, String username, String readYn) {
        update(directMessageRoomReadStatusEntity)
                .set(directMessageRoomReadStatusEntity.readYn, readYn)
                .where(
                        directMessageRoomReadStatusEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomReadStatusEntity.username.eq(username)
                )
                .execute();
    }
}
