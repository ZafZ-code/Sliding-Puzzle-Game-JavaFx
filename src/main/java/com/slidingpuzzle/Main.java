package com.slidingpuzzle;

import com.slidingpuzzle.controllers.MenuController;
import com.slidingpuzzle.controllers.PuzzleController;
import com.slidingpuzzle.enums.Difficulty;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Application entry point.
 *
 * There is one Stage (the window) and one Scene. To change screens we keep the
 * Scene and swap its root node: menu.fxml <-> puzzle.fxml.
 * Controllers receive a reference to Main so they can ask it to change screen.
 */
public class Main extends Application {

    private static final String MENU_FXML = "/com/slidingpuzzle/fxml/menu.fxml";
    private static final String PUZZLE_FXML = "/com/slidingpuzzle/fxml/puzzle.fxml";
    private static final String STYLESHEET = "/com/slidingpuzzle/css/style.css";

    private static final double WINDOW_WIDTH = 520;
    private static final double WINDOW_HEIGHT = 740;

    private Scene scene;

    @Override
    public void start(Stage stage) {
        // The Scene needs a root node; we start with an empty one and replace it in showMenu().
        scene = new Scene(new StackPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(findResource(STYLESHEET).toExternalForm());

        stage.setTitle("Sliding Puzzle");
        stage.setScene(scene);
        stage.setResizable(false);

        showMenu();
        stage.show(); // without show(), nothing appears
    }

    /** Replaces the current screen with the main menu. */
    public void showMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(findResource(MENU_FXML));
            Parent root = loader.load();                     // builds the nodes, creates the controller
            MenuController controller = loader.getController();
            controller.setMain(this);
            scene.setRoot(root);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load " + MENU_FXML, e);
        }
    }

    /** Replaces the current screen with a new game of the chosen difficulty. */
    public void showPuzzle(Difficulty difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader(findResource(PUZZLE_FXML));
            Parent root = loader.load();
            PuzzleController controller = loader.getController();
            controller.setMain(this);
            controller.startGame(difficulty);
            scene.setRoot(root);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load " + PUZZLE_FXML, e);
        }
    }

    /** getResource() returns null when the path is wrong, so we fail with a clear message instead. */
    private URL findResource(String path) {
        URL url = Main.class.getResource(path);
        if (url == null) {
            throw new IllegalStateException("Resource not found on the classpath: " + path);
        }
        return url;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
