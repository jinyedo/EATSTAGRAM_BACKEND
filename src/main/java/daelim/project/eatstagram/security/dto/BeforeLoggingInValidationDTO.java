package daelim.project.eatstagram.security.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
public class BeforeLoggingInValidationDTO {

    @NotBlank(message = "회원 아이디는 필수 입력 정보입니다.")
    private String username;

    @NotBlank(message = "새 비밀번호는 필수 입력 정보입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}", message = "올바른 형식의 비밀번호를 입력해주세요.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인은 필수 입력정보 입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}", message = "올바른 형식의 비밀번호를 입력해주세요.")
    private String newPasswordConfirm;
}
