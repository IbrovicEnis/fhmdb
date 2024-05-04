package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.controllerInstances.NewControllerInstances;
import at.ac.fhcampuswien.fhmdb.ui.UserIntComp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) {
        NewControllerInstances newControllerInstances = NewControllerInstances.getInstance();
        FXMLLoader loader = new FXMLLoader(FhmdbApplication.class.getResource(UserIntComp.HOME.path));

        loader.setControllerFactory(newControllerInstances);

        try{

        Scene scene = new Scene(loader.load(), 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        stage.show();
        Image applicationIcon = new Image(getClass().getResourceAsStream("/at/ac/fhcampuswien/fhmdb/app_icon.png"));
        stage.getIcons().add(applicationIcon);
        } catch (IOException e) {
            System.err.println("Cannot load scene from " + UserIntComp.HOME.path);
        } catch (NullPointerException e) {
            System.err.println("Path to stylesheet may be corrupt.");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}