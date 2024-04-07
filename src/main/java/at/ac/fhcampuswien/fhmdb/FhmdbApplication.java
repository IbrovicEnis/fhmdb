package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @FXML
    private ListView<Movie> movieListView;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

   /* private void fetchMoviesAsync() {
        Thread fetchThread = new Thread(() -> {
            MovieAPI movieAPI = new MovieAPI();
            try {
                List<Movie> movies = movieAPI.getAllMovies(null, null);
                Platform.runLater(() -> updateUIWithMovies(movies));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fetchThread.start();
    }


    private void updateUIWithMovies(List<Movie> movies) {
        ObservableList<Movie> observableMovies = FXCollections.observableArrayList(movies);
        movieListView.setItems(observableMovies);
    }

*/
}