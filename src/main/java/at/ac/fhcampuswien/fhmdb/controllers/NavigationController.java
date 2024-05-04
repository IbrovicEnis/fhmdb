package at.ac.fhcampuswien.fhmdb.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import at.ac.fhcampuswien.fhmdb.controllerInstances.NewControllerInstances;
import at.ac.fhcampuswien.fhmdb.ui.UserIntComp;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
public class NavigationController {

    @FXML
    private VBox navDrawer;

    public VBox getNavDrawer() {
        return navDrawer;
    }

    private HomeController parentController;

    public void setParentController(HomeController parentController) {
        this.parentController = parentController;
    }
    @FXML
    public JFXHamburger hamburgerMenu;

    @FXML
    private JFXDrawer drawer;
    @FXML
    private BorderPane mainPane;
    private boolean isMenuCollapsed = true;

    private final NewControllerInstances newControllerInstances = NewControllerInstances.getInstance();

    private HamburgerBasicCloseTransition transition;

    public void initialize() {
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource(UserIntComp.HOME.path));
        loader.setControllerFactory(newControllerInstances);

        transition = new HamburgerBasicCloseTransition(hamburgerMenu);
        transition.setRate(-1);
        drawer.toBack();


        hamburgerMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            toggleMenuDrawer();
        });
        // start with home view
        navigateToHome();
    }

    private void toggleHamburgerTransitionState(){
        transition.setRate(transition.getRate() * -1);
        transition.play();
    }

    private void toggleMenuDrawer(){
        toggleHamburgerTransitionState();

        if(isMenuCollapsed) {
            TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5), drawer);
            translateTransition.setByX(130);
            translateTransition.play();
            isMenuCollapsed = false;
            drawer.toFront();
        } else {
            TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5), drawer);
            translateTransition.setByX(-130);
            translateTransition.play();
            isMenuCollapsed = true;
            drawer.toBack();
        }
    }

    public void setContent(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(NavigationController.class.getResource(fxmlPath));
        loader.setControllerFactory(newControllerInstances);
        try {
            mainPane.setCenter(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!isMenuCollapsed){
            toggleMenuDrawer();
        }
    }
    @FXML
    public void navigateToWatchlist() {
        setContent(UserIntComp.WATCHLIST.path);
    }

    @FXML
    public void navigateToHome() {
        setContent(UserIntComp.HOMEVIEW.path);
    }
}
