package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String apild;
    @DatabaseField
    private String title;
    @DatabaseField
    private String decsription;
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

    public static String genresToString(List<Genres> genres) {
        return genres.stream()
                .map(Genres::name)
                .collect(Collectors.joining(", "));
    }
    public List<MovieEntity> fromMovies (List<Movie> movies){
        return null;
    }
    public List<Movie> toMovies (List<MovieEntity> movieEntities){
        return null;
    }
}
