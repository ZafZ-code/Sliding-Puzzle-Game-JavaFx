package com.slidingpuzzle.services;

import com.slidingpuzzle.enums.Difficulty;
import com.slidingpuzzle.models.PuzzleBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates random puzzles that are ALWAYS solvable.
 *
 * Why it is guaranteed: we start from the solved board and only apply legal moves.
 * Every legal move can be undone by the opposite move, so the player can always
 * walk back to the solved board by undoing our moves in reverse order.
 * (Shuffling the numbers randomly would NOT give this guarantee: half of all
 * permutations of the tiles are unsolvable.)
 */
public class PuzzleGenerator {

    // Neighbour offsets of a cell: up, down, left, right
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private final Random random;

    public PuzzleGenerator() {
        this(new Random());
    }

    /** Lets tests pass a seeded Random to get repeatable puzzles. */
    public PuzzleGenerator(Random random) {
        this.random = random;
    }

    /** Returns a scrambled, solvable board that is never already solved. */
    public PuzzleBoard generate(Difficulty difficulty) {
        PuzzleBoard board;
        do {
            board = scramble(new PuzzleBoard(difficulty.getSize()), difficulty.getShuffleMoves());
        } while (board.isSolved()); // extremely rare, but a "new" puzzle must not be won already
        return board;
    }

    private PuzzleBoard scramble(PuzzleBoard board, int moves) {
        int previousEmptyRow = -1;
        int previousEmptyCol = -1;

        for (int i = 0; i < moves; i++) {
            List<int[]> candidates = new ArrayList<>();
            for (int[] direction : DIRECTIONS) {
                int row = board.getEmptyRow() + direction[0];
                int col = board.getEmptyCol() + direction[1];
                boolean undoesPreviousMove = (row == previousEmptyRow && col == previousEmptyCol);
                if (board.canMove(row, col) && !undoesPreviousMove) {
                    candidates.add(new int[]{row, col});
                }
            }

            // There is always at least one candidate: the empty cell has 2+ neighbours
            // and we exclude at most one of them.
            int[] chosen = candidates.get(random.nextInt(candidates.size()));

            previousEmptyRow = board.getEmptyRow();
            previousEmptyCol = board.getEmptyCol();
            board.moveTile(chosen[0], chosen[1]);
        }
        return board;
    }
}
