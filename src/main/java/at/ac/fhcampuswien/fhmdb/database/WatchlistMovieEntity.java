package at.ac.fhcampuswien.fhmdb.database;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import at.ac.fhcampuswien.fhmdb.models.Genres;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "watchlist")
public class WatchlistMovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false)
    private String apiId;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField()
    private String description;

    @DatabaseField()
    private String genres;

    @DatabaseField()
    private int releaseYear;

    @DatabaseField()
    private String imgUrl;

    @DatabaseField()
    private int lengthInMinutes;

    @DatabaseField()
    private double rating;

    public WatchlistMovieEntity(){}

    public WatchlistMovieEntity(String title, String description, int releaseYear, List<Genres> genres, double rating) {
        this.apiId = UUID.randomUUID().toString(); // Generate a unique identifier
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genres = genresToString(genres);
        this.rating = rating;
    }


    private String genresToString(List<Genres> genres) {
        StringBuilder sb = new StringBuilder();
        for (Genres genre : genres) {
            sb.append(genre.name());
            sb.append(",");
        }
        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", apiId=" + apiId + ", title=" + title + ", description=" + description + ", releaseYear=" + releaseYear + "]";
    }

    public List<Genres> getGenres() {
        return Arrays.stream(genres.split(",")).map(Genres::valueOf).toList();
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genresToString(genres);
    }

    public String getLengthInMinutes() {
        return lengthInMinutes + " min";
    }

    public String getRating() {
        return rating + "/10";
    }
}
