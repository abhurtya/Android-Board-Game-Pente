package edu.ramapo.abhurtya.pente.model;

import java.io.Serializable;
import java.util.Scanner;
import java.io.Serializable;

/**
 * Represents the human player in the Pente game.
 * Extends the Player class and implements Serializable for saving game state.
 */
public class Human extends Player implements Serializable {

    private Pair<Integer, Integer> location;

    public Human() {

        this.location = new Pair<>(-1, -1);
    }

    /**
     * The human's play logic. This is intended to be implemented through user interaction in the UI.
     * @param board The current state of the game board.
     * @param symbol The symbol ('W' or 'B') representing the human player's pieces.
     */
    @Override
    public void play(Board board, char symbol) {
        // The play method for the human player will be handled in the UI, not here.

    }

    /**
     * Sets the location of the human player's move on the board.
     * @param x The row index of the move.
     * @param y The column index of the move.
     */

    @Override
    public void setLocation(int x, int y) {
        this.location = new Pair<>(x, y);
    }

    /**
     * Gets the current location of the human player's move on the board.
     * @return The current location as a Pair of integers.
     */
    @Override
    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    /**
     * Returns the player type as a string.
     * @return A string indicating this is a Human player.
     */
    @Override
    public String getPlayerType() {
        return "Human";
    }
}
