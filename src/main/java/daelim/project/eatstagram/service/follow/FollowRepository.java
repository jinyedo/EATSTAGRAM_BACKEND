package daelim.project.eatstagram.service.follow;

import daelim.project.eatstagram.service.follow.dsl.FollowDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, String>, FollowDslRepository {
}
