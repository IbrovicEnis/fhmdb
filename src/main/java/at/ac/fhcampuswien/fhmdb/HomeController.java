package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<Movie.Genre> genreComboBox;

    @FXML
    public JFXButton sortBtn;
    private boolean ascendingOrder = true; // Variable to track sorting order

    public List<Movie> allMovies = Movie.initializeMovies();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    @FXML
    private void handleSortButton(ActionEvent event) {
        if (ascendingOrder) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER));
            sortBtn.setText("Sort (desc)");
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
            sortBtn.setText("Sort (asc)");
        }

        ascendingOrder = !ascendingOrder; // Toggle the sorting order
    }

    private void filterMovies() {

        Movie.Genre selectedGenre = genreComboBox.getValue();
        String searchText = searchField.getText().trim().toLowerCase();

        ArrayList<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : allMovies) {
            boolean matchesGenre = selectedGenre == null || movie.getGenres().contains(selectedGenre);
            boolean matchesSearchText = searchText.isEmpty() || movie.getTitle().toLowerCase().contains(searchText);

            if (matchesGenre && matchesSearchText) {
                filteredMovies.add(movie);
            }
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        observableMovies.addAll(allMovies);
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());

        genreComboBox.getItems().addAll(Movie.Genre.values());
        genreComboBox.setPromptText("Filter by Genre");

        searchBtn.setOnAction(event -> filterMovies());

    }
}


/*   Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {
                // TODO sort observableMovies ascending
                sortBtn.setText("Sort (desc)");
            } else {
                // TODO sort observableMovies descending
                sortBtn.setText("Sort (asc)");
            }
        });
*/