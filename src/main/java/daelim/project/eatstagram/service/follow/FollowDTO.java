package daelim.project.eatstagram.service.follow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter @Setter
@ToString
public class FollowDTO extends Follow {

    private String follower;
    private String nickname;
    private String name;
    private String profileImgName;
    private String followYn;
    private String followerYn;
    private String target;
}
