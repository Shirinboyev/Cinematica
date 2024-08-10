package uz.pdp.imageEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageEncoder {
    public static String encodeImage(String imagePath) throws IOException {
        if (Files.exists(Paths.get(imagePath))) {
            try {
                byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
                return Base64.getEncoder().encodeToString(imageBytes);
            } catch (IOException e) {
                System.err.println("Error reading image file: " + imagePath);
                e.printStackTrace();
                throw e;
            }
        } else {
            System.err.println("File not found: " + imagePath);
            throw new IOException("File not found: " + imagePath);
        }
    }
}
