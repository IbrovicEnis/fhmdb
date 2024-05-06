package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:h2:mem:fhmdb";
    private ConnectionSource connectionSource;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;
    private Dao<MovieEntity, Long> movieDao;

    public void createConnectionSource() throws Exception {
        connectionSource = new JdbcConnectionSource(DATABASE_URL, "username", "password");
        watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
        movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);

    }
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }
    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }
    public void closeConnection() throws Exception {
        if (connectionSource != null) {
            connectionSource.close();
        }
    }
}
