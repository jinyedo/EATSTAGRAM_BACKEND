package daelim.project.eatstagram.service.contentLike;

import daelim.project.eatstagram.service.base.BaseService;
import daelim.project.eatstagram.service.content.ContentEntity;
import daelim.project.eatstagram.service.content.ContentService;
import daelim.project.eatstagram.service.member.Member;
import daelim.project.eatstagram.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentLikeService extends BaseService<String, ContentLikeEntity, ContentLikeDTO, ContentLikeRepository> {

    private final MemberService memberService;
    private final ContentService contentService;

    public ResponseEntity<String> save(ContentLikeDTO likeDTO) {
        ContentLikeDTO findLike = getRepository().findByUsernameAndContentId(likeDTO.getUsername(), likeDTO.getContentId());
        Optional<Member> member = memberService.getRepository().findById(likeDTO.getUsername());
        Optional<ContentEntity> content = contentService.getRepository().findById(likeDTO.getContentId());

        if (member.isPresent() && content.isPresent()) { // 회원과 콘텐츠가 있는지 확인  - 없다면 error return
            if(findLike == null) { // 좋아요를 누른적이 없다면 좋아요 추가
                super.save(ContentLikeDTO.builder()
                        .username(likeDTO.getUsername())
                        .contentId(likeDTO.getContentId())
                        .build());
                return new ResponseEntity<>("{\"response\": \"ok\", \"likeCount\": \"" + getRepository().countByContentId(likeDTO.getContentId()) + "\"}", HttpStatus.OK);

            } else { // 좋아요를 누른적이 있따면 좋아요 취소
                getRepository().deleteById(findLike.getLikeId());
                return new ResponseEntity<>("{\"response\": \"fail\", \"likeCount\": \"" + getRepository().countByContentId(likeDTO.getContentId()) + "\"}", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.OK);
    }
}
