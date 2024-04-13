package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.control.Tooltip;


public class HomeController implements Initializable {

    @FXML
    public JFXButton longestMvTitel;
    @FXML
    public TextField startingYears;
    @FXML
    public TextField endingYears;
    @FXML
    public Button filterYears;
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
    private Slider minRatingSlider, maxRatingSlider;
    @FXML
    private Label ratingLabel;



    private boolean ascendingOrder = true;

    public List<Movie> allMovies = Movie.initializeMovies();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private final MovieAPI movieAPI = new MovieAPI();

    private void fetchMoviesAsync() {
        Thread fetchThread = new Thread(() -> {
            MovieAPI movieAPI = new MovieAPI();
            try {
                List<Movie> movies = movieAPI.getAllMovies(null, null, null, null);
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
    @FXML
    private void handleStar(ActionEvent event) {
        Genres selectedGenre = Genres.valueOf(genreComboBox.getValue());
        int minRating = (int) minRatingSlider.getMin();
        int maxRating = (int) maxRatingSlider.getMax();
        List<Movie> filteredMovies = filterMovies(allMovies, selectedGenre, searchField.getText().trim(), minRating, maxRating);
        List<String> mostPopularActors = getStar(filteredMovies);
        if (!mostPopularActors.isEmpty()) {
            long timesFound = getActorCount(filteredMovies, mostPopularActors.get(0)); // Get count for one of the tied actors
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

        tieDisplay.append(String.format("Has been in %d movies!", timesFound));

        alert.setContentText(tieDisplay.toString());
        alert.showAndWait();
    }
    private long getActorCount(List<Movie> movies, String actor) {
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
    //Added showDirectorDialog to Enis´method and lost his name
    public void handleCountMoviesFrom(ActionEvent actionEvent) {
        Genres selectedGenre = Genres.valueOf(genreComboBox.getValue());
        int minRating = (int) minRatingSlider.getMin();
        int maxRating = (int) maxRatingSlider.getMax();
        List<Movie> filteredMovies = filterMovies(allMovies, selectedGenre, searchField.getText().trim(), minRating, maxRating);
        long movieCount = countMoviesFrom(filteredMovies, searchField.getText());
        showDirectorDialog(movieCount);
    }
    private void showDirectorDialog(long movieCount){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How many movies has he directed?");
        alert.setHeaderText(null);
        alert.setContentText("So far he has directed " + movieCount + " movies, that we know of!");
        alert.showAndWait();
    }
    //Cinan´s method
    public List<Movie> getMoviesBetweenYears(List<Movie> filteredMovies, int startYear, int endYear) {

        if (filteredMovies == null || filteredMovies.isEmpty()) {
            return filteredMovies;
        }
        return filteredMovies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }
    private void updateRatingLabel(int minRating, int maxRating) {
        ratingLabel.setText("Rating: " + minRating + " - " + maxRating);
    }
    private void applyFilters() {
        String selectedGenre = genreComboBox.getValue();
        Genres genreFilter = selectedGenre.equals("ALL") ? Genres.ALL : Genres.valueOf(selectedGenre);
        String searchText = searchField.getText().trim();
        int minRating = (int) minRatingSlider.getValue();
        int maxRating = (int) maxRatingSlider.getValue();
        List<Movie> filteredMovies = filterMovies(allMovies, genreFilter, searchText, minRating, maxRating);
        observableMovies.clear();
        if (startingYears != null && endingYears != null &&
                !startingYears.getText().trim().isEmpty() && !endingYears.getText().trim().isEmpty()) {
            try {
                int startYear = Integer.parseInt(startingYears.getText().trim());
                int endYear = Integer.parseInt(endingYears.getText().trim());
                observableMovies.addAll(getMoviesBetweenYears(filteredMovies, startYear, endYear));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            observableMovies.addAll(filteredMovies);
        }

    }

    private void initializeMovies() {
        try {
            allMovies = movieAPI.getAllMovies(null, null, null, null);
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

        minRatingSlider.setMin(1);
        minRatingSlider.setMax(10);
        minRatingSlider.setShowTickLabels(true);
        minRatingSlider.setShowTickMarks(true);minRatingSlider.setMajorTickUnit(1);
        minRatingSlider.setBlockIncrement(1);

        maxRatingSlider.setMin(1);
        maxRatingSlider.setMax(10);
        maxRatingSlider.setShowTickLabels(true);
        maxRatingSlider.setShowTickMarks(true);
        maxRatingSlider.setMajorTickUnit(1);
        maxRatingSlider.setBlockIncrement(1);

        minRatingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int minRating = newValue.intValue();
            updateRatingLabel(minRating, (int) maxRatingSlider.getValue());
            applyFilters();
        });
        maxRatingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int maxRating = newValue.intValue();
            updateRatingLabel((int) minRatingSlider.getValue(), maxRating);
            applyFilters();
        });
        updateRatingLabel((int) minRatingSlider.getValue(), (int) maxRatingSlider.getValue());

        minRatingSlider.setTooltip(new Tooltip("Move slider to set minimum rating"));
        maxRatingSlider.setTooltip(new Tooltip("Move slider to set maximum rating"));

        searchBtn.setOnAction(event -> applyFilters());
        searchField.setOnAction(event -> applyFilters());
        filterYears.setOnAction(event -> applyFilters());
        sortBtn.setOnAction(this::handleSortButton);

        initializeMovies();
        Platform.runLater(this::fetchMoviesAsync);
    }

    public void handleLongestMvTitel(ActionEvent actionEvent) {
        Genres selectedGenre = Genres.valueOf(genreComboBox.getValue());
        int minRating = (int) minRatingSlider.getMin();
        int maxRating = (int) maxRatingSlider.getMax();
        List<Movie> filteredMovies = filterMovies(allMovies, selectedGenre, searchField.getText().trim(),minRating,maxRating);
        int movieTitelLength = getLongestMovieTitle(filteredMovies);
        showDirectorDialog(movieTitelLength);

    }


}