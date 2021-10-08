package daelim.project.eatstagram.service.content;

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

    private String nickname;
    private String profileImgName;

    @Builder.Default
    private List<ContentHashtagDTO> contentHashtagDTOList = new ArrayList<>();

    @Builder.Default
    private List<ContentCategoryDTO> contentCategoryDTOList = new ArrayList<>();

    @Builder.Default
    private List<ContentFileDTO> contentFileDTOList = new ArrayList<>();

    private long likeCount;
    private boolean likeCheck;
}
