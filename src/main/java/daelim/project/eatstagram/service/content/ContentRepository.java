package daelim.project.eatstagram.service.content;

import daelim.project.eatstagram.service.content.dsl.ContentDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<ContentEntity, String>, ContentDslRepository {
}
