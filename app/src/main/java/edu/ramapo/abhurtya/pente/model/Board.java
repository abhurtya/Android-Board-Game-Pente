package edu.ramapo.abhurtya.pente.model;

import java.util.Arrays;
import edu.ramapo.abhurtya.pente.utils.Logger;

public class Board {

    private char[][] grid;

    public Board() {
        grid = new char[19][19];
        for (char[] row : grid) {
            Arrays.fill(row, '*');
        }
    }

    public boolean isCellEmpty(int x, int y) {
        return getCell(x, y) == '*';
    }

    public boolean isValidMove(int x, int y, char symbol) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19) {
            Logger.getInstance().addLog("Invalid position");
            return false;
        }

        if (!isCellEmpty(x, y)) {
            Logger.getInstance().addLog("Cell already occupied");
            return false;
        }

        if (symbol == 'W' && checkFirstMoveSecondMove('W') == 0) {
            if (!(x == 9 && y == 9)) {
                Logger.getInstance().addLog("First white move must be at J10");
                return false;
            }
        }

        if (symbol == 'W' && checkFirstMoveSecondMove('W') == 1) {
            if (!(Math.abs(x - 9) > 3 || Math.abs(y - 9) > 3)) {
                Logger.getInstance().addLog("Second white move must be 3 steps away from J10");
                return false;
            }
        }

        return true;
    }

    // Overloaded method without the symbol argument
    public boolean isValidMove(int x, int y) {
        return isValidMove(x, y, ' ');
    }

    public boolean setCell(int x, int y, char symbol) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19) {
            Logger.getInstance().addLog("Error: Invalid position for setCell.");
            return false;
        }

        grid[x][y] = symbol;
        return true;
    }

    public char getCell(int x, int y) {
        if (x >= 0 && x < 19 && y >= 0 && y < 19) {
            return grid[x][y];
        }
        return ' ';
    }

    public void resetBoard(){
        for (char[] row : grid) {
            Arrays.fill(row, '*');
        }
    }

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

    public boolean captureStones(int x, int y, int dx, int dy, char symbol) {
        if (isCapturePossible(x, y, dx, dy, symbol)) {
            // Capture the pair
            setCell(x + dx, y + dy, '*');
            setCell(x + 2 * dx, y + 2 * dy, '*');

            // Converting into human representation
            Logger.getInstance().addLog("\n*******  Pairs Captured   ******  " + (char) ('A' + (y + dy)) + (x + dx + 1) + " and " + (char) ('A' + (y + 2 * dy)) + (x + 2 * dx + 1) + "!\n");

            return true;
        }
        return false;
    }

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

    //for debugging only
    @Override
    public String toString() {
        // Create a StringBuilder to build the string representation
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < 19; row++) {
            for (int col = 0; col < 19; col++) {
                // Append the current cell to the builder
                builder.append(getCell(row, col));
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    //  for debugging
    public void setState(char[][] initialState) {
        for (int row = 0; row < initialState.length; row++) {
            for (int col = 0; col < initialState[row].length; col++) {
                this.setCell(row, col, initialState[row][col]);
            }
        }
    }

}

