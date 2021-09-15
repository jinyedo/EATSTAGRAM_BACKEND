package daelim.project.eatstagram.service.contentReply;

import daelim.project.eatstagram.service.contentReply.dsl.ContentReplyDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentReplyRepository extends JpaRepository<ContentReplyEntity, String>, ContentReplyDslRepository {
}
