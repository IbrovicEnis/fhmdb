package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.patterns.Observer;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static at.ac.fhcampuswien.fhmdb.database.MovieEntity.fromMovie;

public class WatchlistController implements Initializable, Observer {
    @FXML
    public JFXListView<MovieEntity> movieListView;
    public List<MovieEntity> allMoviesEntity = new ArrayList<>();
    private final ObservableList<MovieEntity> observableMovies = FXCollections.observableArrayList();
    private final DatabaseManager databaseManager = new DatabaseManager();
    ;
    @FXML
    public Button openHome;


    @Override
    public void update(String message) {
        System.out.println("WatchlistController received update: " + message);
        // Handle the update accordingly, like refreshing the watchlist view
    }

    private void initializeMovies() throws DatabaseException {
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
    }

    @FXML
    private void openHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml"));
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
        } catch (DatabaseException dbe) {
            showErrorDialog("Error removing from Database", dbe.getMessage());
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            databaseManager.createConnectionSource();
            initializeMovies();
        } catch (DatabaseException dbe) {
            showErrorDialog("Error creating connection to the Database", dbe.getMessage());
        }

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new WatchlistCell(onRemoveFromWatchlistClicked));

    }

    private void showErrorDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}