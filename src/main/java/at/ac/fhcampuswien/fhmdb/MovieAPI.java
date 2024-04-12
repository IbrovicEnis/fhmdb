package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class MovieAPI {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public List<Movie> getAllMovies(String query, Genres genre, String releaseYear, String ratingFrom) throws IOException {
        String url = buildURL(query, genre, releaseYear, ratingFrom);
        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseData = response.body().string();
            Movie[] moviesArray = gson.fromJson(responseData, Movie[].class);
            return Arrays.asList(moviesArray);
        }
    }


    private String buildURL(String query, Genres genre, String releaseYear, String ratingFrom) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        if (query != null || genre != null || releaseYear != null || ratingFrom != null) {
            urlBuilder.append("?");
            if (query != null) {
                urlBuilder.append("query=").append(query);
                if (genre != null || releaseYear != null || ratingFrom != null) {
                    urlBuilder.append("&");
                }
            }
            if (genre != null) {
                urlBuilder.append("genre=").append(genre);
                if (releaseYear != null || ratingFrom != null) {
                    urlBuilder.append("&");
                }
            }
            if (releaseYear != null) {
                urlBuilder.append("releaseYear=").append(releaseYear);
                if (ratingFrom != null) {
                    urlBuilder.append("&");
                }
            }
            if (ratingFrom != null) {
                urlBuilder.append("ratingFrom=").append(ratingFrom);
            }
        }
        System.out.println("New API Request: " + urlBuilder.toString());
        return urlBuilder.toString();
    }

}