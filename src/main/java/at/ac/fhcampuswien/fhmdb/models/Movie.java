package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Movie {
    private String title;
    private String description;
    // TODO add more properties here
    private List<Genre> genres;
    public Movie(String title, String description, List<Genre> genres) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres list cannot be null or empty");
        }
        this.title = title;
        this.description = description;
        this.genres = genres;
    }
    @Override
    public int hashCode() {
        return Objects.hash(title, description, genres);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Movie otherMovie = (Movie) obj;
        return Objects.equals(title, otherMovie.title) &&
                Objects.equals(description, otherMovie.description) &&
                Objects.equals(genres, otherMovie.genres);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public List<Genre> getGenres() {
        return genres;
    }

    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();
        // TODO add some dummy data here

        // Movie 1
        Movie movie1 = new Movie("Inception", "A mind-bending action-adventure",
                Arrays.asList(Genre.ACTION, Genre.FANTASY));
        movies.add(movie1);

        // Movie 2
        Movie movie2 = new Movie("The Dark Knight", "A superhero action-adventure",
                Arrays.asList(Genre.CRIME, Genre.ACTION));
        movies.add(movie2);

        // Movie 3
        Movie movie3 = new Movie("Jurassic Park", "An adventurous sci-fi journey with dinosaurs",
                Arrays.asList(Genre.SCIENCE_FICTION, Genre.ACTION));
        movies.add(movie3);

        return movies;
    }
    public enum Genre {
        ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY,
        CRIME, DRAMA, DOCUMENTARY, FAMILY, FANTASY,
        HISTORY, HORROR, MUSICAL, MYSTERY, ROMANCE,
        SCIENCE_FICTION, SPORT, THRILLER, WAR, WESTERN
    }
}
