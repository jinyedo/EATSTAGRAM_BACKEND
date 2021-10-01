package daelim.project.eatstagram.service.directMessageRoom.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.directMessageRoom.DirectMessageRoomDTO;
import daelim.project.eatstagram.service.directMessageRoom.QDirectMessageRoom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;

import static daelim.project.eatstagram.service.directMessage.QDirectMessageEntity.directMessageEntity;
import static daelim.project.eatstagram.service.directMessageRoom.QDirectMessageRoom.directMessageRoom;

public class DirectMessageRoomDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageRoomDslRepository {

    public DirectMessageRoomDslRepositoryImpl() {
        super(QDirectMessageRoom.class);
    }

    public List<DirectMessageRoomDTO> getList(List<String> directMessageRoomIdList) {
        return from(directMessageRoom)
                .where(
                        directMessageRoom.directMessageRoomId.in(directMessageRoomIdList)
                )
                .leftJoin(directMessageEntity)
                .on(directMessageEntity.directMessageRoomId.eq(directMessageRoom.directMessageRoomId))
                .select(Projections.bean(DirectMessageRoomDTO.class,
                        directMessageRoom.directMessageRoomId,
                        directMessageRoom.directMessageRoomType,
                        directMessageEntity.regDate.max().as("maxRegDate")
                ))
                .groupBy(directMessageRoom.directMessageRoomId)
                .orderBy(directMessageEntity.regDate.desc())
                .fetch();

    }
}
