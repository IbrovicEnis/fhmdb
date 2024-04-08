package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class HomeController implements Initializable {

    @FXML
    public JFXButton longestMvTitel;

    @FXML
    private ListView<Movie> movieListView;

    @FXML
    public JFXButton searchBtn;
    @FXML
    public JFXButton sortBtn;
    @FXML
    public TextField searchField;

    @FXML
    public JFXComboBox<String> genreComboBox;
    private boolean ascendingOrder = true;

    public List<Movie> allMovies = Movie.initializeMovies();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private final MovieAPI movieAPI = new MovieAPI();
    private void fetchMoviesAsync() {
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
        observableMovies.clear();
        observableMovies.addAll(movies);
        movieListView.setItems(observableMovies);
    }
        @FXML
    private void handleSortButton(ActionEvent useSort) {
        if (ascendingOrder) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER));
            sortBtn.setText("Sort (desc)");
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
            sortBtn.setText("Sort (asc)");
        }

        ascendingOrder = !ascendingOrder;
    }

    public List<Movie> filterMovies(List<Movie> moviesList, Genres genre, String searchText) {
        if (moviesList == null) {
            return null;
        }
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : moviesList) {
            boolean matchesGenre = genre == null || genre == Genres.ALL || movie.getGenres().contains(genre);
            boolean matchesSearchText = searchText == null || searchText.isEmpty()
                    || movie.getTitle().toLowerCase().contains(searchText.toLowerCase())
                    || movie.getDescription().toLowerCase().contains(searchText.toLowerCase());

            if (matchesGenre && matchesSearchText) {
                filteredMovies.add(movie);
            }
        }

        return filteredMovies;
    }
    public String getStar(List<Movie> filteredMovies) {
        if (filteredMovies == null || filteredMovies.isEmpty()) {
            return null;
        }
        Map<String, Long> actorFrequencyMap = filteredMovies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

        Optional<Map.Entry<String, Long>> mostPopularActorEntry = actorFrequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        return mostPopularActorEntry.map(Map.Entry::getKey).orElse(null);
    }

    public int getLongestMovieTitle(List<Movie> filteredMovies){
        return filteredMovies.stream()
                .mapToInt(movie -> movie.getTitle().replace(" ","").length())
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> filteredMovies, String director) {
        if (filteredMovies == null || filteredMovies.isEmpty()) {
            return 0;
        }
        return filteredMovies.stream()
                .filter(movie -> movie.getDirectors().stream().anyMatch(d -> d.equalsIgnoreCase(director)))
                .count();
    }

    @FXML
    private void handleStar(ActionEvent event) {
        Genres selectedGenre = Genres.valueOf(genreComboBox.getValue());
        List<Movie> filteredMovies = filterMovies(allMovies, selectedGenre, searchField.getText().trim());
        String mostPopularActor = getStar(filteredMovies);
       if (mostPopularActor != null) {
           showStarDialog(mostPopularActor);
       } else {
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("No Movies Found");
           alert.setHeaderText(null);
           alert.setContentText("No movies found.");
           alert.showAndWait();
       }
    }
    private void showStarDialog(String mostPopularActor) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Most popular actor!");
        alert.setHeaderText(null);
        alert.setContentText(mostPopularActor);
        alert.showAndWait();
    }

    private void applyFilters() {
        String selectedGenre = genreComboBox.getValue();
        Genres genreFilter = selectedGenre.equals("ALL") ? Genres.ALL : Genres.valueOf(selectedGenre);
        String searchText = searchField.getText().trim();
        List<Movie> filteredMovies = filterMovies(allMovies, genreFilter, searchText);
        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    private void initializeMovies() {
        try {
            allMovies = movieAPI.getAllMovies(null, null);
            observableMovies.addAll(allMovies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());
        genreComboBox.setItems(FXCollections.observableArrayList("ALL", "ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY",
                "CRIME", "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY",
                "HISTORY", "HORROR", "MUSICAL", "MYSTERY", "ROMANCE",
                "SCIENCE_FICTION", "SPORT", "THRILLER", "WAR", "WESTERN"));
        genreComboBox.setValue("ALL");
        searchBtn.setOnAction(event -> applyFilters());
        sortBtn.setOnAction(this::handleSortButton);

        initializeMovies();
        Platform.runLater(this::fetchMoviesAsync);
    }

    public void handlelongestMvTitel(ActionEvent actionEvent) {
        Genres selectedGenre = Genres.valueOf(genreComboBox.getValue());
        List<Movie> filteredMovies = filterMovies(allMovies, selectedGenre, searchField.getText().trim());
        int movieTitelLength = getLongestMovieTitle(filteredMovies);
        showStarDialog(String.valueOf(movieTitelLength));
    }

    public void handlecountMoviesFrom(ActionEvent actionEvent) {
        Genres selectedGenre = Genres.valueOf(genreComboBox.getValue());
        List<Movie> filteredMovies = filterMovies(allMovies, selectedGenre, searchField.getText().trim());
        long movieCount = countMoviesFrom(filteredMovies,searchField.getText());
        showStarDialog(String.valueOf(movieCount));
    }
}