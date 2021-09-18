package daelim.project.eatstagram.service.directMessageRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRoomRepository extends JpaRepository<DirectMessageRoom, String> {
}
