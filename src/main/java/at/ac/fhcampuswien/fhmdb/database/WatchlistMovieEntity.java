package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist")
public class WatchlistMovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String apiId;

    public WatchlistMovieEntity() {
    }

    public WatchlistMovieEntity(long id, String apiId) {
        this.id = id;
        this.apiId = apiId;
    }

    public String getApiId() {
        return apiId;
    }
}
