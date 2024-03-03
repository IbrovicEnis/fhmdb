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
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;
    private boolean ascendingOrder = true; // Variable to track sorting order
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




    public List<Movie> allMovies = Movie.initializeMovies();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    @FXML
    private void handleFilterByGenre(ActionEvent event) {
        Movie.Genre selectedGenre = (Movie.Genre) genreComboBox.getValue();
        String searchText = searchField.getText().trim().toLowerCase();

        // Use a Set to store unique movies based on filtering criteria
        Set<Movie> uniqueMovies = new HashSet<>();

        for (Movie movie : allMovies) {
            if ((selectedGenre == null || movie.getGenres().contains(selectedGenre))
                    && (searchText.isEmpty() || movie.getTitle().toLowerCase().contains(searchText))) {
                uniqueMovies.add(movie);
            }
        }

        // Clear the observableMovies list before adding the filtered movies
        observableMovies.clear();
        observableMovies.addAll(uniqueMovies);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies); // Add dummy data to observable list
        movieListView.setItems(observableMovies); // Set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // Use custom cell factory to display data

        // Add genre filter items to combo box
        genreComboBox.getItems().addAll(Movie.Genre.values());
        genreComboBox.setPromptText("Filter by Genre");

        // Set event handler for combo box selection change
        genreComboBox.setOnAction(event -> handleFilterByGenre((ActionEvent) event));
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