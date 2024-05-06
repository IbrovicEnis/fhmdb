package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public WatchlistRepository(DatabaseManager dbManager) {
        this.watchlistDao = dbManager.getWatchlistDao();
    }
    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return watchlistDao.queryForAll();
    }

    public void addToWatchlist(WatchlistMovieEntity movie) throws SQLException {
        watchlistDao.createIfNotExists(movie);
    }

    public void removeFromWatchlist(long id) throws SQLException {
        watchlistDao.deleteById(id);
    }
}
