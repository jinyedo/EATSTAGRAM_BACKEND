package daelim.project.eatstagram.service.directMessageRoomMember.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.QDirectMessageRoomMemberEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.directMessageRoomMember.QDirectMessageRoomMemberEntity.directMessageRoomMemberEntity;

public class DirectMessageRoomMemberDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageRoomMemberDslRepository {

    public DirectMessageRoomMemberDslRepositoryImpl() {
        super(QDirectMessageRoomMemberEntity.class);
    }

    @Override
    public DirectMessageRoomMemberDTO findByUsernames(List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList) {
        return from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.directMessageRoomType.eq("private"),
                        directMessageRoomMemberEntity.username.eq(directMessageRoomMemberDTOList.get(0).getUsername())
                            .or(directMessageRoomMemberEntity.username.eq(directMessageRoomMemberDTOList.get(1).getUsername()))
                )
                .select(Projections.bean(DirectMessageRoomMemberDTO.class,
                        directMessageRoomMemberEntity.directMessageRoomId,
                        directMessageRoomMemberEntity.directMessageRoomType,
                        directMessageRoomMemberEntity.directMessageRoomId.count().as("count")
                ))
                .groupBy(directMessageRoomMemberEntity.directMessageRoomId)
                .having(directMessageRoomMemberEntity.directMessageRoomId.count().gt(1))
                .fetchOne();
    }
}
