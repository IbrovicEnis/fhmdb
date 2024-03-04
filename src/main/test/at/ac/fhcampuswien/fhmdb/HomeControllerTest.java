package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

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


    //Test wurde implementiert, da es beim Filtern zunächst schwierigkeiten mit doppelten einträgen etc. gab
    @Test
    void test_if_filtering_genre_returns_correct_amount_of_movies()
    {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.ACTION, "");
        assertEquals(2, result.size(), "Die Anzahl der gefilterten Filme nach Genre ACTION sollte 2 sein.");
    }

    @Test
    void test_if_filtering_only_genre_returns_correct_movies_that_contain_that_genre() {
        List<Movie> result = controller.filterMovies(controller.allMovies, Movie.Genre.CRIME, "");
        assertEquals(1, result.size(), "Die Anzahl der gefilterten Filme nach Genre CRIME sollte 1 sein.");
        assertTrue(result.get(0).getGenres().contains(Movie.Genre.CRIME), "Der Film sollte das Genre CRIME enthalten.");
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




}