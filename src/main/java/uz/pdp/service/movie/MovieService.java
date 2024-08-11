package uz.pdp.service.movie;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.daos.movieDao.MovieDao;
import uz.pdp.imageEncoder.FileUploadService;
import uz.pdp.model.Movie;
import uz.pdp.service.BaseService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class MovieService implements BaseService<Movie> {


    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao, FileUploadService fileUploadService) {
        this.movieDao = movieDao;
        this.fileUploadService = fileUploadService;
    }

    private final FileUploadService fileUploadService;


    public void addMovie(Movie movie, MultipartFile posterFile) {
        String uploadDir = "src/main/resources/static/img/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        String fileName = posterFile.getOriginalFilename();
        File destinationFile = new File(uploadDir + fileName);

        try (InputStream inputStream = posterFile.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            movie.setPosterImage("/static/img/" + fileName);
            movieDao.save(movie);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
