package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;


import java.sql.SQLException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:h2:mem:fhmdb";
    private ConnectionSource connectionSource;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;
    private Dao<MovieEntity, Long> movieDao;

    public void createConnectionSource() throws DatabaseException {
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL, "username", "password");
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
        } catch (SQLException sqle) {
            throw new DatabaseException("Failed to create connection and initialize tables", sqle);
        }
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
    public void closeConnection() throws DatabaseException {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (Exception e) {
                throw new DatabaseException("Failed to close database connection", e);
            }
        }
    }
}
