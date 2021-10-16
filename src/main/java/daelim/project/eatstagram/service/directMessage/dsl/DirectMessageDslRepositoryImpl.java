package daelim.project.eatstagram.service.directMessage.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
import daelim.project.eatstagram.service.directMessage.QDirectMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static daelim.project.eatstagram.service.directMessage.QDirectMessageEntity.*;
import static daelim.project.eatstagram.service.directMessageRoomMember.QDirectMessageRoomMemberEntity.*;

public class DirectMessageDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageDslRepository {

    public DirectMessageDslRepositoryImpl() {
        super(QDirectMessageEntity.class);
    }

    @Override
    public Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId, String username) {

        LocalDateTime conditionDate = from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .select(directMessageRoomMemberEntity.conditionDate)
                .fetchOne();

        List<DirectMessageDTO> content = from(directMessageEntity)
                .where(
                        directMessageEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageEntity.regDate.gt(conditionDate)
                )
                .select(Projections.bean(DirectMessageDTO.class,
                        directMessageEntity.directMessageId,
                        directMessageEntity.directMessageType,
                        directMessageEntity.directMessage,
                        directMessageEntity.directMessageRoomId,
                        directMessageEntity.username,
                        directMessageEntity.regDate
                ))
                .orderBy(directMessageEntity.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(directMessageEntity)
                .where(directMessageEntity.directMessageRoomId.eq(directMessageRoomId))
                .from(directMessageEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional @Modifying
    public void deleteByDirectMessageRoomIds(List<String> directMessageRoomIds) {
        delete(directMessageEntity)
                .where(directMessageEntity.directMessageRoomId.in(directMessageRoomIds))
                .execute();
    }
}
