package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;
import java.sql.SQLException;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label genre = new Label();
    private final Label detail = new Label();
    private final JFXButton watchlistButton = new JFXButton("+");
    private final JFXButton moreButton = new JFXButton("Show more!");
    private final HBox header = new HBox(title, moreButton, watchlistButton);
    private final VBox layout = new VBox(header, detail, genre);
    private boolean collapsedDetails = true;

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            title.setStyle("-fx-font-size: 20");
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genre.setText("Genres: " + String.join(", ", movie.getGenresAsString()));

            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("text-blue");
            header.setSpacing(10);
            VBox.setMargin(header, new Insets(0, 0, 10, 0));
            watchlistButton.setStyle("-fx-background-color: #f5c518; -fx-font-weight: bold;");
            moreButton.setStyle("-fx-background-color: #f5c518; -fx-font-weight: bold;");

            detail.setMaxWidth(this.getScene().getWidth() - 60);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.setBorder(new Border(new BorderStroke(Color.web("#f5c518"),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            setGraphic(layout);
        }
    }

    private VBox getDetails() {
        VBox details = new VBox();
        Label directors = new Label("Director: " + String.join(", ", getItem().getDirectors()));
        Label mainCast = new Label("Main Cast: " + String.join(", ", getItem().getMainCast()));
        Label releaseYear = new Label("Release Year: " + getItem().getReleaseYear());
        Label rating = new Label("Rating: " + getItem().getRating());

        directors.getStyleClass().add("text-green");
        mainCast.getStyleClass().add("text-white");
        releaseYear.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");

        details.getChildren().addAll(directors, mainCast, releaseYear, rating);
        return details;
    }

    public MovieCell(ClickEventHandler<Movie> addToWatchlistClicked) {
        super();
        HBox.setMargin(watchlistButton, new Insets(0, 10, 0, 10));

        moreButton.setOnMouseClicked(mouseEvent -> {
            if (collapsedDetails) {
                layout.getChildren().add(getDetails());
                collapsedDetails = false;
                detail.setText("Hide Details");
            } else {
                layout.getChildren().remove(3);
                collapsedDetails = true;
                detail.setText("Show Details");
            }
            setGraphic(layout);
        });

        watchlistButton.setOnMouseClicked(mouseEvent -> {
            try {
                addToWatchlistClicked.onClick(getItem());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
