package com.slidingpuzzle.controllers;

import com.slidingpuzzle.Main;
import com.slidingpuzzle.enums.Difficulty;
import com.slidingpuzzle.services.GameManager;

import java.util.Optional;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * Controller of puzzle.fxml: draws the board and reacts to the player.
 * All rules live in GameManager / PuzzleBoard. This class only
 *   1. forwards clicks to the GameManager,
 *   2. redraws the screen from the GameManager's state.
 *
 * Click flow:  Button click -> handleTileClick(row, col) -> game.moveTile(row, col)
 *              -> renderBoard() + updateStats() -> (if solved) victory dialog
 */
public class PuzzleController {

    private static final double BOARD_PIXELS = 400;   // width and height of the tile area
    private static final double CLOCK_REFRESH_MS = 200;

    @FXML
    private Label difficultyLabel;
    @FXML
    private Label movesLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private GridPane boardGrid;

    private Main main;
    private GameManager game;
    private Timeline clock;

    /**
     * Called automatically by FXMLLoader after the @FXML fields have been injected.
     * The Timeline only refreshes the time label every 200 ms; the real time
     * is measured by GameManager.
     */
    @FXML
    private void initialize() {
        clock = new Timeline(new KeyFrame(Duration.millis(CLOCK_REFRESH_MS), event -> updateStats()));
        clock.setCycleCount(Animation.INDEFINITE);
    }

    /** Called by Main right after the FXML is loaded. */
    public void setMain(Main main) {
        this.main = main;
    }

    /** Starts a new random game of the given difficulty. */
    public void startGame(Difficulty difficulty) {
        game = new GameManager(difficulty);
        difficultyLabel.setText(difficulty.getLabel());
        refreshScreen();
        clock.playFromStart();
    }

    @FXML
    private void handleRestart() {
        game.restart();
        refreshScreen();
        clock.playFromStart();
    }

    @FXML
    private void handleMenu() {
        clock.stop();
        main.showMenu();
    }

    private void handleTileClick(int row, int col) {
        if (!game.moveTile(row, col)) {
            return; // illegal move: nothing changes
        }
        refreshScreen();
        if (game.isSolved()) {
            clock.stop();
            // runLater lets the final board be drawn before the dialog opens
            Platform.runLater(this::showVictoryDialog);
        }
    }

    private void refreshScreen() {
        renderBoard();
        updateStats();
    }

    /**
     * Rebuilds the grid from the current board. The grid is created in Java (not in FXML)
     * because its size (3x3 or 4x4) is only known at run time.
     */
    private void renderBoard() {
        boardGrid.getChildren().clear();

        int size = game.getSize();
        double tileSize = (BOARD_PIXELS - boardGrid.getHgap() * (size - 1)) / size;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = game.getTile(row, col);
                if (value == 0) {
                    boardGrid.add(createEmptySlot(tileSize), col, row); // GridPane.add(node, column, row)
                } else {
                    boardGrid.add(createTile(value, row, col, tileSize, size), col, row);
                }
            }
        }
    }

    private Button createTile(int value, int row, int col, double tileSize, int boardSize) {
        Button tile = new Button(String.valueOf(value));
        tile.setPrefSize(tileSize, tileSize);
        tile.getStyleClass().addAll("tile", "tile-size-" + boardSize);
        if (game.canMove(row, col)) {
            tile.getStyleClass().add("tile-movable");
        }
        tile.setOnAction(event -> handleTileClick(row, col));
        return tile;
    }

    private Region createEmptySlot(double tileSize) {
        Region slot = new Region();
        slot.setPrefSize(tileSize, tileSize);
        slot.getStyleClass().add("empty-slot");
        return slot;
    }

    private void updateStats() {
        movesLabel.setText("Moves: " + game.getMoveCount());
        timeLabel.setText("Time: " + game.getElapsedFormatted());
    }

    private void showVictoryDialog() {
        ButtonType newGameButton = new ButtonType("New Game", ButtonData.OK_DONE);
        ButtonType menuButton = new ButtonType("Main Menu", ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", newGameButton, menuButton);
        alert.initOwner(boardGrid.getScene().getWindow());
        alert.setTitle("Puzzle solved");
        alert.setHeaderText("Congratulations, you solved the puzzle!");
        alert.setContentText("Moves: " + game.getMoveCount() + "\nTime: " + game.getElapsedFormatted());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == newGameButton) {
            startGame(game.getDifficulty());
        } else {
            handleMenu(); // "Main Menu" button, or the dialog was closed
        }
    }
}
