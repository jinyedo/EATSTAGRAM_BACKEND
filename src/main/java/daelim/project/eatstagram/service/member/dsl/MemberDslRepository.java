package daelim.project.eatstagram.service.member.dsl;

import daelim.project.eatstagram.service.member.MemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberDslRepository {

    Page<MemberDTO> getSearchPagingList(Pageable pageable, String username, String condition);
    List<MemberDTO> getSearchList(String username, String condition);
    MemberDTO getMemberInfo(String username);
}
