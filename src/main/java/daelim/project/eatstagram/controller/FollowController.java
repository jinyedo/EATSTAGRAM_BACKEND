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
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우 페이징 리스트
    @RequestMapping("/getPagingList")
    @ResponseBody
    public Page<FollowDTO> getFollowPagingList(Pageable pageable, @ModelAttribute FollowDTO followDTO) {
        return followService.getFollowPagingList(pageable, followDTO);
    }

    // 팔로우 여부
    @RequestMapping("/getFollowYn")
    @ResponseBody
    public ResponseEntity<String> getFollowYn(String username, String target) {
        return followService.getFollowYn(username, target);
    }

    // 팔로우 수
    @RequestMapping("/getFollowCount")
    @ResponseBody
    public ResponseEntity<String> getFollowCount(String target) {
        return followService.getFollowCount(target);
    }

    // 팔로우 및 언팔로우
    @RequestMapping("/save")
    @ResponseBody
    public ResponseEntity<String> save(String username, String target) {
        return followService.save(username, target);
    }
}
