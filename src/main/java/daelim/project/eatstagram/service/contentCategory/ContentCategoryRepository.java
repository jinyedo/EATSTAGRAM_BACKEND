package daelim.project.eatstagram.service.contentCategory;

import daelim.project.eatstagram.service.contentCategory.dsl.ContentCategoryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentCategoryRepository extends JpaRepository<ContentCategoryEntity, String>, ContentCategoryDslRepository {
}
