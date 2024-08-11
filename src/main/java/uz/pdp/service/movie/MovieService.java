package uz.pdp.service.movie;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.daos.movieDao.MovieDao;
import uz.pdp.model.Movie;
import uz.pdp.service.BaseService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class MovieService implements BaseService<Movie> {


    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }


    private final Path rootPath = Path.of("C:\\Users\\gayra\\OneDrive\\Desktop\\file\\Cinematica\\src\\main\\resources\\static\\img");

/*    public void addMovie( MultipartFile posterFile) throws IOException {
        String originalFilename = posterFile.getOriginalFilename();
        String mimeType = StringUtils.getFilenameExtension(originalFilename);
        String generatedFileName = UUID.randomUUID() + "." + mimeType;
        String extension = "/static/img/" + generatedFileName;
        Files.copy(posterFile.getInputStream(),rootPath.resolve(generatedFileName), StandardCopyOption.REPLACE_EXISTING);
        Movie movie = new Movie();
        movie.setTitle(originalFilename);

    }*/
public void addMovie(Movie movie, MultipartFile posterFile) throws IOException {
    String originalFilename = posterFile.getOriginalFilename();

    if (originalFilename == null || originalFilename.isEmpty()) {
        throw new IllegalArgumentException("File name is invalid");
    }

    // Get the file extension
    String extension = StringUtils.getFilenameExtension(originalFilename);
    if (extension == null) {
        throw new IllegalArgumentException("File extension is invalid");
    }

    // Generate a unique filename
    String generatedFileName = UUID.randomUUID() + "." + extension;

    // Define the path where the file will be saved
    Path destinationPath = rootPath.resolve(generatedFileName);

    // Ensure the directory exists
    Files.createDirectories(rootPath);

    // Copy the file to the target location
    try (InputStream inputStream = posterFile.getInputStream()) {
        Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
        throw new IOException("Failed to save the file", e);
    }

    // Save only the filename in the database
    movie.setPosterImage(generatedFileName);
    movieDao.save(movie);
}

    @Override
    public void save(Movie entity) throws IOException {
        movieDao.save(entity);
    }

    @Override
    public void update(Movie entity) {
        movieDao.update(entity);
    }

    @Override
    public void delete(int entity) {
        movieDao.delete(entity);
    }

    @Override
    public Movie getById(int id) {
        return movieDao.getById(id);
    }

    @Override
    public List<Movie> getAll() {
        return movieDao.getAll();
    }

}