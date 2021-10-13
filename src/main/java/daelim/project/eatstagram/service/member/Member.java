package daelim.project.eatstagram.service.member;

import daelim.project.eatstagram.service.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id
    private String username; // 아이디
    private String password; // 비밀번호
    private String name; // 이름
    private String email; // 이메일
    private String nickname; // 닉네임
    private String introduce;
    private boolean formSocial; // 소셜 로그인 유무
    private String socialType; // 소셜 로그인 타입
    private String profileImgName; // 프로필 이미지 이름
    private String profileImgPath; // 프로필 이미지 저장 위치

    @Builder.Default
    @Enumerated(EnumType.STRING) @ElementCollection(fetch = FetchType.LAZY)
    private Set<MemberRole> roleSet = new HashSet<>(); // 사용자 권한

    // 사용자 권한 추가
    public void addMemberRole(MemberRole memberRole) {
        roleSet.add(memberRole);
    }
}
