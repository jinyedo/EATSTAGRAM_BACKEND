package daelim.project.eatstagram.service.directMessage;

import daelim.project.eatstagram.service.directMessage.dsl.DirectMessageDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessageEntity, String>, DirectMessageDslRepository {
}
