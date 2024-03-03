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
        // Adding a movie with multiple genres
        // Movie 1
        Movie movie1 = new Movie("Inception", "A mind-bending action-adventure",
                Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION));
        movies.add(movie1);

        // Movie 2
        Movie movie2 = new Movie("The Dark Knight", "A superhero action-adventure",
                Arrays.asList(Genre.ACTION, Genre.CRIME, Genre.DRAMA));
        movies.add(movie2);

        // Movie 3
        Movie movie3 = new Movie("Jurassic Park", "An adventurous sci-fi journey with dinosaurs",
                Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION));
        movies.add(movie3);

        // Movie 4
        Movie movie4 = new Movie("Avatar", "A visually stunning sci-fi adventure",
                Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY));
        movies.add(movie4);

        // Movie 5
        Movie movie5 = new Movie("The Fault in Our Stars", "A heartfelt drama with a touch of a love story",
                Arrays.asList(Genre.DRAMA, Genre.ROMANCE));
        movies.add(movie5);

        // Movie 6
        Movie movie6 = new Movie("Pride and Prejudice", "A classic drama in love",
                Arrays.asList(Genre.DRAMA, Genre.ROMANCE));
        movies.add(movie6);
        // Movie 7
        Movie movie7 = new Movie("Forrest Gump", "A heartwarming drama-comedy",
                Arrays.asList(Genre.DRAMA, Genre.COMEDY, Genre.ROMANCE));
        movies.add(movie7);

// Movie 8
        Movie movie8 = new Movie("The Shawshank Redemption", "A powerful drama about hope and freedom",
                Arrays.asList(Genre.DRAMA, Genre.CRIME));
        movies.add(movie8);

// Movie 9
        Movie movie9 = new Movie("The Matrix", "A groundbreaking sci-fi action film",
                Arrays.asList(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.ADVENTURE));
        movies.add(movie9);

// Movie 10
        Movie movie10 = new Movie("The Godfather", "An iconic crime drama",
                Arrays.asList(Genre.CRIME, Genre.DRAMA));
        movies.add(movie10);

// Movie 11
        Movie movie11 = new Movie("Inglourious Basterds", "A war film with a twist of dark humor",
                Arrays.asList(Genre.ACTION, Genre.DRAMA, Genre.WAR));
        movies.add(movie11);

// Movie 12
        Movie movie12 = new Movie("Toy Story", "An animated adventure with heartwarming themes",
                Arrays.asList(Genre.ANIMATION, Genre.ADVENTURE, Genre.COMEDY));
        movies.add(movie12);
        // Movie 13
        Movie movie13 = new Movie("The Avengers", "A thrilling superhero ensemble",
                Arrays.asList(Genre.ACTION, Genre.SCIENCE_FICTION));
        movies.add(movie13);

// Movie 14
        Movie movie14 = new Movie("The Grand Budapest Hotel", "A quirky and visually stunning comedy",
                Arrays.asList(Genre.COMEDY, Genre.DRAMA));
        movies.add(movie14);

// Movie 15
        Movie movie15 = new Movie("The Silence of the Lambs", "A suspenseful crime thriller",
                Arrays.asList(Genre.HORROR));
        movies.add(movie15);

// Movie 16
        Movie movie16 = new Movie("The Lion King", "An animated classic with unforgettable music",
                Arrays.asList(Genre.ANIMATION, Genre.ADVENTURE, Genre.MUSICAL));
        movies.add(movie16);

// Movie 17
        Movie movie17 = new Movie("Titanic", "An epic romance and disaster film",
                Arrays.asList(Genre.ROMANCE, Genre.DRAMA));
        movies.add(movie17);

// Movie 18
        Movie movie18 = new Movie("The Social Network", "A drama about the founding of Facebook",
                Arrays.asList(Genre.DRAMA, Genre.BIOGRAPHY));
        movies.add(movie18);

// Movie 19
        Movie movie19 = new Movie("Casablanca", "A classic romance set during World War II",
                Arrays.asList(Genre.ROMANCE, Genre.DRAMA, Genre.WAR));
        movies.add(movie19);

// Movie 20
        Movie movie20 = new Movie("Frozen", "An animated musical adventure",
                Arrays.asList(Genre.ANIMATION, Genre.ADVENTURE, Genre.MUSICAL));
        movies.add(movie20);
        return movies;
    }
    public enum Genre {
        ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY,
        CRIME, DRAMA, DOCUMENTARY, FAMILY, FANTASY,
        HISTORY, HORROR, MUSICAL, MYSTERY, ROMANCE,
        SCIENCE_FICTION, SPORT, THRILLER, WAR, WESTERN
    }
}
