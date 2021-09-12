package daelim.project.eatstagram.service.contentLike.dsl;

import daelim.project.eatstagram.service.contentLike.ContentLikeDTO;

public interface ContentLikeDslRepository {

    ContentLikeDTO findByUsernameAndContentId(String username, String contentId);
    long countByContentId(String contentId);

}
