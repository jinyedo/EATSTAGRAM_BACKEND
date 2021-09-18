package daelim.project.eatstagram.service.member.dsl;

import com.querydsl.core.types.Projections;
import daelim.project.eatstagram.service.member.MemberDTO;
import daelim.project.eatstagram.service.member.QMember;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static daelim.project.eatstagram.service.member.QMember.*;

public class MemberDslRepositoryImpl extends QuerydslRepositorySupport implements MemberDslRepository {

    public MemberDslRepositoryImpl() {
        super(QMember.class);
    }

    @Override
    public List<MemberDTO> getListByNameAndNickname(String condition) {
        return from(member)
                .where(
                        member.name.contains(condition).or(member.nickname.contains(condition))
                )
                .select(Projections.bean(MemberDTO.class,
                        member.username,
                        member.name,
                        member.nickname
                ))
                .fetch();
    }
}
