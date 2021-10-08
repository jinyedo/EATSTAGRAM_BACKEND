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
import static daelim.project.eatstagram.service.member.QMember.*;

public class ContentDslRepositoryImpl extends QuerydslRepositorySupport implements ContentDslRepository {

    public ContentDslRepositoryImpl() {
        super(QContentEntity.class);
    }

    @Override
    public Page<ContentDTO> getPagingList(Pageable pageable) {
        List<ContentDTO> content = from(contentEntity)
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.contentId,
                        contentEntity.text,
                        contentEntity.location,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .orderBy(contentEntity.contentId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(contentEntity)
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(contentEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ContentDTO> getMyPagingListBy(Pageable pageable, String username) {
        List<ContentDTO> content = from(contentEntity)
                .where(
                        contentEntity.username.eq(username)
                )
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.contentId,
                        contentEntity.text,
                        contentEntity.location,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .orderBy(contentEntity.contentId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(contentEntity)
                .where(contentEntity.username.eq(username))
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(contentEntity)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public ContentDTO findByContentId(String contentId) {
        return from(contentEntity)
                .where(contentEntity.contentId.eq(contentId))
                .leftJoin(member)
                .on(member.username.eq(contentEntity.username))
                .select(Projections.bean(ContentDTO.class,
                        contentEntity.text,
                        contentEntity.location,
                        member.username,
                        member.nickname,
                        member.profileImgName
                ))
                .fetchOne();
    }
}
