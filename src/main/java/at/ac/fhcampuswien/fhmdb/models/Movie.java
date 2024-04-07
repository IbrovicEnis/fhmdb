package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.Genres;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genres> genres;
    private final String director;
    private final int releaseYear;
    private final double rating;

    public Movie(String title, String description, List<Genres> genres, String director, int releaseYear, double rating) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a proper movie title!");
        }
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Please assign a proper genre for the movie!");
        }
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.director = director;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, genres, director, releaseYear, rating);
    }

    @Override
    public boolean equals(Object movie) {
        if (this == movie) return true;
        if (movie == null || getClass() != movie.getClass()) return false;
        Movie otherMovie = (Movie) movie;
        return Objects.equals(title, otherMovie.title) &&
                Objects.equals(description, otherMovie.description) &&
                Objects.equals(genres, otherMovie.genres) &&
                Objects.equals(director, otherMovie.director) &&
                releaseYear == otherMovie.releaseYear &&
                Double.compare(otherMovie.rating, rating) == 0;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDirector() {
        return director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }
    public List<Genres> getGenres() {
        return genres;
    }

    public String getGenresAsString() {
        String result = "";
        for (Enum genre : genres) {
            if (!result.isEmpty()) {
                result += ", ";
            }
            result += genre.toString();
        }
        return result;

    }


    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();

        return movies;
    }
}
