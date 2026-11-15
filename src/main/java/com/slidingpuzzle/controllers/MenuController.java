package com.slidingpuzzle.controllers;

import com.slidingpuzzle.Main;

import com.slidingpuzzle.enums.Difficulty;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

/**
 * Controller of menu.fxml.
 *
 * How FXML and this class are connected:
 *  - menu.fxml declares fx:controller="com.slidingpuzzle.controllers.MenuController",
 *    so FXMLLoader creates one instance of this class.
 *  - A component with fx:id="easyRadio" is injected into the field named easyRadio
 *    (the field needs @FXML because it is private).
 *  - onAction="#handleStart" calls the method handleStart() when the button is clicked.
 */
public class MenuController {

    @FXML
    private RadioButton easyRadio;   // fx:id="easyRadio" in menu.fxml

    @FXML
    private RadioButton hardRadio;   // fx:id="hardRadio" in menu.fxml

    private Main main;

    /** Called by Main right after the FXML is loaded. */
    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void handleStart() {
        Difficulty difficulty = hardRadio.isSelected() ? Difficulty.HARD : Difficulty.EASY;
        main.showPuzzle(difficulty);
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
