package daelim.project.eatstagram.service.member;

import daelim.project.eatstagram.security.dto.ValidationMemberDTO;
import daelim.project.eatstagram.service.base.BaseService;
import daelim.project.eatstagram.service.base.ModelMapperUtils;
import daelim.project.eatstagram.storage.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService extends BaseService<String, Member, MemberDTO, MemberRepository> {

    private final StorageRepository storageRepository;
    private static final String PROFILE_IMAGE_FOLDER_NAME = "profile";
    private final PasswordEncoder passwordEncoder;

    // 일반 회원가입
    public ValidationMemberDTO join(ValidationMemberDTO dto) {
        Optional<Member> result =  getRepository().findByUsernameAndFormSocial(dto.getUsername(), false);
        if (result.isPresent()) {
            dto.setMsg(dto.getUsername() + "은 이미 존재하는 아이디입니다.");
            return dto;
        } else {
            Member member = Member.builder()
                        .username(dto.getUsername())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .email(dto.getEmail())
                        .name(dto.getName())
                        .nickname(dto.getNickname())
                        .formSocial(false)
                        .build();
            member.addMemberRole(MemberRole.USER);
            getRepository().save(member);
        }
        dto.setMsg("회원가입 성공");
        dto.setJoinSuccessYn(true);
        return dto;
    }

    // 소셜 회원가입
    public ResponseEntity<Object> joinSocial(MemberDTO dto) {
        Optional<Member> result =  getRepository().findByUsernameAndFormSocial(dto.getUsername(), true);
        String password = UUID.randomUUID().toString();
        if (result.isEmpty()) {
            Member member = Member.builder()
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(password))
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .nickname(dto.getNickname())
                    .formSocial(true)
                    .socialType(dto.getSocialType())
                    .build();
            member.addMemberRole(MemberRole.USER);
            getRepository().save(member);
            MemberDTO memberDTO = ModelMapperUtils.map(member, MemberDTO.class);
            memberDTO.setPassword(password);
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        }
        MemberDTO memberDTO = ModelMapperUtils.map(result.get(), MemberDTO.class);
        memberDTO.setPassword(passwordEncoder.encode(password));
        save(memberDTO);
        memberDTO.setPassword(password);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    // 아이디 중복 검사
    public String checkUsername(String username) {
        Optional<Member> result = getRepository().findByUsernameAndFormSocial(username,false);
        if (result.isPresent()) {
            return "fail";
        }
        return "ok";
    }

    // 닉네임 중복검사
    public String checkNickname(String nickname) {
        Optional<Member> result =  getRepository().findMemberByNickname(nickname);
        if (result.isPresent()) {
            return "fail";
        } else {
            return "ok";
        }
    }

    // 검색시 사용자 리스트 가져오기
    public List<MemberDTO> getSearchList(String username, String condition) {
        return getRepository().getSearchList(username, condition);
    }

    // 사용자 정보 가져오기
    public MemberDTO getMemberInfo(String username) {
        return getRepository().getMemberInfo(username);
    }

    // 특정 사용자 정보 수정하기
    public MemberDTO setMemberInfo(MemberDTO memberDTO) {
        Member member = getRepository().findByUsername(memberDTO.getUsername()).orElseThrow();
        if (StringUtils.isNotEmpty(memberDTO.getName())) member.setName(memberDTO.getName());
        if (StringUtils.isNotEmpty(memberDTO.getNickname())) member.setNickname(memberDTO.getNickname());
        if (StringUtils.isNotEmpty(memberDTO.getIntroduce())) member.setIntroduce(memberDTO.getIntroduce());
        getRepository().save(member);
        return ModelMapperUtils.map(member, MemberDTO.class);
    }

    // 비밀번호 변경하기
    public ResponseEntity<String> setPassword(String username, String password, String newPassword, String newPasswordConfirm) {
        try {
            if (!newPassword.equals(newPasswordConfirm)) return new ResponseEntity<>("{\"response\": \"fail\", \"msg\": \"새 비밀번호가 일치하지 않습니다.\"}", HttpStatus.OK);
            Member member = getRepository().findByUsername(username).orElseThrow();
            boolean result = passwordEncoder.matches(password, member.getPassword());
            if (result) {
                member.setPassword(passwordEncoder.encode(newPassword));
                getRepository().save(member);
                return new ResponseEntity<>("{\"response\": \"true\", \"msg\": \"비밀번호 변경 완료\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("{\"response\": \"fail\", \"msg\": \"현재 비밀번호가 일치하지 않습니다.\"}", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"response\": \"error\", \"msg\": \"서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.\"}", HttpStatus.OK);
        }
    }

    // 프로필 이미지 저장 및 삭제
    public ResponseEntity<Object> saveProfileImg(String username, MultipartFile file) {
        Path folderPath = storageRepository.makeFolder(PROFILE_IMAGE_FOLDER_NAME);
        String fileName = null;
        if (file == null) {
            Member member = getRepository().findByUsername(username).orElseThrow();
            String profileImgPath = member.getProfileImgPath();
            if (!profileImgPath.isEmpty()) {
                File deleteFile = new File(profileImgPath);
                if (deleteFile.exists()) {
                    deleteFile.delete();
                }
                member.setProfileImgName(null);
                member.setProfileImgPath(null);
                getRepository().save(member);
            }
        } else {
            String fileType = file.getContentType();
            assert fileType != null;
            // 이미지 파일만 업로드 가능
            if (!fileType.startsWith("image")) {
                log.warn("this file is not image type");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.OK);
            }

            // 실제 파일 이름 IE 나 Edge 는 전체 경로가 들어오므로
            String originalName = file.getOriginalFilename();
            assert originalName != null;
            fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            String uuid = UUID.randomUUID().toString();
            fileName =  uuid + "_" + fileName;
            String saveName = folderPath + File.separator + fileName;
            Path savePath = Paths.get(saveName);

            Member member = getRepository().findByUsername(username).orElseThrow();
            member.setProfileImgName(fileName);
            member.setProfileImgPath(savePath.normalize().toAbsolutePath().toString());
            getRepository().save(member);

            try {
                file.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.OK);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("profileImgName", fileName);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }
}
