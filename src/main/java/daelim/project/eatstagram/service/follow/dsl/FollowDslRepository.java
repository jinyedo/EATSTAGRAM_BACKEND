package daelim.project.eatstagram.service.follow.dsl;

import daelim.project.eatstagram.service.follow.FollowDTO;
import daelim.project.eatstagram.service.follow.FollowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowDslRepository {

/* 팔로우 */
    Page<FollowDTO> getFollowPagingList(Pageable pageable, String target);
    FollowEntity followCheck(String username, String target);
    long getFollowCount(String target);

/* 팔로워 */
    Page<FollowDTO> getFollowerPagingList(Pageable pageable, String target);
    FollowEntity followerCheck(String username, String target);
    long getFollowerCount(String target);
    void delete(String username, String target);
}
