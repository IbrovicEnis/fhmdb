package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.database.MovieEntity.genresToString;

public class WatchlistCell extends ListCell<MovieEntity> {
    private final Label title = new Label();
    private final Label description = new Label();
    private final Label genre = new Label();
    private final JFXButton moreButton = new JFXButton("See more!");
    private final JFXButton removeButton = new JFXButton("-");
    private final HBox header = new HBox(title, moreButton, removeButton);
    private final VBox layout = new VBox(header, description, genre);
    private boolean collapsedDetails = true;

    public WatchlistCell(ClickEventHandler removeFromWatchlistClick) {
        super();

        moreButton.setStyle("-fx-background-color: #f5c518;");
        VBox.setMargin(header, new Insets(0, 0, 10, 0));
        HBox.setMargin(moreButton, new Insets(0, 10, 0, 10));
        removeButton.setStyle("-fx-background-color: #f5c518;");
        title.getStyleClass().add("text-yellow");
        title.setStyle("-fx-font-size: 20");
        description.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-blue");
        header.setAlignment(Pos.CENTER_LEFT);
        title.setMaxWidth(Double.MAX_VALUE);
        title.fontProperty().set(title.getFont().font(20));
        description.setWrapText(true);
        layout.setPadding(new Insets(10));

        moreButton.setOnMouseClicked(mouseEvent -> {
            if (collapsedDetails) {
                layout.getChildren().add(getDetails());
                collapsedDetails = false;
                moreButton.setText("See less!");
            } else {
                layout.getChildren().remove(3);
                collapsedDetails = true;
                moreButton.setText("See more!");
            }
            setGraphic(layout);
        });
        removeButton.setOnMouseClicked(mouseEvent -> {
            MovieEntity movieEntity = getItem();
            if (movieEntity != null) {
                removeFromWatchlistClick.onClick(movieEntity);
            }
        });
    }

    private VBox getDetails() {
        VBox details = new VBox();
        Label releaseYear = new Label("Release Year: " + getItem().getReleaseYear());

        Label rating = new Label("Rating: " + getItem().getRating());

        releaseYear.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");

        details.getChildren().add(releaseYear);
        details.getChildren().add(rating);

        return details;
    }
    @Override
    protected void updateItem(MovieEntity movieEntity, boolean empty) {
        super.updateItem(movieEntity, empty);

        if (empty || movieEntity == null) {
            setGraphic(null);
            setText(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movieEntity.getTitle());
            description.setText(
                    movieEntity.getDescription() != null
                            ? movieEntity.getDescription()
                            : "Currently no info, but soon ™!"
            );

            description.setMaxWidth(this.getScene().getWidth() - 30);

            String genres = movieEntity.getGenres();
            genre.setText(genres);

            layout.setPadding(new Insets(10));
            layout.setBorder(new Border(new BorderStroke(Color.web("#f5c518"),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            setGraphic(layout);
        }
    }
}