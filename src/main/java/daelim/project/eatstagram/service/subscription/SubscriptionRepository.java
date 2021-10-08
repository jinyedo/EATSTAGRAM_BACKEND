package daelim.project.eatstagram.service.subscription;

import daelim.project.eatstagram.service.subscription.dsl.SubscriptionDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, String>, SubscriptionDslRepository {

}
