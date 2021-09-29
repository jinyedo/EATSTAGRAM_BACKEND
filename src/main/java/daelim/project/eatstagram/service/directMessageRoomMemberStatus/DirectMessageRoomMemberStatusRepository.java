package daelim.project.eatstagram.service.directMessageRoomMemberStatus;

import daelim.project.eatstagram.service.directMessageRoomMemberStatus.dsl.DirectMessageRoomMemberStatusDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRoomMemberStatusRepository extends JpaRepository<DirectMessageRoomMemberStatusEntity, String>, DirectMessageRoomMemberStatusDslRepository {
}
