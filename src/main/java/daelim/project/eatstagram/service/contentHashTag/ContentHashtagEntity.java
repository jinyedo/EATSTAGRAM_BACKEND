package daelim.project.eatstagram.service.contentHashTag;

import daelim.project.eatstagram.service.content.ContentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "content_hashtag")
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ContentHashtagEntity extends ContentHashtag {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentId", insertable = false, updatable = false)
    private ContentEntity content;
}
