package daelim.project.eatstagram.service.content.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.content.ContentDTO;
import daelim.project.eatstagram.service.content.QContentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.content.QContentEntity.contentEntity;

public class ContentDslRepositoryImpl extends QuerydslRepositorySupport implements ContentDslRepository {

    public ContentDslRepositoryImpl() {
        super(QContentEntity.class);
    }

    @Override
    public Page<ContentDTO> getPagingList(Pageable pageable) {
        List<ContentDTO> content = from(contentEntity)
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.contentId,
                        contentEntity.text,
                        contentEntity.location,
                        contentEntity.username
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(contentEntity)
                .select(contentEntity.contentId,
                        contentEntity.text,
                        contentEntity.location,
                        contentEntity.username
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}
