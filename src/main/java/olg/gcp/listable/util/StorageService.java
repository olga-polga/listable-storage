package olg.gcp.listable.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
        String testMe();
        String saveFile(MultipartFile filePart) throws IOException;
}
