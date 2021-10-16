package daelim.project.eatstagram.service.contentLike;

import daelim.project.eatstagram.service.base.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentLikeService extends BaseService<String, ContentLikeEntity, ContentLikeDTO, ContentLikeRepository> {

    public ResponseEntity<String> save(ContentLikeDTO likeDTO) {
        ContentLikeDTO findLike = getRepository().findByUsernameAndContentId(likeDTO.getUsername(), likeDTO.getContentId());

        if(findLike == null) { // 좋아요를 누른적이 없다면 좋아요 추가
            super.save(ContentLikeDTO.builder()
                    .username(likeDTO.getUsername())
                    .contentId(likeDTO.getContentId())
                    .build());
            return new ResponseEntity<>("{\"response\": \"ok\", \"likeCheck\": \"" + true + "\", \"likeCount\": \"" + getRepository().countByContentId(likeDTO.getContentId()) + "\"}", HttpStatus.OK);

        } else { // 좋아요를 누른적이 있따면 좋아요 취소
            getRepository().deleteById(findLike.getLikeId());
            return new ResponseEntity<>("{\"response\": \"ok\", \"likeCheck\": \"" + false + "\", \"likeCount\": \"" + getRepository().countByContentId(likeDTO.getContentId()) + "\"}", HttpStatus.OK);
        }
    }

    public void deleteByContentIds(List<String> contentIds) {
        getRepository().deleteByContentIds(contentIds);
    }
}
