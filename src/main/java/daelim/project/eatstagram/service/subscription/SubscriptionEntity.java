package daelim.project.eatstagram.service.subscription;

import daelim.project.eatstagram.service.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "subscription")
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class SubscriptionEntity extends Subscription {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber", insertable = false, updatable = false)
    private Member member2;
}
