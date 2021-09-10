package daelim.project.eatstagram.service.contentFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentFileRepository extends JpaRepository<ContentFileEntity, String> {
}
