package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.service.follow.FollowDTO;
import daelim.project.eatstagram.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follower")
@RequiredArgsConstructor
public class FollowerController {

    private final FollowService followService;

    // 팔로워 페이징 리스트
    @RequestMapping("/getPagingList")
    @ResponseBody
    public Page<FollowDTO> getFollowerPagingList(Pageable pageable, @ModelAttribute FollowDTO followDTO) {
        return followService.getFollowerPagingList(pageable, followDTO);
    }

    // 팔로워 여부
    @RequestMapping("/getFollowerYn")
    @ResponseBody
    public ResponseEntity<String> getFollowerYn(String username, String target) {
        return followService.getFollowerYn(username, target);
    }

    // 팔로워 수
    @RequestMapping("/getFollowerCount")
    @ResponseBody
    public ResponseEntity<String> getFollowerCount(String target) {
        return followService.getFollowerCount(target);
    }

    // 팔로워 삭제
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> delete(String username, String target) {
        return followService.delete(username, target);
    }
}
