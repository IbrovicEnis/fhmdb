package at.ac.fhcampuswien.fhmdb.services;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAPI {
    private static final String BASE_URL = "http://prog2.fh-campuswien.ac.at/movies";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    private String buildURL(String query, Genres genre, String releaseYear, String ratingFrom) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        List<String> parameters = new ArrayList<>();

        if (query != null) {
            parameters.add("query=" + query);
        }
        if (genre != null) {
            parameters.add("genre=" + genre);
        }
        if (releaseYear != null) {
            parameters.add("releaseYear=" + releaseYear);
        }
        if (ratingFrom != null) {
            parameters.add("ratingFrom=" + ratingFrom);
        }

        if (!parameters.isEmpty()) {
            urlBuilder.append("?");
            urlBuilder.append(String.join("&", parameters));
        }
        return urlBuilder.toString();
    }

    public List<Movie> getAllMovies(String query, Genres genre, String releaseYear, String ratingFrom) throws MovieApiException {
        String url = buildURL(query, genre, releaseYear, ratingFrom);
        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseData = response.body().string();
            Movie[] moviesArray = gson.fromJson(responseData, Movie[].class);
            List<Movie> movies = new ArrayList<>(Arrays.asList(moviesArray));

            for (Movie movie : movies) {
                if (movie.getApiId() == null || movie.getApiId().trim().isEmpty()) {
                    movie.setApiId();
                }
            }
            return movies;
        } catch (IOException ioe) {
            throw new MovieApiException("Failed to fetch movies from API", ioe);
        }
    }
}