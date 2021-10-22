package daelim.project.eatstagram.service.follow;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FollowService extends BaseService<String, FollowEntity, FollowDTO, FollowRepository> {

    public void deleteByUsername(String username) {
        getRepository().deleteByUsername(username);
    }

/* 팔로우 : 해당 유저가 구독한 사람 */
    // 팔로우 페이징 리스트
    public Page<FollowDTO> getFollowPagingList(Pageable pageable, FollowDTO followDTO) {
        Page<FollowDTO> followPagingList = getRepository().getFollowPagingList(pageable, followDTO.getTarget());
        for (FollowDTO dto : followPagingList) {
            String followYn = followCheck(followDTO.getUsername(), dto.getFollow()) == null ? "N" : "Y";
            String followerYn = followerCheck(followDTO.getUsername(), dto.getFollow()) == null ? "N" : "Y";
            dto.setFollowYn(followYn);
            dto.setFollowerYn(followerYn);
        }
        return followPagingList;
    }

    // 팔로우 여부
    public ResponseEntity<String> getFollowYn(String username, String target) {
        String followYn = followCheck(username, target) == null ? "N" : "Y";
        return new ResponseEntity<>("{\"response\": \"ok\", \"followYn\": \"" + followYn + "\"}", HttpStatus.OK);
    }

    // 팔로우 수
    public ResponseEntity<String> getFollowCount(String target) {
        long followCount = getRepository().getFollowCount(target);
        return new ResponseEntity<>("{\"response\": \"ok\", \"followCount\": \"" + followCount + "\"}", HttpStatus.OK);
    }

    // 팔로우 및 언팔로우
    public ResponseEntity<String> save(String username, String target) {
        FollowEntity followEntity = followCheck(username, target);
        if (followEntity == null) {
            FollowDTO followDTO = FollowDTO.builder()
                    .username(username)
                    .follow(target)
                    .build();
            super.save(followDTO);
            return new ResponseEntity<>("{\"response\": \"ok\", \"followYn\": \"Y\"}", HttpStatus.OK);
        } else {
            getRepository().delete(followEntity);
            return new ResponseEntity<>("{\"response\": \"ok\", \"followYn\": \"N\"}", HttpStatus.OK);
        }
    }

    // 팔로우 체크
    public FollowEntity followCheck(String username, String target) {
        return getRepository().followCheck(username, target);
    }

/* 팔로워 : 해당 유저를 구독한 사람 */
    // 팔로우 페이징 리스트
    public Page<FollowDTO> getFollowerPagingList(Pageable pageable, FollowDTO followDTO) {
        Page<FollowDTO> followerPagingList = getRepository().getFollowerPagingList(pageable, followDTO.getTarget());
        for (FollowDTO dto : followerPagingList) {
            System.out.println("---------------------");
            System.out.println(dto.getFollower());
            String followYn = followCheck(followDTO.getUsername(), dto.getFollower()) == null ? "N" : "Y";
            String followerYn = followerCheck(followDTO.getUsername(), dto.getFollower()) == null ? "N" : "Y";
            dto.setFollowYn(followYn);
            dto.setFollowerYn(followerYn);
        }
        return followerPagingList;
    }

    // 팔로워 여부
    public ResponseEntity<String> getFollowerYn(String username, String target) {
        String followerYn = followerCheck(username, target) == null ? "N" : "Y";
        return new ResponseEntity<>("{\"response\": \"ok\", \"followerYn\": \"" + followerYn + "\"}", HttpStatus.OK);
    }

    // 팔로워 수
    public ResponseEntity<String> getFollowerCount(String target) {
        long followerCount = getRepository().getFollowerCount(target);
        return new ResponseEntity<>("{\"response\": \"ok\", \"followerCount\": \"" + followerCount + "\"}", HttpStatus.OK);
    }

    // 팔로워 삭제
    public ResponseEntity<String> delete(String username, String target) {
        try {
            FollowEntity followEntity = followerCheck(username, target);
            if (followEntity == null) {
                return new ResponseEntity<>("{\"response\": \"fail\", \"msg\": \"해당 팔로워가 존재하지 않습니다.\"}", HttpStatus.OK);
            } else {
                getRepository().delete(followEntity);
                return new ResponseEntity<>("{\"response\": \"ok\", \"msg\": \"삭제를 완료했습니다.\"}", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"response\": \"error\", \"msg\": \"서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.\"}", HttpStatus.OK);
        }
    }

    // 팔로워 체크
    public FollowEntity followerCheck(String username, String target) {
        return getRepository().followerCheck(username, target);
    }
}
