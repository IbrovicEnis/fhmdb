package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genres> genres;
    private final List<String> directors;
    private final int releaseYear;
    private final double rating;
    private final List<String> mainCast;

    public Movie(String title, String description, List<Genres> genres, List<String> directors, int releaseYear, double rating, List<String> mainCast) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a proper movie title!");
        }
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Please assign a proper genre for the movie!");
        }
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.directors = directors != null ? directors : new ArrayList<>();
        this.mainCast = mainCast != null ? mainCast : new ArrayList<>();
        this.releaseYear = releaseYear;
        this.rating = rating;

    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, genres, directors, releaseYear, rating, mainCast);
    }

    @Override
    public boolean equals(Object movie) {
        if (this == movie) return true;
        if (movie == null || getClass() != movie.getClass()) return false;
        Movie otherMovie = (Movie) movie;
        return Objects.equals(title, otherMovie.title) &&
                Objects.equals(description, otherMovie.description) &&
                Objects.equals(genres, otherMovie.genres) &&
                Objects.equals(directors, otherMovie.directors) &&
                releaseYear == otherMovie.releaseYear &&
                Double.compare(otherMovie.rating, rating) == 0 &&
                Objects.equals(mainCast, otherMovie.mainCast);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getDirectors() {
        return directors;
    }
    public List<String> getMainCast() {
        return mainCast;
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
