package daelim.project.eatstagram.service.contentLike;

import daelim.project.eatstagram.service.contentLike.dsl.ContentLikeDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentLikeRepository extends JpaRepository<ContentLikeEntity, String>, ContentLikeDslRepository {
}
