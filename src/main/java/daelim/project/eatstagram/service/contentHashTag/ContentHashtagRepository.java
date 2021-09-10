package daelim.project.eatstagram.service.contentHashTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentHashtagRepository extends JpaRepository<ContentHashtagEntity, String> {
}
