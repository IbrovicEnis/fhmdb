package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.services.MovieAPI;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.j256.ormlite.dao.Dao;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import at.ac.fhcampuswien.fhmdb.database.MovieEntity;

import static at.ac.fhcampuswien.fhmdb.database.MovieEntity.*;


public class HomeController implements Initializable {

    @FXML
    public JFXButton longestMvTitel;

    @FXML
    public JFXComboBox<String> releaseYearComboBox;
    @FXML
    public Button openWatch;
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
    @FXML
    private Slider minRatingSlider;
    @FXML
    private Label ratingLabel;
    private DatabaseManager databaseManager = new DatabaseManager();
    private boolean ascendingOrder = true;

    public List<Movie> allMovies = Movie.initializeMovies();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private final MovieAPI movieAPI = new MovieAPI();


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


    public List<Movie> filterMovies(List<Movie> moviesList, Genres genre, String searchText, int minRating, int maxRating) {
        if (moviesList == null) {
            return null;
        }
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : moviesList) {
            boolean matchesGenre = genre == null || genre == Genres.ALL || movie.getGenres().contains(genre);
            boolean matchesDirector = movie.getDirectors().stream()
                    .anyMatch(director -> director.toLowerCase().contains(searchText.toLowerCase()));
            boolean matchesSearchText = searchText == null || searchText.isEmpty()
                    || movie.getTitle().toLowerCase().contains(searchText.toLowerCase())
                    || matchesDirector
                    || movie.getDescription().toLowerCase().contains(searchText.toLowerCase());

            if (matchesGenre && matchesSearchText) {
                filteredMovies.add(movie);
            }
        }
        int startYear;
        int endYear;
        if (searchText != null && searchText.matches("\\d{4}-\\d{4}")) {
            String[] years = searchText.split("-");
            startYear = Integer.parseInt(years[0].trim());
            endYear = Integer.parseInt(years[1].trim());
            return getMoviesBetweenYears(moviesList, startYear, endYear);

        }
        filteredMovies = filteredMovies.stream()
                .filter(movie -> movie.getRating() >= minRating && movie.getRating() <= maxRating)
                .collect(Collectors.toList());

