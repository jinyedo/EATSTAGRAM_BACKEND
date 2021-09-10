package daelim.project.eatstagram.service.contentFile;

import daelim.project.eatstagram.service.base.BaseEntity;
import daelim.project.eatstagram.service.base.DTOKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ContentFile extends BaseEntity {

    @Id @DTOKey("CF")
    protected String contentFileId;
    protected String name; // 파일 이름
    protected String type; // 파일 타입 ex) img, mp3, mp4...
    protected String path; // 파일 경로
    protected String contentId;
}
