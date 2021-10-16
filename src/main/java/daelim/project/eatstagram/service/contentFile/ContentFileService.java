package daelim.project.eatstagram.service.contentFile;

import daelim.project.eatstagram.service.base.BaseService;
import daelim.project.eatstagram.storage.StorageRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class ContentFileService extends BaseService<String, ContentFileEntity, ContentFileDTO, ContentFileRepository> {

    private final StorageRepository storageRepository;
    public static final String CONTENT_FILE_FOLDER_NAME = "content";

    public void deleteByContentIds(List<String> contentIds) {
        getRepository().deleteByContentIds(contentIds);
    }

    public void deleteByContentId(String contentId) {
        getRepository().deleteByContentId(contentId);
    }

    public void videoStream(String contentName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("----------콘텐츠 불러오기----------");
        log.info("contentName : " + contentName);
        String path = storageRepository.getRootLocation().toString() + File.separator + CONTENT_FILE_FOLDER_NAME + File.separator;

        // 확장자 확인
        String[] filenameSeparate = contentName.split("\\.");
        log.info("파일 확장자 확인 : " + Arrays.toString(filenameSeparate));
        // 확장자 명이 없거나 mp4 형식이 아니라면 예외 발생
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
