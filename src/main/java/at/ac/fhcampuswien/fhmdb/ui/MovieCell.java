package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label genre = new Label();
    private final Label detail = new Label();
    private final Label directors = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final Label mainCast = new Label();
    private final VBox layout = new VBox(title, genre, detail, directors, mainCast, releaseYear, rating);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            title.setText("");
            detail.setText("");
            genre.setText("");
            directors.setText("");
            mainCast.setText("");
            releaseYear.setText("");
            rating.setText("");
            layout.setBackground(null);
            title.getStyleClass().clear();
            detail.getStyleClass().clear();
            genre.getStyleClass().clear();
            directors.getStyleClass().clear();
            mainCast.getStyleClass().clear();
            releaseYear.getStyleClass().clear();
            rating.getStyleClass().clear();
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genre.setText("Genres: " + String.join(", ", movie.getGenresAsString()));
            directors.setText("Director: " + String.join(", ", movie.getDirectors()));
            mainCast.setText("Main Cast: " + String.join(", ", movie.getMainCast()));
            releaseYear.setText("Release Year: " + movie.getReleaseYear());
            rating.setText("Rating: " + movie.getRating());
            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("text-blue");
            directors.getStyleClass().add("text-green");
            mainCast.getStyleClass().add("text-orange");
            releaseYear.getStyleClass().add("text-purple");
            rating.getStyleClass().add("text-red");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 60);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}
