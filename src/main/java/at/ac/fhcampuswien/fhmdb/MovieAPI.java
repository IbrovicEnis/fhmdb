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
    private final OkHttpClient client;
    private final String baseURL;
    private final Gson gson;
    public MovieAPI() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SSLContext", e);
        }
        this.client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();

        this.gson = new Gson();
        this.baseURL = "https://prog2.fh-campuswien.ac.at";
    }

    public List<Movie> getAllMovies(String searchTerm, String genre) throws IOException {
        String url = baseURL + "/movies";
        if (searchTerm != null || genre != null) {
            url += "?";
            if (searchTerm != null) {
                url += "query=" + searchTerm;
            }
            if (genre != null) {
                url += "&genre=" + genre;
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
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
}