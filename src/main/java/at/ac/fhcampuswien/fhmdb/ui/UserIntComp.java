package at.ac.fhcampuswien.fhmdb.ui;

public enum UserIntComp {
    HOME("/at/ac/fhcampuswien/fhmdb/main.fxml"),
    HOMEVIEW("/at/ac/fhcampuswien/fhmdb/home-view.fxml"),
    WATCHLIST("/at/ac/fhcampuswien/fhmdb/watchlist.fxml");

    public final String path;

    UserIntComp(String path) {
        this.path = path;
    }
}
