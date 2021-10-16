package daelim.project.eatstagram.service.contentLike.dsl;

import daelim.project.eatstagram.service.contentLike.ContentLikeDTO;

import java.util.List;

public interface ContentLikeDslRepository {

    ContentLikeDTO findByUsernameAndContentId(String username, String contentId);
    long countByContentId(String contentId);
    void deleteByContentIds(List<String> contentIds);
    void deleteByContentId(String contentId);
}
