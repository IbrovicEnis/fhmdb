package at.ac.fhcampuswien.fhmdb.services;

import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieBuilder {
    private String title;
    private List<Genres> genres;
    private int releaseYear;
    private String description;
    private List<String> directors;
    private List<String> mainCast;
    private double rating;

    public MovieBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public MovieBuilder setGenres(List<String> genres) {
        this.genres = genres.stream()
                .map(Genres::valueOf)
                .collect(Collectors.toList());
        return this;
    }

    public MovieBuilder setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public MovieBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public MovieBuilder setDirectors(List<String> directors) {
        this.directors = directors;
        return this;
    }

    public MovieBuilder setMainCast(List<String> mainCast) {
        this.mainCast = mainCast;
        return this;
    }

    public MovieBuilder setRating(double rating) {
        this.rating = rating;
        return this;
    }


    public Movie build() {
        return new Movie(title, description, genres, directors, releaseYear, rating, mainCast);
    }
}