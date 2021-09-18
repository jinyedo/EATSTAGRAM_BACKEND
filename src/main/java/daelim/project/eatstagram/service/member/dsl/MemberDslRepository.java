package daelim.project.eatstagram.service.member.dsl;

import daelim.project.eatstagram.service.member.MemberDTO;

import java.util.List;

public interface MemberDslRepository {

    List<MemberDTO> getListByNameAndNickname(String condition);
}
