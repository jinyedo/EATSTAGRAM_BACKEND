package daelim.project.eatstagram.service.directMessage.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
import daelim.project.eatstagram.service.directMessage.QDirectMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.directMessage.QDirectMessageEntity.*;

public class DirectMessageDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageDslRepository {

    public DirectMessageDslRepositoryImpl() {
        super(QDirectMessageEntity.class);
    }

    @Override
    public Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId) {

        List<DirectMessageDTO> content = from(directMessageEntity)
                .where(
                        directMessageEntity.directMessageRoomId.eq(directMessageRoomId)
                )
                .select(Projections.bean(DirectMessageDTO.class,
                        directMessageEntity.directMessageId,
                        directMessageEntity.directMessageType,
                        directMessageEntity.directMessage,
                        directMessageEntity.directMessageRoomId,
                        directMessageEntity.username
                ))
                .orderBy(directMessageEntity.directMessageId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(directMessageEntity)
                .where(directMessageEntity.directMessageRoomId.eq(directMessageRoomId))
                .from(directMessageEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}
