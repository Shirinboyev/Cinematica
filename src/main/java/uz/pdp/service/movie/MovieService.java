package uz.pdp.service.movie;

import org.springframework.stereotype.Service;
import uz.pdp.daos.movieDao.MovieDao;
import uz.pdp.model.Movie;
import uz.pdp.service.BaseService;

import java.io.IOException;
import java.util.List;

@Service
public class MovieService implements BaseService<Movie> {
    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
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
