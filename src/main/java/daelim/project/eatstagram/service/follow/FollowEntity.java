package daelim.project.eatstagram.service.follow;

import daelim.project.eatstagram.service.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "follow")
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class FollowEntity extends Follow {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow", insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private Member member2;
}
