package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public abstract class MovieDeserializer implements JsonDeserializer<Movie> {
    private final Gson gson;

    @Override
    public Movie deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();
        List<String> genres = Arrays.asList(context.deserialize(jsonObject.get("genres"), String[].class));
        List<String> directors = Arrays.asList(gson.fromJson(jsonObject.get("directors"), String[].class));
        List<String> mainCast = Arrays.asList(gson.fromJson(jsonObject.get("mainCast"), String[].class));
        int releaseYear = jsonObject.get("releaseYear").getAsInt();
        double rating = jsonObject.get("rating").getAsDouble();

        return new MovieBuilder()
                .setTitle(title)
                .setDescription(description)
                .setGenres(genres)
                .setDirectors(directors)
                .setMainCast(mainCast)
                .setReleaseYear(releaseYear)
                .setRating(rating)
                .build();
    }

    public MovieDeserializer() {

        this.gson = new GsonBuilder()

                .create();
    }
}

