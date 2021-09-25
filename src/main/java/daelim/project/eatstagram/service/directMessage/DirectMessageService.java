package daelim.project.eatstagram.service.directMessage;

import daelim.project.eatstagram.service.base.BaseService;
import daelim.project.eatstagram.storage.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Service
@RequiredArgsConstructor
public class DirectMessageService extends BaseService<String, DirectMessageEntity, DirectMessageDTO, DirectMessageRepository> {

    private final StorageRepository storageRepository;
    public static final String DIRECT_MESSAGE_FILE_FOLDER_NAME = "dm";

    public Page<DirectMessageDTO> getDirectMessagePagingList(Pageable pageable, String directMessageRoomId) {
        return getRepository().getDirectMessagePagingList(pageable, directMessageRoomId);
    }

    public ByteBuffer fileSave(String filename, BinaryMessage message) {
        storageRepository.makeFolder(DIRECT_MESSAGE_FILE_FOLDER_NAME);
        String path = storageRepository.getRootLocation().toString() + File.separator + DIRECT_MESSAGE_FILE_FOLDER_NAME + File.separator;
        ByteBuffer byteBuffer = message.getPayload();
        File file = new File(path, filename);
        FileOutputStream out = null;
        FileChannel outChannel = null;

        try {
            byteBuffer.flip(); // byteBuffer 를 읽기 위해서 세팅
            out = new FileOutputStream(file, true); // 생성을 위해 OutputStream 을 연다.
            outChannel = out.getChannel(); // 채널을 열고
            byteBuffer.compact(); // 파일을 복사한다.
            outChannel.write(byteBuffer); // 파일을 쓴다.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (outChannel != null) outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byteBuffer.position(0); // 파일을 저장하면서 position 값이 변경되었으므로 0으로 초기화한다.
        return byteBuffer;
    }
}
