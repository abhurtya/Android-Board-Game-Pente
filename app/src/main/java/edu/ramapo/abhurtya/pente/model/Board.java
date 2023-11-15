package edu.ramapo.abhurtya.pente.model;

import java.util.Arrays;
import edu.ramapo.abhurtya.pente.utils.Logger;
import java.io.Serializable;

/**
 * Model Class to represent the game board.
 * This class is responsible for storing the state of the game board and validating moves.
 */
public class Board implements Serializable {

    private char[][] grid;

    /**
     * Constructor for Board.
     * Initializes a 19x19 grid filled with '*' indicating empty cells.
     */
    public Board() {
        grid = new char[19][19];
        for (char[] row : grid) {
            Arrays.fill(row, '*');
        }
    }

    /**
     * Checks if the cell at the given position is empty.
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @return True if the cell is empty, false otherwise.
     */
    public boolean isCellEmpty(int x, int y) {
        return getCell(x, y) == '*';
    }

    /**
     * Checks if the given move is valid.
     * @param x The x coordinate of the move.
     * @param y The y coordinate of the move.
     * @param symbol The symbol of the player making the move.
     * @return True if the move is valid, false otherwise.
     */

    public boolean isValidMove(int x, int y, char symbol) {
        String reason = whyInvalid(x, y, symbol);
        if (!reason.isEmpty()) {
//            Logger.getInstance().addLog(reason);
            return false;
        }
        return true;
    }

    /**
     * Checks why the given move is invalid.
     * @param x The x coordinate of the move.
     * @param y The y coordinate of the move.
     * @param symbol The symbol of the player making the move.
     * @return A string describing why the move is invalid. Returns an empty string if the move is valid.
     */
    public String whyInvalid(int x, int y, char symbol) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19) {
            return "Invalid position";
        }

        if (!isCellEmpty(x, y)) {
            return "Cell already occupied";
        }

        if (symbol == 'W' && checkFirstMoveSecondMove('W') == 0) {
            if (!(x == 9 && y == 9)) {
                return "First white move must be at J10";
            }
        }

        if (symbol == 'W' && checkFirstMoveSecondMove('W') == 1) {
            if (!(Math.abs(x - 9) > 3 || Math.abs(y - 9) > 3)) {
                return "Second white move must be 3 steps away from J10";
            }
        }

        return ""; // if move valid
    }


    // Overloaded method without the symbol argument
    public boolean isValidMove(int x, int y) {
        return isValidMove(x, y, ' ');
    }

    /**
     * Sets the symbol of a player at a specific cell on the board.
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @param symbol The symbol to set at the cell.
     * @return True if the cell was set successfully, false if the coordinates are invalid.
     */
    public boolean setCell(int x, int y, char symbol) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19) {
            Logger.getInstance().addLog("Error: Invalid position for setCell.");
            return false;
        }

        grid[x][y] = symbol;
        return true;
    }

    /**
     * Gets the symbol of the player at a specific cell on the board.
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @return The symbol at the cell, or ' ' if the coordinates are invalid.
     */

    public char getCell(int x, int y) {
        if (x >= 0 && x < 19 && y >= 0 && y < 19) {
            return grid[x][y];
        }
        return ' ';
    }

    /**
     * Resets the board to its initial state.
     */
    public void resetBoard(){
        for (char[] row : grid) {
            Arrays.fill(row, '*');
        }
    }

    /**
     * Checks how many moves have been made by the player with the given symbol.
     * @param symbol The symbol of the player.
     * @return The number of moves made by the player. Returns -1 if more than one move is made.
     */

    public int checkFirstMoveSecondMove(char symbol) {
        int count = 0;
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == symbol) {
                    count++;
                    // More than one move made, return early
                    if (count > 1) return -1;
                }
            }
        }
        return count;
    }

    /**
     * count the number of consecutive stones in a given direction from a starting cell.
     * @param x The x coordinate of the starting cell.
     * @param y The y coordinate of the starting cell.
     * @param dx The x direction for counting consecutive stones.
     * @param dy The y direction for counting consecutive stones.
     * @param symbol The symbol of the player to count
     * @return The number of consecutive stones in the given direction.
     */
    public int countConsecutiveStones(int x, int y, int dx, int dy, char symbol) {
        int count = 0;
        for (int i = 1; i < 5; ++i) {
            int newX = x + i * dx;
            int newY = y + i * dy;

            if (newX < 0 || newX >= 19 || newY < 0 || newY >= 19 || getCell(newX, newY) != symbol) {
                break;
            }

            ++count;
        }
        return count;
    }

    /**
     * Attempts to capture stones on the board.
     * @param x The x coordinate of the starting cell.
     * @param y The y coordinate of the starting cell.
     * @param dx The x direction for capturing.
     * @param dy The y direction for capturing.
     * @param symbol The symbol of the player attempting the capture.
     * @return True if a capture was made, false otherwise.
     */
    public boolean captureStones(int x, int y, int dx, int dy, char symbol) {
        if (isCapturePossible(x, y, dx, dy, symbol)) {
            // Capture the pair
            setCell(x + dx, y + dy, '*');
            setCell(x + 2 * dx, y + 2 * dy, '*');

            // Converting into human representation
            Logger.getInstance().addLog("*******  Pairs Captured   ******  " + (char) ('A' + (y + dy)) + (x + dx + 1) + " and " + (char) ('A' + (y + 2 * dy)) + (x + 2 * dx + 1) + "!");

            return true;
        }
        return false;
    }

    /**
     * Checks if capturing stones is possible in a given direction from a starting cell.
     * @param x The x coordinate of the starting cell.
     * @param y The y coordinate of the starting cell.
     * @param dx The x direction for checking capture.
     * @param dy The y direction for checking capture.
     * @param symbol The symbol of the player attempting the capture.
     * @return True if capturing is possible, false otherwise.
     */
    public boolean isCapturePossible(int x, int y, int dx, int dy, char symbol) {
        // Early return if the index is out of bounds
        for (int i = 1; i <= 3; ++i) {
            int newX = x + i * dx;
            int newY = y + i * dy;
            if (newX < 0 || newX >= 19 || newY < 0 || newY >= 19) {
                return false;
            }
        }

        char first = getCell(x + dx, y + dy);
        char second = getCell(x + 2 * dx, y + 2 * dy);
        char third = getCell(x + 3 * dx, y + 3 * dy);

        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
        return (first == opponentSymbol && second == opponentSymbol && third == symbol);
    }


}

