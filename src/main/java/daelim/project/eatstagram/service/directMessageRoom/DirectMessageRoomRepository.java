package daelim.project.eatstagram.service.directMessageRoom;

import daelim.project.eatstagram.service.directMessageRoomMember.dsl.DirectMessageRoomMemberDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRoomRepository extends JpaRepository<DirectMessageRoom, String>, DirectMessageRoomMemberDslRepository {
}
