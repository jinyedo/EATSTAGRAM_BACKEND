package daelim.project.eatstagram.service.directMessageRoomReadStatus;

import daelim.project.eatstagram.service.directMessageRoomReadStatus.dsl.DirectMessageRoomReadStatusDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRoomReadStatusRepository extends JpaRepository<DirectMessageRoomReadStatusEntity, String>, DirectMessageRoomReadStatusDslRepository {
}
