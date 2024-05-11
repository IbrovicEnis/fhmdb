package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;


import java.sql.SQLException;
import java.util.List;
public class MovieRepository {
    private Dao<MovieEntity, Long> movieDao;

    public MovieRepository(DatabaseManager dbManager) {
        this.movieDao = dbManager.getMovieDao();
    }
    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return movieDao.queryForAll();
        } catch (SQLException sqle) {
            throw new DatabaseException("Failed to retrieve all movies from the database", sqle);
        }
    }

    public MovieEntity getMovie(long id) throws DatabaseException {
        try {
            return movieDao.queryForId(id);
        } catch (SQLException sqle) {
            throw new DatabaseException("Failed to retrieve the specific movie from the database", sqle);
        }
    }

    public void addMovie(MovieEntity movie) throws DatabaseException {
        try {
            movieDao.create(movie);
        } catch (SQLException sqle) {
            throw new DatabaseException("Failed to add the movie to the database", sqle);
        }

    }
    public MovieEntity findMovieByApiId(String ApiId) throws DatabaseException {
        try {
            List<MovieEntity> movies = movieDao.queryBuilder()
                    .where()
                    .eq("ApiId", ApiId)
                    .query();
            return movies.isEmpty() ? null : movies.get(0);
        } catch (SQLException sqle){
            throw new DatabaseException("Failed to find movie in the database", sqle);
        }
    }

}
