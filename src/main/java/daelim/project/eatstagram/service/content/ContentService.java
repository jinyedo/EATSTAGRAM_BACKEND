package daelim.project.eatstagram.service.content;

import daelim.project.eatstagram.service.base.BaseService;
import daelim.project.eatstagram.service.contentCategory.ContentCategoryDTO;
import daelim.project.eatstagram.service.contentCategory.ContentCategoryService;
import daelim.project.eatstagram.service.contentFile.ContentFileDTO;
import daelim.project.eatstagram.service.contentFile.ContentFileService;
import daelim.project.eatstagram.service.contentHashTag.ContentHashtagDTO;
import daelim.project.eatstagram.service.contentHashTag.ContentHashtagService;
import daelim.project.eatstagram.storage.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService extends BaseService<String, ContentEntity, ContentDTO, ContentRepository> {

    private final String CONTENT_FILE_FOLDER_NAME = "content";

    private final ContentFileService contentFileService;
    private final ContentHashtagService contentHashtagService;
    private final ContentCategoryService contentCategoryService;
    private final StorageRepository storageRepository;

    public Page<ContentDTO> getPagingList(Pageable pageable) {
        Page<ContentDTO> pagingList = getRepository().getPagingList(pageable);
        for (ContentDTO contentDTO : pagingList) {
            List<ContentFileDTO> contentFileList = contentFileService.getRepository().getListByContentId(contentDTO.getContentId());
            List<ContentHashtagDTO> contentHashtagList = contentHashtagService.getRepository().getListByContentId(contentDTO.getContentId());
            List<ContentCategoryDTO> contentCategoryList = contentCategoryService.getRepository().getListByContentId(contentDTO.getContentId());
            contentDTO.setContentFileDTOList(contentFileList);
            contentDTO.setContentHashtagDTOList(contentHashtagList);
            contentDTO.setContentCategoryDTOList(contentCategoryList);
        }
        return pagingList;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> add(ContentDTO contentDTO, MultipartFile[] uploadFiles) {
        if (uploadFiles.length <= 0 || contentDTO.getContentCategoryDTOList().size() <= 0 || StringUtils.isEmpty(contentDTO.text))
            return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.BAD_REQUEST);

        ContentDTO result = save(contentDTO);
        for (ContentHashtagDTO contentHashtagDTO : contentDTO.getContentHashtagDTOList()) {
            contentHashtagDTO.setContentId(result.getContentId());
            contentHashtagService.save(contentHashtagDTO);
        }
        for (ContentCategoryDTO contentCategoryDTO : contentDTO.getContentCategoryDTOList()) {
            contentCategoryDTO.setContentId(result.getContentId());
            contentCategoryService.save(contentCategoryDTO);
        }

        //Path folderPath = storageRepository.makeFolder(CONTENT_FILE_FOLDER_NAME);
        Path folderPath = storageRepository.getRootLocation();

        for (MultipartFile uploadFile : uploadFiles) {

            String fileType = uploadFile.getContentType();
            assert fileType != null;
            // "이미지" 와 "동영상" 파일만 업로드 가능
            if (!(fileType.startsWith("image") || fileType.startsWith("video"))) {
                log.warn("this file is not image or video type");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                // HTTP 403 Forbidden 클라이언트 오류 상태 응답 코드는 서버에 요청이 전달되었지만, 권한 때문에 거절되었다는 것을 의미
                return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE 나 Edge 는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            assert originalName != null;
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            String uuid = UUID.randomUUID().toString();
            fileName =  uuid + "_" + fileName;
            String saveName = folderPath + File.separator + fileName;
            Path savePath = Paths.get(saveName);

            ContentFileDTO contentFileDTO = ContentFileDTO.builder()
                    .name(fileName)
                    .type(fileType)
                    .path(savePath.normalize().toAbsolutePath().toString())
                    .contentId(result.getContentId())
                    .build();
            contentFileService.save(contentFileDTO);

            try {
                uploadFile.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("{\"response\": \"error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("{\"response\": \"ok\"}", HttpStatus.OK);
    }

    public void videoStream(String contentName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("----------콘텐츠 불러오기----------");
        log.info("contentName : " + contentName);
        //String path = storageRepository.getRootLocation().toString() + File.separator + CONTENT_FILE_FOLDER_NAME + File.separator;
        String path = storageRepository.getRootLocation().toString() + File.separator;
        // 확장자 확인
        String[] filenameSeparate = contentName.split("\\.");
        log.info("파일 확장자 확인 : " + Arrays.toString(filenameSeparate));
        // 확장자 명이 없거나 mp3 형식이 아니라면 예외 발생
        if (filenameSeparate.length <= 1 || !filenameSeparate[1].equals("mp4"))
            throw new RuntimeException("ERROR!!! 파일 형식 오류");

        // 해당 파일이 없을 경우 예외 발생
        File file = new File(path + contentName);
        log.info("file : " + file);
        if (!file.exists()) throw new FileNotFoundException("ERROR!!! 파일을 찾을 수 없습니다.");

        // Progressbar 에서 특정 위치를 클릭하거나 해서 임의 위치의 내용을 요청할 수 있으므로
        // 파일의 임의의 위치에서 읽어오기 위해 RandomAccessFile 클래스를 사용한다.
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        long rangeStart = 0;    // 요청 범위의 시작 위치
        long rangeEnd = 0;      // 요청 범위의 끝 위치
        boolean isPart = false; // 부분 요청일 경우 true, 전체 요청일 경우 false

        // randomAccessFile 을 클로즈 하기 위하여
        try {
            // 콘텐츠 파일 크기
            long contentSize = randomAccessFile.length();

            // 스트림 요청 범위, request 에서 헤더의 range 를 읽는다.
            String range = request.getHeader("range");

            // 브라우저에 따라 range 형식이 다른데, 기본 형식은 "bytes={start}-{end}" 형식이다.
            // range 가 null 이거나, rangeStart 가 0이고 end 가 없을 경우 전체 요청이다.
            // 요청 범위를 구한다.
            if (range != null) {
                // 처리의 편의를 위해 요청 range 에 end 값이 없을 경우 넣어줌
                if (range.endsWith("-")) {
                    range = range + (contentSize - 1);
                }
                int idxm = range.trim().indexOf("-"); //"-" 위치
                rangeStart = Long.parseLong(range.substring(6, idxm));
                rangeEnd = Long.parseLong(range.substring(idxm + 1));
                if (rangeStart > 0) {
                    isPart = true;
                }
            } else {
                // range 가 null 인 경우 동영상 전체 크기로 초기값을 넣어줌. 0부터 시작하므로 -1
                rangeStart = 0;
                rangeEnd = contentSize - 1;
            }
            // 전송 파일 크기
            long partSize = rangeEnd - rangeStart + 1;
            // 전송시작
            response.reset();
            // 전체 요청일 경우 200, 부분 요청일 경우 206을 반환상태 코드로 지정
            response.setStatus(isPart ? 206 : 200);
            // mime type 지정
            response.setContentType("audio/mp4");
            // 전송 내용을 헤드에 넣어준다. 마지막에 파일 전체 크기를 넣는다.
            response.setHeader("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + contentSize);
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", "" + partSize);
            OutputStream out = response.getOutputStream();
            // 동영상 파일의 전송시작 위치 지정
            randomAccessFile.seek(rangeStart);
            // 파일 전송... java io는 1회 전송 byte 수가 int 로 지정됨
            // 동영상 파일의 경우 int 형으로는 처리 안되는 크기의 파일이 있으므로
            // 8kb로 잘라서 파일의 크기가 크더라도 문제가 되지 않도록 구현
            int bufferSize = 8 * 1024;
            byte[] buf = new byte[bufferSize];
            do {
                int block = partSize > bufferSize ? bufferSize : (int) partSize;
                int len = randomAccessFile.read(buf, 0, block);
                out.write(buf, 0, len);
                partSize -= block;
            }
            while (partSize > 0);
        } catch (IOException e) {
            // 전송 중에 브라우저를 닫거나, 화면을 전환한 경우 종료해야 하므로 전송취소.
            // progressBar 를 클릭한 경우에는 클릭한 위치값으로 재요청이 들어오므로 전송 취소.
        } finally {
            randomAccessFile.close();
        }
    }
}
