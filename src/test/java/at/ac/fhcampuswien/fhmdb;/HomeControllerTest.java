import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


public class HomeControllerTest {

    HomeController controller;

    @BeforeEach
    public void create_list_to_test() {
        controller = new HomeController();

        controller.allMovies = Arrays.asList(
                new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY)),
                new Movie("The Dark Knight", "A superhero action-adventure", Arrays.asList(Movie.Genre.CRIME, Movie.Genre.ACTION)),
                new Movie("Parasite", "A dark twist on a family's dream for a better life", Arrays.asList(Movie.Genre.THRILLER, Movie.Genre.DRAMA))
        );
    }


    //Test was implemented because there were initial difficulties with duplicate entries, etc., during filtering.
    @Test
    void test_if_filtering_genre_returns_correct_amount_of_movies()
    {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ACTION, "");
        assertEquals(2, result.size(), "The number of filtered movies for the genre ACTION should be 2.");
    }

    @Test
    void test_if_filtering_only_genre_returns_correct_movies_that_contain_that_genre() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.CRIME, "");
        assertEquals(1, result.size(), "The number of filtered movies for the genre CRIME should be 1.");
        assertTrue(result.get(0).getGenres().contains(Movie.Genre.CRIME), "The movie should belong to the genre CRIME.");
    }


    @Test
    void test_if_filtering_only_text_returns_correct_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ALL, "superhero");
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDescription().contains("superhero") ||result.get(0).getTitle().contains("superhero") );
    }

    @Test
    void test_if_filtering_genre_and_text_returns_correct_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ACTION, "superhero");
        assertEquals(1, result.size());
        assertTrue((result.get(0).getDescription().contains("superhero") ||result.get(0).getTitle().contains("superhero")) &&  result.get(0).getGenres().contains(Movie.Genre.ACTION));
    }
    @Test
    void test_sorting_button_by_title() {

            // Sort allMovies in ascending order
            controller.allMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER));

            // Check the order of the items in allMovies
            assertEquals("Inception", controller.allMovies.get(0).getTitle());
            assertEquals("Parasite", controller.allMovies.get(1).getTitle());
            assertEquals("The Dark Knight", controller.allMovies.get(2).getTitle());

            // Sort allMovies in descending order
            controller.allMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());

            // Check the order of the items in allMovies
            assertEquals("The Dark Knight", controller.allMovies.get(0).getTitle());
            assertEquals("Parasite", controller.allMovies.get(1).getTitle());
            assertEquals("Inception", controller.allMovies.get(2).getTitle());
        }
    @Test
    void test_filtering_with_empty_input() {
        List<Movie> result = controller.filterMovies(new ArrayList<>(), Movie.Genre.ACTION, "");
        assertEquals(0, result.size(), "The number of filtered movies should be 0 when the input list is empty.");
    }
    @Test
    void test_filtering_if_null_input() {
        List<Movie> result = controller.filterMovies(null, Movie.Genre.ACTION, "");
        assertNull(result, "The result should be null when the input list is null.");
    }
    @Test
    void test_filtering_with_single_movie() {
        List<Movie> singleMovieList = Collections.singletonList(new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY)));
        List<Movie> result = controller.filterMovies(singleMovieList, Movie.Genre.ACTION, "");
        assertEquals(1, result.size(), "The number of filtered movies should be 1 when the input list contains a single movie of the specified genre.");
    }
    @Test
    void test_filtering_with_invalid_input() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ACTION, "invalid text");
        assertEquals(0, result.size(), "The number of filtered movies should be 0 when the search text does not match any movie.");
    }
   /*@Test
    void test_initialize() {
        controller.allMovies = HomeController.initializeMovies();
        controller.initialize(null, null);
        assertEquals(controller.allMovies.size(), controller.observableMovies.size());
        assertEquals(controller.observableMovies, controller.movieListView.getItems());
        assertEquals(Arrays.asList(Movie.Genre.values()), controller.genreComboBox.getItems());
        assertNotNull(controller.searchBtn.getOnAction());
    }
    @Test
    void test_updateItem() {
        MovieCell cell = new MovieCell();
        Movie movie = new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        cell.updateItem(movie, false);
        assertEquals("Inception", cell.titleLabel.getText());
        assertEquals("A mind-bending action-adventure", cell.descriptionLabel.getText());
        assertEquals("[ACTION, FANTASY]", cell.genreLabel.getText());
    }*/
}