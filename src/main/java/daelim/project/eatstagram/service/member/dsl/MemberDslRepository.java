package daelim.project.eatstagram.service.member.dsl;

import daelim.project.eatstagram.service.member.MemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberDslRepository {

    // 랭킹별 사용자 리스트 가져오기
    Page<MemberDTO> getRankingPagingList(Pageable pageable);
    // 검색시 사용자 페이징 리스트 가져오기
    Page<MemberDTO> getSearchPagingList(Pageable pageable, String username, String condition);
    // 검색시 사용자 리스트 가져오기
    List<MemberDTO> getSearchList(String username, String condition);
    // 특정 사용자 정보 가져오기
    MemberDTO getMemberInfo(String username);
}