        return filteredMovies;
    }

    public List<String> getStar(List<Movie> filteredMovies) {
        if (filteredMovies == null || filteredMovies.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Long> actorFrequencyMap = filteredMovies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

        Optional<Long> tieCount = actorFrequencyMap.values().stream().max(Long::compareTo);

        return tieCount.map(max -> actorFrequencyMap.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(max))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public long getActorCount(List<Movie> movies, String actor) {
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .filter(mainActor -> mainActor.equals(actor))
                .count();
    }

    public int getLongestMovieTitle(List<Movie> filteredMovies) {
        return filteredMovies.stream()
                .mapToInt(movie -> movie.getTitle().replace(" ", "").length())
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> filteredMovies, String directors) {
        if (filteredMovies == null || filteredMovies.isEmpty()) {
            return 0;
        }
        return filteredMovies.stream()
                .filter(movie -> movie.getDirectors().stream()
                        .anyMatch(d -> d.toLowerCase().contains(directors.toLowerCase())))
                .count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> filteredMovies, int startYear, int endYear) {

        if (filteredMovies == null || filteredMovies.isEmpty()) {
            return filteredMovies;
        }
        return filteredMovies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

    @FXML
    private void handleStar(ActionEvent event) {
        List<String> mostPopularActors = getStar(observableMovies);
        if (!mostPopularActors.isEmpty()) {
            long timesFound = getActorCount(observableMovies, mostPopularActors.get(0)); // Get count for one of the tied actors
            showStarDialog(mostPopularActors, timesFound);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Movies Found");
            alert.setHeaderText(null);
            alert.setContentText("No movies found.");
            alert.showAndWait();
        }
    }

    private void showStarDialog(List<String> actors, long timesFound) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Most popular actor(s)!");
        alert.setHeaderText(null);

        StringBuilder tieDisplay = new StringBuilder();
        tieDisplay.append("Most popular actor(s):").append(System.lineSeparator());
        for (String actor : actors) {
            tieDisplay.append("* ").append(actor).append(System.lineSeparator());
        }

        tieDisplay.append(String.format("They have been in %d movie(s) from our list!", timesFound));

        alert.setContentText(tieDisplay.toString());
        alert.showAndWait();
    }


    public void handleLongestMvTitel(ActionEvent actionEvent) {
        int movieTitelLength = getLongestMovieTitle(observableMovies);
        showLongestMovieTitleDialog(movieTitelLength);

    }

    private void showLongestMovieTitleDialog(long movieTitleLength) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("What length does the longest movie title has ?");
        alert.setHeaderText(null);
        alert.setContentText("The longest Movie title consists of " + movieTitleLength + " words");
        alert.showAndWait();
    }


    public void handleCountMoviesFrom(ActionEvent actionEvent) {
        long movieCount = countMoviesFrom(observableMovies, searchField.getText());
        showDirectorDialog(movieCount);
    }

    private void showDirectorDialog(long movieCount) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How many movies has he directed?");
        alert.setHeaderText(null);
        alert.setContentText("So far he has directed " + movieCount + " movies, that we know of!");
        alert.showAndWait();
    }


    private void updateRatingLabel(int minRating, int maxRating) {
        ratingLabel.setText("Rating: " + minRating + " - " + maxRating);
    }

    private void applyFilters() {
        String selectedGenreValue = genreComboBox.getValue();
        Genres selectedGenre = null;

        if (selectedGenreValue != null && !selectedGenreValue.equals("ALL")) {
            try {
                selectedGenre = Genres.valueOf(selectedGenreValue);
            } catch (IllegalArgumentException ex) {
                System.err.println("Invalid genre: " + selectedGenreValue);
                return;
            }
        }

        String searchText = searchField.getText().trim().isEmpty() ? null : searchField.getText().trim();

        String minRating = String.valueOf((int) minRatingSlider.getValue());
        if (minRatingSlider.getValue() == minRatingSlider.getMin()) {
            minRating = null;
        }

        String selectedReleaseYearValue = releaseYearComboBox.getValue();
        if ("ALL".equals(selectedReleaseYearValue)) {
            selectedReleaseYearValue = null;
        }

        try {
            List<Movie> filteredMovies = movieAPI.getAllMovies(searchText, selectedGenre, selectedReleaseYearValue, minRating);
            observableMovies.clear();
            observableMovies.addAll(filteredMovies);
        } catch (MovieApiException e) {
            e.printStackTrace();
        }
    }

    private void initializeMovies(){
        try {
            if (checkIfMoviesExistInDatabase()) {
                allMovies = loadMoviesFromDatabase();
                System.out.println("Loaded movies from database.");
            } else {
                allMovies = movieAPI.getAllMovies(null, null, null, null);
                saveMoviesToDatabase(allMovies);
                System.out.println("Loaded movies from API and saved to database.");
            }
            observableMovies.addAll(allMovies);
        } catch (SQLException | MovieApiException sqle) {
            showErrorDialog("Initialization Error", sqle.getMessage());
        }
    }

    private boolean checkIfMoviesExistInDatabase() throws SQLException {
        MovieRepository movieRepository = new MovieRepository(databaseManager);
        return movieRepository.getMovieCount() > 0;
    }

    private List<Movie> loadMoviesFromDatabase(){
        MovieRepository movieRepository = new MovieRepository(databaseManager);
        List<MovieEntity> movieEntities = null;
        try {
            movieEntities = movieRepository.getAllMovies();
        } catch (DatabaseException dbe) {
            showErrorDialog("Initialization Error", dbe.getMessage());
        }
        return toMovies(movieEntities);
    }


    private void saveMoviesToDatabase(List<Movie> movies) {
        MovieRepository movieRepository = new MovieRepository(databaseManager);
        for (Movie movie : movies) {
            MovieEntity movieEntity = MovieEntity.fromMovie(movie);
            try {
                movieRepository.addMovie(movieEntity);
            } catch (DatabaseException dbe) {
                showErrorDialog("Error saving Movies to Database", dbe.getMessage());
            }
        }
    }

    private void fillReleaseYear(ComboBox<String> comboBox) {
        new Thread(() -> {
            try {
                List<Movie> movies = new MovieAPI().getAllMovies(null, null, null, null);
                List<String> years = movies.stream()
                        .map(Movie::getReleaseYear)
                        .distinct()
                        .sorted()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                years.add(0, "ALL");
                Platform.runLater(() -> comboBox.setItems(FXCollections.observableArrayList(years)));
            } catch (MovieApiException mae) {
                showErrorDialog("Error getting Movies from API", mae.getMessage());
            }
        }).start();
    }

    private final ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedItem) -> {
        WatchlistRepository watchlist = new WatchlistRepository(databaseManager);
        try {
            boolean isAlreadyInWatchlist = watchlist.isInWatchlist(clickedItem.getApiId());
            if (!isAlreadyInWatchlist) {
                MovieEntity movieEntity = MovieEntity.fromMovie(clickedItem);
                WatchlistMovieEntity watched = new WatchlistMovieEntity(movieEntity.getId(), movieEntity.getApiId());
                watchlist.addToWatchlist(watched);
            } else {
                System.out.println("Movie is already in Watchlist.");
            }

        } catch (DatabaseException dbe) {
            showErrorDialog("Error populating the Database", dbe.getMessage());
        }
    };

    @FXML
    private void openWatchlist() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/watchlist.fxml"));
            Parent newPageRoot = loader.load();
            Stage currentStage = (Stage) openWatch.getScene().getWindow();
            currentStage.setScene(new Scene(newPageRoot, currentStage.getScene().getWidth(), currentStage.getScene().getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            databaseManager.createConnectionSource();
        } catch (DatabaseException dbe) {
            showErrorDialog("Error creating connection to the Database", dbe.getMessage());
        }
        initializeMovies();
        movieListView.setItems(observableMovies); // Direktes Setzen der Filme in die ListView
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked));
        genreComboBox.setItems(FXCollections.observableArrayList("ALL", "ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY",
                "CRIME", "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY",
                "HISTORY", "HORROR", "MUSICAL", "MYSTERY", "ROMANCE",
                "SCIENCE_FICTION", "SPORT", "THRILLER", "WAR", "WESTERN"));
        genreComboBox.setPromptText("Genre");
        fillReleaseYear(releaseYearComboBox);
        releaseYearComboBox.setPromptText("Release Year");
        minRatingSlider.setMin(1);
        minRatingSlider.setMax(10);
        minRatingSlider.setShowTickLabels(true);
        minRatingSlider.setShowTickMarks(true);
        minRatingSlider.setMajorTickUnit(1);
        minRatingSlider.setBlockIncrement(1);

        minRatingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int minRating = newValue.intValue();
            updateRatingLabel(minRating, 10);
            applyFilters();
        });
        updateRatingLabel((int) minRatingSlider.getValue(), 10);

        minRatingSlider.setTooltip(new Tooltip("Move slider to set minimum rating"));

        searchBtn.setOnAction(event -> applyFilters());
        searchField.setOnAction(event -> applyFilters());
        sortBtn.setOnAction(this::handleSortButton);
    }

    private void showErrorDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}