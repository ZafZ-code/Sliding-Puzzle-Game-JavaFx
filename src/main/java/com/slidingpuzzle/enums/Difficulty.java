package com.slidingpuzzle.enums;

/**
 * The available puzzle sizes.
 * Each constant stores everything that depends on the difficulty,
 * so no other class needs "if (size == 3) ..." logic.
 */
public enum Difficulty {

    EASY("3 × 3  ·  Easy", 3, 100),
    HARD("4 × 4  ·  Hard", 4, 200);

    private final String label;
    private final int size;          // the board is size x size
    private final int shuffleMoves;  // how many random legal moves scramble the board

    Difficulty(String label, int size, int shuffleMoves) {
        this.label = label;
        this.size = size;
        this.shuffleMoves = shuffleMoves;
    }

    public String getLabel() {
        return label;
    }

    public int getSize() {
        return size;
    }

    public int getShuffleMoves() {
        return shuffleMoves;
    }
}
