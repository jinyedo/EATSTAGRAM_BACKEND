package daelim.project.eatstagram.service.contentFile.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.contentFile.ContentFileDTO;
import daelim.project.eatstagram.service.contentFile.QContentFileEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

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
}
