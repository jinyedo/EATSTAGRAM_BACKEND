package daelim.project.eatstagram.service.contentSaved;

import daelim.project.eatstagram.service.contentSaved.dsl.ContentSavedDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentSavedRepository extends JpaRepository<ContentSavedEntity, String>, ContentSavedDslRepository {
}
