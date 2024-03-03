package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // Adding a movie with multiple genres
        List<Genre> movie1Genres = Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION);
        List<Genre> actionAdventureSciFiGenres = Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION);
        Movie movie1 = new Movie("Inception", "A mind-bending action-adventure", actionAdventureSciFiGenres);
        movies.add(movie1);

        actionAdventureSciFiGenres = Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION);
        Movie movie2 = new Movie("The Dark Knight", "A superhero action-adventure", actionAdventureSciFiGenres);
        movies.add(movie2);

        List<Genre> actionAdventureSciFiGenres3 = Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION);
        Movie movie5 = new Movie("Jurassic Park", "An adventurous sci-fi journey with dinosaurs", actionAdventureSciFiGenres3);
        movies.add(movie5);

        List<Genre> actionAdventureSciFiGenres4 = Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION);
        Movie movie6 = new Movie("Avatar", "A visually stunning sci-fi adventure", actionAdventureSciFiGenres4);
        movies.add(movie6);

        List<Genre> dramaRomanceGenres3 = Arrays.asList(Genre.DRAMA, Genre.ROMANCE);
        Movie movie7 = new Movie("The Fault in Our Stars", "A heartfelt drama with a touch of romance", dramaRomanceGenres3);
        movies.add(movie7);

        List<Genre> dramaRomanceGenres4 = Arrays.asList(Genre.DRAMA, Genre.ROMANCE);
        Movie movie8 = new Movie("Pride and Prejudice", "A classic romantic drama", dramaRomanceGenres4);
        movies.add(movie8);
        return movies;
    }
    public enum Genre {
        ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY,
        CRIME, DRAMA, DOCUMENTARY, FAMILY, FANTASY,
        HISTORY, HORROR, MUSICAL, MYSTERY, ROMANCE,
        SCIENCE_FICTION, SPORT, THRILLER, WAR, WESTERN
    }
}
