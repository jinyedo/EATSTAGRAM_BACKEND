package daelim.project.eatstagram.service.directMessageRoomMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRoomMemberRepository extends JpaRepository<DirectMessageRoomMemberEntity, String> {
}
