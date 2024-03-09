package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Movie {
    private String title;
    private String description;
    private List<Genre> genres;
    public Movie(String title, String description, List<Genre> genres) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a proper movie title!");
        }
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Please assign a proper genre for the movie!");
        }
        this.title = title;
        this.description = description;
        this.genres = genres;
    }
    public String getGenresAsString() {
        return genres.stream()
                .map(Enum::toString)
                .collect(Collectors.joining(", "));
    }
    @Override
    public int hashCode() {
        return Objects.hash(title, description, genres);
    }

    @Override
    public boolean equals(Object movie) {
        if (this == movie) return true;
        if (movie == null || getClass() != movie.getClass()) return false; // check if movie is null or not in Movie
        Movie otherMovie = (Movie) movie;
        return Objects.equals(title, otherMovie.title) &&
                Objects.equals(description, otherMovie.description) &&
                Objects.equals(genres, otherMovie.genres);
    }

    public String getTitle() {return title;
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

        // Movie 3
        Movie movie3 = new Movie("Interstellar", "A journey beyond the stars",
                Arrays.asList(Movie.Genre.SCIENCE_FICTION, Movie.Genre.DRAMA));
        movies.add(movie3);

        // Movie 4
        Movie movie4 = new Movie("The Grand Budapest Hotel", "A visually stunning caper",
                Arrays.asList(Movie.Genre.COMEDY, Movie.Genre.DRAMA));
        movies.add(movie4);

        // Movie 5
        Movie movie5 = new Movie("Mad Max: Fury Road", "A post-apocalyptic action film",
                Arrays.asList(Movie.Genre.ACTION, Movie.Genre.ADVENTURE));
        movies.add(movie5);

        // Movie 6
        Movie movie6 = new Movie("La La Land", "A musical romantic drama",
                Arrays.asList(Movie.Genre.MUSICAL, Movie.Genre.DRAMA));
        movies.add(movie6);

        // Movie 7
        Movie movie7 = new Movie("Parasite", "A dark twist on a family's dream for a better life",
                Arrays.asList(Movie.Genre.THRILLER, Movie.Genre.DRAMA));
        movies.add(movie7);

        // Movie 8
        Movie movie8 = new Movie("Joker", "A gritty character study of the iconic villain",
                Arrays.asList(Movie.Genre.CRIME, Movie.Genre.DRAMA));
        movies.add(movie8);

        return movies;
    }
    public enum Genre {
        ALL,ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY,
        CRIME, DRAMA, DOCUMENTARY, FAMILY, FANTASY,
        HISTORY, HORROR, MUSICAL, MYSTERY, ROMANCE,
        SCIENCE_FICTION, SPORT, THRILLER, WAR, WESTERN
    }
}
