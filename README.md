# Sliding Puzzle (JavaFX)

3×3 and 4×4 sliding puzzle with move counter, timer, restart, menu and win dialog.

## Run

```
mvn javafx:run
```

Requirements: a recent JDK (25+ recommended for JavaFX 27) and Maven.
If you use another JavaFX version, change `<javafx.version>` in `pom.xml`.

### VS Code
1. Install the "Extension Pack for Java".
2. File > Open Folder > the `SlidingPuzzle` folder (the one with `pom.xml`).
3. Wait for Maven to import, then run `mvn javafx:run` in the terminal.

## Structure

```
src/main/java/com/slidingpuzzle/
  Main.java                    window + screen switching (FXMLLoader)
  enums/Difficulty.java        EASY (3x3), HARD (4x4)
  models/PuzzleBoard.java      grid state, legal-move check, win check
  services/PuzzleGenerator.java  random solvable puzzle (random legal moves from solved)
  services/GameManager.java    moves, timer, win state, restart
  controllers/MenuController.java
  controllers/PuzzleController.java   draws the GridPane, handles clicks
src/main/resources/com/slidingpuzzle/
  fxml/menu.fxml, fxml/puzzle.fxml
  css/style.css
```

## Flow

```
click tile -> PuzzleController.handleTileClick -> GameManager.moveTile -> PuzzleBoard.moveTile
           -> renderBoard() + updateStats() -> if solved: stop clock + victory dialog
```
