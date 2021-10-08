package daelim.project.eatstagram.service.subscription;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter @Setter
@ToString
public class SubscriptionDTO extends Subscription {

    private String nickname;
    private String name;
    private String profileImgName;
}
