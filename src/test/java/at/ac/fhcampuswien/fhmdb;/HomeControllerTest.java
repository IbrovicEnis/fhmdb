import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.Genres;
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
                new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page")),
                new Movie("The Dark Knight", "", "A superhero action-adventure", Arrays.asList(Genres.CRIME, Genres.ACTION), Arrays.asList("Christopher Nolan"), 2008, 9.0, Arrays.asList("Christian Bale", "Heath Ledger", "Aaron Eckhart")),
                new Movie("The Godfather", "", "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.", Arrays.asList(Genres.DRAMA), Arrays.asList("Francis Ford Coppola"), 1972, 9.2, Arrays.asList("Marlon Brando", "Al Pacino", "James Caan")),
                new Movie("Once Upon a Time in Hollywood", "", "A faded television actor and his stunt double strive to achieve fame.", Arrays.asList(Genres.COMEDY, Genres.DRAMA), Arrays.asList("Quentin Tarantino"), 2019, 7.7, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"))

        );
    }

    //Test was implemented because there were initial difficulties with duplicate entries, etc., during filtering.
    @Test
    void test_if_filtering_genre_returns_correct_amount_of_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Genres.ACTION, "", 0, 9999);
        assertEquals(2, result.size(), "The number of filtered movies for the genre ACTION should be 2.");
    }

    @Test
    void test_if_filtering_only_genre_returns_correct_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Genres.CRIME, "", 0, 9999);
        assertEquals(1, result.size(), "The number of filtered movies for the genre CRIME should be 1.");
        assertTrue(result.get(0).getGenres().contains(Genres.CRIME), "The movie should belong to the genre CRIME.");
        assertEquals("The Dark Knight", result.get(0).getTitle(), "The movie Title should be 'The Dark Knight'");
    }

    @Test
    void test_if_filtering_only_text_returns_correct_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Genres.ALL, "bending", 0, 9999);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDescription().contains("bending") || result.get(0).getTitle().contains("bendigng"), "The movie should have 'bending' in his description/title");
        assertEquals("Inception", result.get(0).getTitle(), "The movie Title should be 'Inception'");
    }

    @Test
    void test_if_filtering_genre_and_text_returns_correct_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Genres.ACTION, "superhero", 0, 9999);
        assertEquals(1, result.size());
        assertTrue((result.get(0).getDescription().contains("superhero") || result.get(0).getTitle().contains("superhero")) && result.get(0).getGenres().contains(Genres.ACTION));
        assertEquals("The Dark Knight", result.get(0).getTitle(), "The movie Title should be 'The Dark Knight'");
    }

    @Test
    void test_sorting_button_by_title() {
        // Ascending order
        controller.allMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER));
        assertEquals("Inception", controller.allMovies.get(0).getTitle());
        assertEquals("Once Upon a Time in Hollywood", controller.allMovies.get(1).getTitle());
        assertEquals("The Dark Knight", controller.allMovies.get(2).getTitle());
        assertEquals("The Godfather", controller.allMovies.get(3).getTitle());

        // Descending order
        controller.allMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
        assertEquals("The Godfather", controller.allMovies.get(0).getTitle());
        assertEquals("The Dark Knight", controller.allMovies.get(1).getTitle());
        assertEquals("Once Upon a Time in Hollywood", controller.allMovies.get(2).getTitle());
        assertEquals("Inception", controller.allMovies.get(3).getTitle());
    }

    @Test
    void test_filtering_with_empty_input() {
        List<Movie> result = controller.filterMovies(new ArrayList<>(), Genres.ACTION, "", 0, 0);
        assertEquals(0, result.size(), "The number of filtered movies should be 0 when the input list is empty.");
    }

    @Test
    void test_filtering_if_null_input() {
        List<Movie> result = controller.filterMovies(null, Genres.ACTION, "", 0, 0);
        assertNull(result, "The result should be null when the input list is null.");
    }

    @Test
    void test_filtering_with_invalid_input() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Genres.ACTION, "invalid text", 0, 0);
        assertEquals(0, result.size(), "The number of filtered movies should be 0 when the search text does not match any movie.");
    }

    @Test
    void test_filtering_is_case_insensitive() {
        List<Movie> result = controller.filterMovies(controller.allMovies, null, "inception", 0, 9999);
        assertEquals(1, result.size(), "Filtering should be case insensitive.");
    }

    @Test
    void test_sorting_filtered_movies() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Genres.ACTION, "", 0, 9999);
        result.sort(Comparator.comparing(Movie::getTitle));
        assertEquals("Inception", result.get(0).getTitle(), "Inception should be first after sorting filtered list.");
    }

    @Test
    void test_sorting_filtered_movies_reversed() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Genres.ACTION, "", 0, 9999);
        result.sort(Comparator.comparing(Movie::getTitle).reversed());
        assertEquals("The Dark Knight", result.get(0).getTitle(), "The Dark Knight should be first after sorting filtered list.");
    }

    @Test
    void test_equals_with_same_movies() {
        Movie movie1 = new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        Movie movie2 = new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        assertTrue(movie1.equals(movie2), "It should be considered equal");
    }

    @Test
    void test_equals_with_null() {
        Movie movie1 = new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        assertFalse(movie1.equals(null), "It should not be considered equal");
    }

    @Test
    void test_equals_with_different_titles() {
        Movie movie1 = new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        Movie movie2 = new Movie("The Dark Knight", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        assertFalse(movie1.equals(movie2), "It should not be considered equal");
    }

    @Test
    void test_equals_with_different_description() {
        Movie movie1 = new Movie("Inception", "", "A super-hero action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        Movie movie2 = new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        assertFalse(movie1.equals(movie2), "It should not be considered equal");
    }

    @Test
    void test_equals_with_different_genres() {
        Movie movie1 = new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        Movie movie2 = new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.COMEDY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"));
        assertFalse(movie1.equals(movie2), "It should not be considered equal");
    }

    @Test
    void test_GetGenresAsString() {
        String genres = controller.allMovies.get(0).getGenresAsString();
        assertEquals("ACTION, FANTASY", genres, "Output should be: ACTION, FANTASY but was: " + genres);
    }


    // Test Cases für Aufgabe 2 starten ab hier


    @Test
    public void test_if_getLongestMovieTitle_returns_the_correct_length() {
        assertEquals(24, controller.getLongestMovieTitle(controller.allMovies));
    }

    @Test
    public void test_if_getLongestMovieTitle_returns_the_correct_length_if_the_list_is_empty() {
        List<Movie> emptyMovieList = Collections.emptyList();
        assertEquals(0, controller.getLongestMovieTitle(emptyMovieList));
    }

    @Test
    public void test_if_getLongestMovieTitle_returns_the_correct_length_when_2_movies_have_the_same_title_length() {
        List<Movie> movies = Arrays.asList(
                new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page")),
                new Movie("Inception", "", "A mind-bending action-adventure", Arrays.asList(Genres.ACTION, Genres.FANTASY), Arrays.asList("Christopher Nolan"), 2010, 8.8, Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"))
        );
        assertEquals(9, controller.getLongestMovieTitle(movies));
    }

    @Test
    public void test_if_getStars_returns_the_correct_actor() {
        assertTrue(controller.getStar(controller.allMovies).contains("Leonardo DiCaprio"));
    }

    @Test
    public void test_getStars_with_a_empty_list() {
        assertTrue(controller.getStar(Collections.emptyList()).isEmpty());
    }

    @Test
    public void test_if_getStars_returns_the_correct_actors_with_multiple_top_actors() {
        List<Movie> movies = Arrays.asList(
                new Movie("Movie1", "", "Description", Arrays.asList(Genres.ACTION, Genres.COMEDY), Arrays.asList("Director"), 2000, 9.0, Arrays.asList("Actor1", "Actor2")),
                new Movie("Movie2", "", "Description", Arrays.asList(Genres.ACTION, Genres.COMEDY), Arrays.asList("Director"), 2001, 8.5, Arrays.asList("Actor2")),
                new Movie("Movie3", "", "Description", Arrays.asList(Genres.ACTION, Genres.COMEDY), Arrays.asList("Director"), 2002, 9.2, Arrays.asList("Actor1"))
        );

        List<String> expectedActors = Arrays.asList("Actor1", "Actor2");
        List<String> returnedActors = controller.getStar(movies);

        Collections.sort(expectedActors);
        Collections.sort(returnedActors);

        assertEquals(expectedActors, returnedActors);
    }

    @Test
    public void test_if_countMoviesFrom_returns_corrert_amount_of_movies() {
        assertEquals(2, controller.countMoviesFrom(controller.allMovies, "Christopher Nolan"));
    }

    @Test
    public void test_countMoviesFrom_with_a_empty_list() {
        assertEquals(0, controller.countMoviesFrom(Collections.emptyList(), "Christopher Nolan"));
    }

    @Test
    public void test_countMoviesFrom_with_no_matches() {
        assertEquals(0, controller.countMoviesFrom(controller.allMovies, "Enis Ibrovic"));
    }

    @Test
    public void test_countMoviesFrom_and_ignoring_case() {
        List<Movie> movies = Arrays.asList(
                new Movie("Movie1", "", "Description", Arrays.asList(Genres.ACTION, Genres.COMEDY), Arrays.asList("Director"), 2000, 9.0, Arrays.asList("Actor1", "Actor2")),
                new Movie("Movie2", "", "Description", Arrays.asList(Genres.ACTION, Genres.COMEDY), Arrays.asList("director"), 2001, 8.5, Arrays.asList("Actor2")),
                new Movie("Movie3", "", "Description", Arrays.asList(Genres.ACTION, Genres.COMEDY), Arrays.asList("Director"), 2002, 9.2, Arrays.asList("Actor1"))
        );
        HomeController controller = new HomeController();
        assertEquals(3, controller.countMoviesFrom(movies, "director"));
    }

    @Test
    public void test_getMoviesBetweenYears_with_Null_List() {
        List<Movie> movies = null;
        assertNull(controller.getMoviesBetweenYears(movies, 0, 3000), "Expected method to return null for null input list.");
    }

    @Test
    public void test_getMoviesBetweenYears_with_no_matching_Movies() {
        List<Movie> result = controller.getMoviesBetweenYears(controller.allMovies, 1990, 2000);
        assertTrue(result.isEmpty(), "Expected no movies to match the year range.");
    }

    @Test
    public void test_getMoviesBetweenYears_with_exact_startYear_and_endYear() {
        List<Movie> result = controller.getMoviesBetweenYears(controller.allMovies, 2008, 2019);
        assertEquals(3, result.size(), "Expected three movies that were released exactly within the range.");
    }

    @Test
    public void test_FilterMovies_by_Year_Range() {
        String searchText = "2000-2015";
        List<Movie> filteredMovies = controller.filterMovies(controller.allMovies, Genres.ALL, searchText, 2000, 20015);
        assertEquals(2, filteredMovies.size(), "Expected two movies to be filtered by year range.");
        assertTrue(filteredMovies.containsAll(Arrays.asList(controller.allMovies.get(0), controller.allMovies.get(1))), "Filtered movies should match the specified years.");
    }

    @Test
    public void test_GetActorCount_with_multiple_appearances() {
        long count = controller.getActorCount(controller.allMovies, "Leonardo DiCaprio");
        assertEquals(2, count);
    }

    @Test
    public void test_GetActorCount_with_no_appearances() {
        long count = controller.getActorCount(controller.allMovies, "Enis Ibrovic");
        assertEquals(0, count);
    }

    @Test
    public void test_GetActorCount_with_a_empty_List() {
        List<Movie> movies = Collections.emptyList();
        long count = controller.getActorCount(movies, "Enis Ibrovic");
        assertEquals(0, count);
    }

}