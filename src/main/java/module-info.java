module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires java.desktop;
    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.models;
    exports at.ac.fhcampuswien.fhmdb.services;
    opens at.ac.fhcampuswien.fhmdb.services to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson, javafx.fxml;
}