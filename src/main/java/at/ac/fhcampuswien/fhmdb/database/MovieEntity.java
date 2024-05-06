package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String apiId;
    @DatabaseField
    private String title;
    @DatabaseField
    private String description;
    @DatabaseField
    private String genres;
    @DatabaseField
    private int releaseYear;
    @DatabaseField
    private String imgUrl;
    @DatabaseField
    private int lengthInMinutes;
    @DatabaseField
    private double rating;
    public MovieEntity(){}
    public MovieEntity(String apiId,String title, String description, String genres,
                       int releaseYear, String imgUrl, int lengthInMinutes, double rating) {
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    public static String genresToString(List<Genres> genres) {
        return genres.stream()
                .map(Genres::name)
                .collect(Collectors.joining(", "));
    }
    public static List<Genres> stringToGenres(String genresString) {
        return Arrays.stream(genresString.split(","))
                .map(String::trim)
                .map(Genres::valueOf)
                .collect(Collectors.toList());
    }
    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        return movies.stream()
                .map(movie -> new MovieEntity(
                        movie.getApiId(),
                        movie.getTitle(),
                        movie.getDescription(),
                        genresToString(movie.getGenres()),
                        movie.getReleaseYear(),
                        "",
                        0,
                        movie.getRating()
                ))
                .collect(Collectors.toList());
    }
    public static MovieEntity fromMovie(Movie movie) {
        if (movie == null) return null;
        return new MovieEntity(
                movie.getApiId(),
                movie.getTitle(),
                movie.getDescription(),
                genresToString(movie.getGenres()),
                movie.getReleaseYear(),
                "",
                0,
                movie.getRating()
        );
    }

    public static List<Movie> toMovies (List<MovieEntity> movieEntities){
        return movieEntities.stream()
                .map(movieEntity -> new Movie(
                        movieEntity.getTitle(),
                        movieEntity.getDescription(),
                        stringToGenres(movieEntity.getGenres()),
                        null,
                        movieEntity.getReleaseYear(),
                        movieEntity.getRating(),
                        null
                ))
                .collect(Collectors.toList());
    }
    public static Movie toMovie(MovieEntity movieEntity) {
        if (movieEntity == null) return null;
        return new Movie(
                movieEntity.getTitle(),
                movieEntity.getDescription(),
                stringToGenres(movieEntity.getGenres()),
                null,
                movieEntity.getReleaseYear(),
                movieEntity.getRating(),
                null
        );
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public String getGenres() {
        return genres;
    }

    public Long getId() {
    return id;
    }

    public String getApiId() {
        return apiId;
    }
}

