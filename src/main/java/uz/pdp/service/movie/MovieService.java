package uz.pdp.service.movie;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.daos.movieDao.MovieDao;
import uz.pdp.imageEncoder.FileUploadService;
import uz.pdp.model.Movie;
import uz.pdp.service.BaseService;

import java.io.IOException;
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
        String uploadDir = "C:\\Users\\gayra\\OneDrive\\Desktop\\file\\Cinematica\\src\\main\\resources\\static\\img\\";
        String fileName = posterFile.getOriginalFilename();
        try {
            fileUploadService.saveFile(uploadDir, fileName, posterFile);
            movie.setPosterImage(uploadDir + fileName);
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
