package daelim.project.eatstagram.service.contentSaved;

import daelim.project.eatstagram.service.content.ContentEntity;
import daelim.project.eatstagram.service.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "content_saved")
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ContentSavedEntity extends ContentSaved {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentId", insertable = false, updatable = false)
    private ContentEntity content;
}
