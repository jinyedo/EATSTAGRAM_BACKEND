package daelim.project.eatstagram.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.socket.BinaryMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
@Getter @Setter
public class StorageRepository {

    private final Path rootLocation;

    StorageRepository(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public void init() {
        File uploadPathFolder = new File(String.valueOf(rootLocation));
        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public Path makeFolder(String folderName) {
        File uploadPathFolder = new File(String.valueOf(rootLocation), folderName);
        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return uploadPathFolder.toPath();
    }
}
