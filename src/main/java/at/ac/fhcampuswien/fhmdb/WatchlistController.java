package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistCell;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static at.ac.fhcampuswien.fhmdb.database.MovieEntity.fromMovie;

public class WatchlistController implements Initializable {
    @FXML
    public JFXListView<MovieEntity> movieListView;
    public List<MovieEntity> allMoviesEntity = new ArrayList<>();
    private final ObservableList<MovieEntity> observableMovies = FXCollections.observableArrayList();
    private final DatabaseManager databaseManager = new DatabaseManager();
    ;
    @FXML
    public Button openHome;

    private void initializeMovies() {
        try {
            WatchlistRepository watchlist = new WatchlistRepository(databaseManager);
            MovieRepository movieRepository = new MovieRepository(databaseManager);
            List<WatchlistMovieEntity> moviesInWatchList = watchlist.getWatchlist();

            for (WatchlistMovieEntity movie : moviesInWatchList) {
                MovieEntity movieEntity = movieRepository.findMovieByApiId(movie.getApiId());
                if (movieEntity != null) {
                    allMoviesEntity.add(movieEntity);
                }
            }
            observableMovies.setAll(allMoviesEntity);
        } catch (Exception e) {
        }
    }

    @FXML
    private void openHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Parent newPageRoot = loader.load();
            Stage currentStage = (Stage) openHome.getScene().getWindow();
            currentStage.setScene(new Scene(newPageRoot, currentStage.getScene().getWidth(), currentStage.getScene().getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ClickEventHandler<MovieEntity> onRemoveFromWatchlistClicked = (clickedItem) -> {
        WatchlistRepository watchlist = new WatchlistRepository(databaseManager);
        try {
            watchlist.removeFromWatchlist(clickedItem.getApiId());
            Platform.runLater(() -> {
                allMoviesEntity.removeIf(movie -> movie.getApiId().equals(clickedItem.getApiId()));
                observableMovies.setAll(allMoviesEntity);
            });
        } catch (DatabaseException e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Entfernen aus der Watchlist: ", e);
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            databaseManager.createConnectionSource();
            initializeMovies();
            movieListView.setItems(observableMovies);
            movieListView.setCellFactory(movieListView -> new WatchlistCell(onRemoveFromWatchlistClicked));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler bei der Initialisierung der Datenverbindung", e);
        }
    }
}