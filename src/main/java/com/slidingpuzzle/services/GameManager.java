package com.slidingpuzzle.services;

import com.slidingpuzzle.enums.Difficulty;
import com.slidingpuzzle.models.PuzzleBoard;

/**
 * Coordinates one game: the board, the move counter, the clock and the win state.
 * It has no JavaFX code. The controller asks it questions ("how many moves?",
 * "is it solved?") and tells it what the player did ("tile clicked").
 *
 * The clock is computed from System.nanoTime(), so it is exact even if the screen
 * is refreshed late. The controller only needs to re-read it from time to time.
 */
public class GameManager {

    private final Difficulty difficulty;
    private final PuzzleGenerator generator;

    private PuzzleBoard initialBoard; // kept so "Restart" replays the same puzzle
    private PuzzleBoard board;        // the board the player is changing
    private int moveCount;
    private boolean solved;
    private long startNanos;
    private long endNanos;

    public GameManager(Difficulty difficulty) {
        this(difficulty, new PuzzleGenerator());
    }

    public GameManager(Difficulty difficulty, PuzzleGenerator generator) {
        this.difficulty = difficulty;
        this.generator = generator;
        startNewGame();
    }

    /** Generates a brand-new random puzzle and starts the clock. */
    public void startNewGame() {
        initialBoard = generator.generate(difficulty);
        beginRound();
    }

    /** Replays the same puzzle from its starting position: moves = 0, clock = 0. */
    public void restart() {
        beginRound();
    }

    private void beginRound() {
        board = initialBoard.copy();
        moveCount = 0;
        solved = false;
        startNanos = System.nanoTime();
        endNanos = 0;
    }

    /**
     * Tries to slide the tile at (row, col).
     *
     * @return true if the tile moved; false if the move was illegal or the game is already won
     *         (in that case nothing changes, and the move counter does not increase)
     */
    public boolean moveTile(int row, int col) {
        if (solved || !board.moveTile(row, col)) {
            return false;
        }
        moveCount++;
        if (board.isSolved()) {
            solved = true;
            endNanos = System.nanoTime(); // freezes the clock
        }
        return true;
    }

    // ----- read-only information for the user interface -----

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getSize() {
        return board.getSize();
    }

    public int getTile(int row, int col) {
        return board.getTile(row, col);
    }

    public boolean canMove(int row, int col) {
        return board.canMove(row, col);
    }

    public int getMoveCount() {
        return moveCount;
    }

    public boolean isSolved() {
        return solved;
    }

    public long getElapsedSeconds() {
        long end = solved ? endNanos : System.nanoTime();
        return (end - startNanos) / 1_000_000_000L;
    }

    /** Elapsed time as mm:ss, for example "01:07". */
    public String getElapsedFormatted() {
        long seconds = getElapsedSeconds();
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}
