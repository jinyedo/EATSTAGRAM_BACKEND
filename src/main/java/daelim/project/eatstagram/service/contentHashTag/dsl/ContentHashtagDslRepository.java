package daelim.project.eatstagram.service.contentHashTag.dsl;

import daelim.project.eatstagram.service.contentHashTag.ContentHashtagDTO;

import java.util.List;

public interface ContentHashtagDslRepository {

    List<ContentHashtagDTO> getListByContentId(String contentId);
}
