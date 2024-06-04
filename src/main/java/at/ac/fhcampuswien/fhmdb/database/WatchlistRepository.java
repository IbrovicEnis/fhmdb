package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.patterns.Observable;
import at.ac.fhcampuswien.fhmdb.patterns.Observer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable {
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    private List<Observer> observers = new ArrayList<>();

    private List<Movie> watchlist;

    public WatchlistRepository() {
        this.observers = new ArrayList<>();
        this.watchlist = new ArrayList<>();
    }


    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
    public WatchlistRepository(DatabaseManager dbManager) {
        this.watchlistDao = dbManager.getWatchlistDao();
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return watchlistDao.queryForAll();
        } catch (SQLException sqle) {
            throw new DatabaseException("Failed to retrieve watchlist from database", sqle);
        }
    }

    public void addToWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            watchlistDao.createIfNotExists(movie);
            notifyObservers("Movie added to watchlist!");
        } catch (SQLException sqle) {
            throw new DatabaseException("Failed to add movie to watchlist", sqle);
        }
    }

    public void removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = watchlistDao.deleteBuilder();
            deleteBuilder.where().eq("apiId", apiId);
            deleteBuilder.delete();
            notifyObservers("Movie removed from watchlist!");
        } catch (SQLException sqle) {
            throw new DatabaseException("Failed to remove movie from watchlist", sqle);
        }
    }

    public boolean isInWatchlist(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
            queryBuilder.where().eq("apiId", apiId);
            return !watchlistDao.query(queryBuilder.prepare()).isEmpty();
        } catch (SQLException sqle) {
            throw new DatabaseException("Failed to check if movie is in watchlist", sqle);
        }
    }
}
