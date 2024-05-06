package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

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

    public void removeFromWatchlist(String apiId) throws SQLException {
        DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = watchlistDao.deleteBuilder();
        deleteBuilder.where().eq("apiId", apiId);
        deleteBuilder.delete();
    }
    public boolean isInWatchlist(String apiId) throws SQLException {
        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
        queryBuilder.where().eq("apiId", apiId);
        return watchlistDao.query(queryBuilder.prepare()).size() > 0;
    }
}
