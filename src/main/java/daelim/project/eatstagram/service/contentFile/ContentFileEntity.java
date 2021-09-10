package daelim.project.eatstagram.service.contentFile;

import daelim.project.eatstagram.service.content.ContentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "content_file")
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ContentFileEntity extends ContentFile {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentId", insertable = false, updatable = false)
    private ContentEntity content;
}
