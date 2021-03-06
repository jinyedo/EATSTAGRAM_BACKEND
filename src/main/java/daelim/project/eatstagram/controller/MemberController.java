package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.security.dto.AuthMemberDTO;
import daelim.project.eatstagram.security.dto.AfterLoggingInValidationDTO;
import daelim.project.eatstagram.security.dto.BeforeLoggingInValidationDTO;
import daelim.project.eatstagram.security.dto.ValidationMemberDTO;
import daelim.project.eatstagram.service.biz.MemberBizService;
import daelim.project.eatstagram.service.emailAuth.EmailAuthDTO;
import daelim.project.eatstagram.service.emailAuth.EmailAuthService;
import daelim.project.eatstagram.service.member.MemberDTO;
import daelim.project.eatstagram.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberBizService memberBizService;
    private final EmailAuthService emailAuthService;
    private ValidationMemberDTO validationMemberDTO;

    // 로그인 성공
    @RequestMapping("loginSuccess")
    @ResponseBody
    public ResponseEntity<String> loginSuccess(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        return new ResponseEntity<>("{\"response\": \"ok\"," +
                " \"username\": \"" + authMemberDTO.getUsername() + "\"," +
                " \"nickname\": \"" + authMemberDTO.getNickname() + "\"}", HttpStatus.OK);
    }

    // 로그인 실패
    @RequestMapping("loginFail")
    @ResponseBody
    public ResponseEntity<String> loginFail(HttpServletRequest request) {
        return new ResponseEntity<>("{\"response\": \"fail\", \"msg\": \"" + request.getAttribute("msg") + "\"}", HttpStatus.OK);
    }

    // 사용자 아이디 중복확인
    @RequestMapping("/checkUsername")
    @ResponseBody
    public ResponseEntity<String> checkUsername(String username) {
        return new ResponseEntity<>("{\"response\": \"" + memberService.checkUsername(username) + "\"}" , HttpStatus.OK);
    }

    // 사용자 닉네임 중복확인
    @RequestMapping("/checkNickname")
    @ResponseBody
    public ResponseEntity<String> checkNickname(String nickname) {
        return new ResponseEntity<>("{\"response\": " + "\"" + memberService.checkNickname(nickname) + "\"}" , HttpStatus.OK);
    }

    // 회원가입 1단계 : 회원 정보 유효성 검사
    @RequestMapping("/join/step/one")
    @ResponseBody
    public ResponseEntity<Object> joinStepOne(@ModelAttribute @Valid ValidationMemberDTO validationMemberDTO, Errors errors) {
        if (errors.hasErrors()) {
            log.info("-----회원가입 유효성 검사 오류 종류-----");
            for (FieldError error : errors.getFieldErrors()) {
                log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
            }
            log.info("------------------------------------");
            return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.OK);
        } else {
            this.validationMemberDTO = validationMemberDTO;
            return new ResponseEntity<>("{\"response\": \"ok\", \"joinToken\": \"true\"}", HttpStatus.OK);
        }
    }

    // 회원가입 2단계 : 이메일 인증번호 발송
    @RequestMapping("/join/step/two")
    @ResponseBody
    public ResponseEntity<EmailAuthDTO> joinStepTwo() {
        EmailAuthDTO emailAuthenticationDTO =
                emailAuthService.createEmailCertificationNumber(
                        validationMemberDTO.getUsername(),
                        validationMemberDTO.getEmail());
        return new ResponseEntity<>(emailAuthenticationDTO, HttpStatus.OK);
    }

    // 회원가입 3단계 : 회원가입 완료
    @RequestMapping("/join/step/three")
    @ResponseBody
    public ResponseEntity<ValidationMemberDTO> joinStepThree(String emailAuthId) {
        emailAuthService.confirmEmail(emailAuthId);
        return new ResponseEntity<>(memberService.join(validationMemberDTO), HttpStatus.OK);
    }

    // 소셜 로그인 회원가입
    @RequestMapping("/join/social")
    public ResponseEntity<Object> joinSocial(@ModelAttribute MemberDTO memberDTO) {
        return memberService.joinSocial(memberDTO);
    }

    // 비밀번호 찾기 링크 전송
    @RequestMapping("/sendFindPasswordLink")
    @ResponseBody
    public ResponseEntity<String> sendFindPasswordLink(String username) {
        return memberService.sendFindPasswordLink(username);
    }

    // 로그인 전 비밀번호 변경
    @RequestMapping("/changePasswordBeforeLoggingIn")
    @ResponseBody
    public ResponseEntity<String> changePasswordBeforeLoggingIn(@ModelAttribute @Valid BeforeLoggingInValidationDTO beforeLoggingInValidationDTO, Errors errors) {
        if (errors.hasErrors()){
            log.info("-----비밀번호 변경 유효성 검사 오류 종류-----");
            log.info("username : " + beforeLoggingInValidationDTO.getUsername());
            log.info("newPassword : " + beforeLoggingInValidationDTO.getNewPassword());
            log.info("newPasswordConfirm : " + beforeLoggingInValidationDTO.getNewPasswordConfirm());
            for (FieldError error : errors.getFieldErrors()) {
                log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
            }
            log.info("----------------------------------------");
            return new ResponseEntity<>("{\"response\": \"fail\", \"msg\": \"입력값이 잘못되었습니다. 다시 확인해 주세요.\"}", HttpStatus.OK);
        }
        return memberService.changePasswordBeforeLoggingIn(beforeLoggingInValidationDTO);
    }

    // 로그인 후 비밀번호 변경
    @RequestMapping("/changePasswordAfterLoggingIn")
    @ResponseBody
    public ResponseEntity<String> changePasswordAfterLoggingIn(@ModelAttribute @Valid AfterLoggingInValidationDTO changePasswordValidationDTO, Errors errors) {
        if (errors.hasErrors()){
            log.info("-----비밀번호 변경 유효성 검사 오류 종류-----");
            for (FieldError error : errors.getFieldErrors()) {
                log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
            }
            log.info("----------------------------------------");
            return new ResponseEntity<>("{\"response\": \"fail\", \"msg\": \"입력값이 잘못되었습니다. 다시 확인해 주세요.\"}", HttpStatus.OK);
        }
        return memberService.changePasswordAfterLoggingIn(changePasswordValidationDTO);
    }

    // 특정 사용자 정보 가져오기
    @RequestMapping("/getMemberInfo")
    @ResponseBody
    public MemberDTO getMemberInfo(String username) {
        return memberService.getMemberInfo(username);
    }

    // 특정 사용자 정보 수정하기
    @RequestMapping("/setMemberInfo")
    @ResponseBody
    public MemberDTO setMemberInfo(@ModelAttribute MemberDTO memberDTO) {
        return memberService.setMemberInfo(memberDTO);
    }

    // 프로필 사진 저장 및 삭제
    @RequestMapping("/saveProfileImg")
    @ResponseBody
    public ResponseEntity<Object> saveProfileImg(String username, MultipartFile file) {
        return memberService.saveProfileImg(username, file);
    }

    // 랭킹별 사용자 페이징 리스트 가져오기
    @RequestMapping("/getRankingPagingList")
    @ResponseBody
    public Page<MemberDTO> getRankingPagingList(Pageable pageable, String username) {
        return memberBizService.getRankingPagingList(pageable, username);
    }

    // 사용자 검색 페이징리스트
    @RequestMapping("/getSearchPagingList")
    @ResponseBody
    public Page<MemberDTO> getSearchPagingList(Pageable pageable, String username, String condition) {
        return memberBizService.getSearchPagingList(pageable, username, condition);
    }

    // 사용자 검색 리스트
    @RequestMapping("/getSearchList")
    @ResponseBody
    public List<MemberDTO> getSearchList(String username, String condition) {
        return memberService.getSearchList(username, condition);
    }
}
