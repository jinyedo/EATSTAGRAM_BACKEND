package daelim.project.eatstagram.service.directMessageRoomMember.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.QDirectMessageRoomMemberEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static daelim.project.eatstagram.service.directMessageRoomMember.QDirectMessageRoomMemberEntity.directMessageRoomMemberEntity;
import static daelim.project.eatstagram.service.member.QMember.member;

public class DirectMessageRoomMemberDslRepositoryImpl extends QuerydslRepositorySupport implements DirectMessageRoomMemberDslRepository {

    public DirectMessageRoomMemberDslRepositoryImpl() {
        super(QDirectMessageRoomMemberEntity.class);
    }

    @Override
    public List<DirectMessageRoomMemberDTO> findByDirectMessageRoomIdJoinMember(String directMessageRoomId) {
        return from(directMessageRoomMemberEntity)
                .where(directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId))
                .leftJoin(member)
                .on(directMessageRoomMemberEntity.username.eq(member.username))
                .select(Projections.bean(DirectMessageRoomMemberDTO.class,
                        member.username,
                        member.name,
                        member.nickname
                ))
                .fetch();
    }

    @Override
    public List<DirectMessageRoomMemberDTO> findByDirectMessageRoomId(String directMessageRoomId, String username) {
        return from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.username.eq(username).not(),
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId)
                )
                .select(Projections.bean(DirectMessageRoomMemberDTO.class,
                    directMessageRoomMemberEntity.username
                ))
                .fetch();
    }

    @Override
    public List<DirectMessageRoomMemberDTO> findByDirectMessageRoomIdAndNotUsernameJoinMember(String directMessageRoomId, String username) {
        return from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.username.eq(username).not(),
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId)
                )
                .leftJoin(member)
                .on(member.username.eq(directMessageRoomMemberEntity.username))
                .select(Projections.bean(DirectMessageRoomMemberDTO.class,
                        member.username,
                        member.nickname,
                        member.name
                ))
                .fetch();
    }

    @Override
    public List<DirectMessageRoomMemberDTO> findByUsername(String username) {
        return from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.inYn.eq("Y"),
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .select(Projections.bean(DirectMessageRoomMemberDTO.class,
                        directMessageRoomMemberEntity.directMessageRoomId,
                        directMessageRoomMemberEntity.directMessageRoomType
                ))
                .fetch();
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

    @Override
    public String getInYn(String directMessageRoomId, String username) {
        return from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .select(directMessageRoomMemberEntity.inYn)
                .fetchOne();
    }

    @Override
    @Transactional(rollbackFor = Exception.class) @Modifying
    public void updateInYn(String directMessageRoomId, String username, String inYn) {
        update(directMessageRoomMemberEntity)
                .set(directMessageRoomMemberEntity.inYn, inYn)
                .where(
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .execute();
    }

    @Override
    public String getConnectionYn(String directMessageRoomId, String username) {
        return from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .select(
                        directMessageRoomMemberEntity.connectionYn
                )
                .fetchOne();
    }

    @Override
    public String getAlertYn(String directMessageRoomId, String username) {
        return from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .select(
                        directMessageRoomMemberEntity.alertYn
                )
                .fetchOne();
    }

    @Override
    @Transactional(rollbackFor = Exception.class) @Modifying
    public void updateConnectionYn(String directMessageRoomId, String username, String connectionYn) {
        update(directMessageRoomMemberEntity)
                .set(directMessageRoomMemberEntity.connectionYn, connectionYn)
                .where(
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .execute();
    }

    @Override
    @Transactional(rollbackFor = Exception.class) @Modifying
    public void updateAllConnectionYn(String username, String connectionYn) {
        update(directMessageRoomMemberEntity)
                .set(directMessageRoomMemberEntity.connectionYn, connectionYn)
                .where(
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .execute();
    }

    @Override
    @Transactional(rollbackFor = Exception.class) @Modifying
    public void updateAlertYn(String directMessageRoomId, String username, String alertYn) {
        update(directMessageRoomMemberEntity)
                .set(directMessageRoomMemberEntity.alertYn, alertYn)
                .where(
                        directMessageRoomMemberEntity.directMessageRoomId.eq(directMessageRoomId),
                        directMessageRoomMemberEntity.username.eq(username)
                )
                .execute();
    }

    @Override
    public long unreadMessageTotalCountByUsername(String username) {
        return from(directMessageRoomMemberEntity)
                .where(
                        directMessageRoomMemberEntity.username.eq(username),
                        directMessageRoomMemberEntity.connectionYn.eq("N"),
                        directMessageRoomMemberEntity.alertYn.eq("Y")
                )
                .fetchCount();
    }
}
