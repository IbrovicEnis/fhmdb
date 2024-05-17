module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires java.desktop;
    requires java.sql;
    requires ormlite.jdbc;
    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb.controllers to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.controllers to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.models;
    exports at.ac.fhcampuswien.fhmdb.services;
    exports at.ac.fhcampuswien.fhmdb.exceptions;
    exports at.ac.fhcampuswien.fhmdb.database to ormlite.jdbc;
    opens at.ac.fhcampuswien.fhmdb.services to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson, javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.database to ormlite.jdbc;

}