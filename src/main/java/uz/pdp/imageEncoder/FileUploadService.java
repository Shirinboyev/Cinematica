package uz.pdp.imageEncoder;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUploadService {

    public void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        String filePath = uploadDir + fileName;
        File file = new File(filePath);
        multipartFile.transferTo(file);
    }
}