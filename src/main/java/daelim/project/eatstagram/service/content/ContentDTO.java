package daelim.project.eatstagram.service.content;

import com.querydsl.jpa.JPQLQuery;
import daelim.project.eatstagram.service.contentCategory.ContentCategoryDTO;
import daelim.project.eatstagram.service.contentFile.ContentFileDTO;
import daelim.project.eatstagram.service.contentHashTag.ContentHashtagDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
public class ContentDTO extends Content {

    private List<ContentHashtagDTO> contentHashtagDTOList = new ArrayList<>();
    private List<ContentCategoryDTO> contentCategoryDTOList = new ArrayList<>();
    private List<ContentFileDTO> contentFileDTOList = new ArrayList<>();
}
