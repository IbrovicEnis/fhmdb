package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import com.jfoenix.controls.JFXListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {
    @FXML
    private BorderPane mainPane;
    @FXML
    public JFXListView watchlistView;

    private WatchlistRepository watchlistRepository;

    protected ObservableList<WatchlistMovieEntity> observableWatchlist = FXCollections.observableArrayList();


    @FXML
    private void navigateToWatchlistView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/watchlist.fxml"));
            loader.setController(this);
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private final ClickEventHandler onRemoveFromWatchlistClicked = (o) -> {
        if (o instanceof WatchlistMovieEntity) {
            WatchlistMovieEntity watchlistMovieEntity = (WatchlistMovieEntity) o;

            try {
                WatchlistRepository watchlistRepository = WatchlistRepository.getInstance();
                watchlistRepository.removeFromWatchlist(watchlistMovieEntity);
                observableWatchlist.remove(watchlistMovieEntity);
            } catch (DataBaseException e) {
                UserDialog dialog = new UserDialog("Database Error", "Could not remove movie from watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navigateToWatchlistView();
        List<WatchlistMovieEntity> watchlist = new ArrayList<>();
        try {
            watchlistRepository = WatchlistRepository.getInstance();
            watchlist = getWatchlist();
            observableWatchlist.addAll(getWatchlist());
            watchlistView.setItems(observableWatchlist);
            watchlistView.setCellFactory(movieListView -> new WatchlistCell(onRemoveFromWatchlistClicked));

        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("Database Error", "Could not read movies from DB");
            dialog.show();
            e.printStackTrace();
        }

        if(watchlist.size() == 0) {
            watchlistView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }


        System.out.println("WatchlistController initialized");
    } private List<WatchlistMovieEntity> getWatchlist() throws DataBaseException {
        return watchlistRepository.readWatchlist();
    }
}
    /*void initializeWatchlist() {
        // Load watchlist data from the database
        List<WatchlistMovieEntity> watchlist = new ArrayList<>();
        try {
            watchlistRepository = WatchlistRepository.getInstance();
            watchlist = getWatchlist();
            observableWatchlist.addAll(watchlist);
            watchlistView.setItems(observableWatchlist);
        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("Database Error", "Could not read movies from DB");
            dialog.show();
            e.printStackTrace();
        }

        if (watchlist.isEmpty()) {
            watchlistView.setPlaceholder(new Label("Watchlist is empty"));
        }
    }*/

