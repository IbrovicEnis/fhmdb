package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
public class MovieRepository {
    private Dao<MovieEntity, Long> movieDao;

    public MovieRepository(DatabaseManager dbManager) {
        this.movieDao = dbManager.getMovieDao();
    }
    public List<MovieEntity> getAllMovies() throws SQLException {
        return movieDao.queryForAll();
    }

    public MovieEntity getMovie(long id) throws SQLException {
        return movieDao.queryForId(id);
    }

    public void addMovie(MovieEntity movie) throws SQLException {
        movieDao.create(movie);
    }

    public void deleteMovie(long id) throws SQLException {
        movieDao.deleteById(id);
    }
}
