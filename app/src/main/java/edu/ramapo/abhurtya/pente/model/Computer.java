package edu.ramapo.abhurtya.pente.model;
import java.io.Serializable;

/**
 * Represents the computer player in the Pente game.
 * Extends the Player class and implements Serializable for saving game state.
 */

public class Computer extends Player implements Serializable {

    private Pair<Integer, Integer> location;

    public Computer() {
        this.location = new Pair<>(-1, -1);
    }

    /**
     * The computer's play logic. Determines the best move using a strategy function.
     * @param board The current state of the game board.
     * @param symbol The symbol ('W' or 'B') representing the computer's synbol.
     */
    @Override
    public void play(Board board, char symbol) {
        // Use the strategy function to get the best move
        Pair<Integer, Integer> bestMove = strategy(board, symbol, true);

        // Convert the numerical coordinates to a human-readable representation
        char column = (char)('A' + bestMove.getValue());
        int row = bestMove.getKey() + 1;

        System.out.println("Computer chose position: " + column + row);

        // Validation done by the strategy
        setLocation(bestMove.getKey(), bestMove.getValue());
        board.setCell(bestMove.getKey(), bestMove.getValue(), symbol);
    }

    /**
     * Sets the location of the computer's move on the board.
     * @param x The row index of the move.
     * @param y The column index of the move.
     */
    @Override
    public void setLocation(int x, int y) {
        this.location = new Pair<>(x, y);
    }

    /**
     * Gets the current location of the computer's move on the board.
     * @return The current location as a Pair of integers.
     */
    @Override
    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    /**
     * Returns the player type as a string.
     * @return A string indicating this is a Computer player.
     */
    @Override
    public String getPlayerType() {
        return "Computer";
    }


}