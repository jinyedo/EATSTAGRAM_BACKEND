package daelim.project.eatstagram.service.contentFile.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.contentFile.ContentFileDTO;
import daelim.project.eatstagram.service.contentFile.QContentFileEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static daelim.project.eatstagram.service.contentFile.QContentFileEntity.contentFileEntity;

public class ContentFileDslRepositoryImpl extends QuerydslRepositorySupport implements ContentFileDslRepository {

    public ContentFileDslRepositoryImpl() {
        super(QContentFileEntity.class);
    }

    @Override
    public List<ContentFileDTO> getListByContentId(String contentId) {
        return from(contentFileEntity)
                .where(contentFileEntity.contentId.eq(contentId))
                .select(Projections.bean(ContentFileDTO.class,
                        contentFileEntity.contentFileId,
                        contentFileEntity.name,
                        contentFileEntity.type,
                        contentFileEntity.path))
                .fetch();
    }

    @Override
    @Transactional @Modifying
    public void deleteByContentIds(List<String> contentIds) {
        delete(contentFileEntity)
                .where(contentFileEntity.contentId.in(contentIds))
                .execute();
    }

    @Override
    @Transactional @Modifying
    public void deleteByContentId(String contentId) {
        delete(contentFileEntity)
                .where(contentFileEntity.contentId.eq(contentId))
                .execute();
    }
}
