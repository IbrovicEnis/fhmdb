import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
    void test_if_filtering_only_genre_returns_correct_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.CRIME, "");
        assertEquals(1, result.size(), "The number of filtered movies for the genre CRIME should be 1.");
        assertTrue(result.get(0).getGenres().contains(Movie.Genre.CRIME), "The movie should belong to the genre CRIME.");
        assertEquals("The Dark Knight", result.get(0).getTitle(), "The movie Title should be 'The Dark Knight'");
    }

    @Test
    void test_if_filtering_only_text_returns_correct_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ALL, "bending");
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDescription().contains("bending") || result.get(0).getTitle().contains("bendigng"), "The movie should have 'bending' in his description/title");
        assertEquals("Inception", result.get(0).getTitle(), "The movie Title should be 'Inception'");
    }

    @Test
    void test_if_filtering_genre_and_text_returns_correct_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ACTION, "superhero");
        assertEquals(1, result.size());
        assertTrue((result.get(0).getDescription().contains("superhero") ||result.get(0).getTitle().contains("superhero")) &&  result.get(0).getGenres().contains(Movie.Genre.ACTION));
        assertEquals("The Dark Knight", result.get(0).getTitle(), "The movie Title should be 'The Dark Knight'");
    }
    @Test
    void test_sorting_button_by_title() {
            // Ascending order
            controller.allMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER));
            assertEquals("Inception", controller.allMovies.get(0).getTitle());
            assertEquals("Parasite", controller.allMovies.get(1).getTitle());
            assertEquals("The Dark Knight", controller.allMovies.get(2).getTitle());

            // Descending order
            controller.allMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
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
    void test_filtering_with_invalid_input() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ACTION, "invalid text");
        assertEquals(0, result.size(), "The number of filtered movies should be 0 when the search text does not match any movie.");
    }
    @Test
    void test_filtering_is_case_insensitive() {
        List<Movie> result = controller.filterMovies(controller.allMovies, null, "inception");
        assertEquals(1, result.size(), "Filtering should be case insensitive.");
    }
    @Test
    void test_sorting_filtered_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ACTION, "");
        result.sort(Comparator.comparing(Movie::getTitle));
        assertEquals("Inception", result.get(0).getTitle(), "Inception should be first after sorting filtered list.");
    }
    @Test
    void test_sorting_filtered_movies_reversed() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ACTION, "");
        result.sort(Comparator.comparing(Movie::getTitle).reversed());
        assertEquals("The Dark Knight", result.get(0).getTitle(), "The Dark Knight should be first after sorting filtered list.");
    }
    @Test
    void test_movie_constructor_title_null() {
        assertThrows(IllegalArgumentException.class, () -> new Movie(null, "description", Arrays.asList(Movie.Genre.ACTION)));
    }

    @Test
    void test_movie_constructor_genres_null() {
        assertThrows(IllegalArgumentException.class, () -> new Movie("title", "description", null));
    }
    @Test
    void test_equals_with_same_movies() {
        Movie movie1 = new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        Movie movie2 = new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        assertTrue(movie1.equals(movie2), "It should be considered equal");
    }
    @Test
    void test_equals_with_null() {
        Movie movie1 = new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        assertFalse(movie1.equals(null), "It should not be considered equal");
    }
    @Test
    void test_equals_with_different_titles() {
        Movie movie1 = new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        Movie movie2 = new Movie("The Dark Knight", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        assertFalse(movie1.equals(movie2), "It should not be considered equal");
    }
    @Test
    void test_equals_with_different_description() {
        Movie movie1 = new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        Movie movie2 = new Movie("Inception", "A superhero action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        assertFalse(movie1.equals(movie2), "It should not be considered equal");
    }
    @Test
    void test_equals_with_different_genres() {
        Movie movie1 = new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY));
        Movie movie2 = new Movie("Inception", "A mind-bending action-adventure", Arrays.asList(Movie.Genre.ACTION));
        assertFalse(movie1.equals(movie2), "It should not be considered equal");
    }
    @Test
    void test_GetGenresAsString() {
        String genres = controller.allMovies.get(0).getGenresAsString();
        assertEquals("ACTION, FANTASY", genres, "Output should be: ACTION, FANTASY but was: "+ genres);
    }
}