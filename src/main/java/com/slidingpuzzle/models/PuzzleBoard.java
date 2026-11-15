package com.slidingpuzzle.models;

/**
 * The state of a sliding puzzle: an N x N grid of numbers where 0 means "empty".
 * A new board starts in the solved configuration:
 *
 *     1 2 3
 *     4 5 6
 *     7 8 _      (_ is stored as 0)
 *
 * This class knows nothing about JavaFX, so it can be tested on its own.
 */
public class PuzzleBoard {

    public static final int EMPTY = 0;

    private final int size;
    private final int[][] tiles;
    private int emptyRow;
    private int emptyCol;

    /** Creates a solved board of the given size. */
    public PuzzleBoard(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Board size must be at least 2, got " + size);
        }
        this.size = size;
        this.tiles = new int[size][size];
        fillSolved();
    }

    /** Private copy constructor, used by {@link #copy()}. */
    private PuzzleBoard(PuzzleBoard other) {
        this.size = other.size;
        this.tiles = new int[size][size];
        for (int row = 0; row < size; row++) {
            this.tiles[row] = other.tiles[row].clone();
        }
        this.emptyRow = other.emptyRow;
        this.emptyCol = other.emptyCol;
    }

    /** Returns an independent copy (changing the copy never changes this board). */
    public PuzzleBoard copy() {
        return new PuzzleBoard(this);
    }

    private void fillSolved() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                tiles[row][col] = row * size + col + 1;
            }
        }
        emptyRow = size - 1;
        emptyCol = size - 1;
        tiles[emptyRow][emptyCol] = EMPTY;
    }

    public int getSize() {
        return size;
    }

    /** Returns the number on the tile at (row, col), or {@link #EMPTY} for the empty cell. */
    public int getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getEmptyRow() {
        return emptyRow;
    }

    public int getEmptyCol() {
        return emptyCol;
    }

    /** A tile can move only if it is directly next to the empty cell (up, down, left or right). */
    public boolean canMove(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        int distance = Math.abs(row - emptyRow) + Math.abs(col - emptyCol);
        return distance == 1;
    }

    /**
     * Slides the tile at (row, col) into the empty cell.
     *
     * @return true if the move was legal and the board changed, false otherwise
     *         (an illegal move leaves the board untouched)
     */
    public boolean moveTile(int row, int col) {
        if (!canMove(row, col)) {
            return false;
        }
        tiles[emptyRow][emptyCol] = tiles[row][col];
        tiles[row][col] = EMPTY;
        emptyRow = row;
        emptyCol = col;
        return true;
    }

    /** True when the numbers are in ascending order with the empty cell in the bottom-right corner. */
    public boolean isSolved() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                boolean lastCell = (row == size - 1 && col == size - 1);
                int expected = lastCell ? EMPTY : row * size + col + 1;
                if (tiles[row][col] != expected) {
                    return false;
                }
            }
        }
        return true;
    }
}
