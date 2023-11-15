package edu.ramapo.abhurtya.pente.model;

import java.util.List;
import java.util.Scanner;

import edu.ramapo.abhurtya.pente.utils.Logger;

/**
 * Represents a round of Pente, managing the board, players, and game logic.
 */

public class Round {
    private Board board;
    private Player humanPlayer;
    private Player computerPlayer;

    private boolean endRound = false;
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for a new round with fresh players and a new board.
     * @param human The human player.
     * @param computer The computer player.
     */
    public Round(Player human, Player computer) {
        this.humanPlayer = human;
        this.computerPlayer = computer;
        resetPlayers();
        this.board = new Board();
    }

    /**
     * Constructor for a round with pre-loaded players and a pre-loaded board.
     * @param human The human player.
     * @param computer The computer player.
     * @param loadedBoard The pre-loaded board for the game.
     */
    public Round(Player human, Player computer, Board loadedBoard) {
        this.humanPlayer = human;
        this.computerPlayer = computer;
        this.humanPlayer.setPoints(0);
        this.computerPlayer.setPoints(0);
        this.board = loadedBoard;
    }

    /**
     * resets the players' scores and captures to 0.
     * This is used when starting a new game.
     */
    private void resetPlayers() {
        this.humanPlayer.setCaptures(0);
        this.computerPlayer.setCaptures(0);
        this.humanPlayer.setPoints(0);
        this.computerPlayer.setPoints(0);
    }

    /**
     * resets the players' scores and captures to 0 and the board to a new board.
     */
    public void resetRound(){
        resetPlayers();
        this.board.resetBoard();
    }

    /**
     * Determines the first player based on a coin toss
     * @return The symbol of the first player.
     */
    public char tossHumanComputer( int userChoice) {
        // 1 head 2 tails
        int tossResult = (int) (Math.random() * 2) + 1;
        char firstPlayerSymbol = (tossResult == userChoice) ? 'H' : 'C';

        return firstPlayerSymbol;
    }

    /**
     * Returns the board of current round.
     * @return The board of current round.
     */
    public Board getBoard(){
        return board;
    }


    /**
     * Checks if the current player has five stones in a row.
     * @param symbol The symbol of the current player ('W' or 'B').
     * @param currentPlayer The player whose move is being checked.
     * @return true if there are five consecutive stones, false otherwise.
     */
    public boolean checkForFiveInARow(char symbol, Player currentPlayer) {
        Pair<Integer, Integer> lastMove = currentPlayer.getLocation();
        int x = lastMove.getKey();
        int y = lastMove.getValue();

        List<Pair<Integer, Integer>> directions = List.of(
                new Pair<>(1, 0), // x-axis
                new Pair<>(0, 1), // y-axis
                new Pair<>(1, 1), // x=y line
                new Pair<>(1, -1) // x=-y line
        );

        for (Pair<Integer, Integer> direction : directions) {
            int dx = direction.getKey();
            int dy = direction.getValue();
            int count = 1; // Counting the last move itself

            count += board.countConsecutiveStones(x, y, dx, dy, symbol);
            count += board.countConsecutiveStones(x, y, -dx, -dy, symbol);

            if (count >= 5) {
                // 5 points to the player with 5 in a row
                currentPlayer.addPoints(5);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current player has four stones in a row.
     * @param symbol The symbol of the current player ('W' or 'B').
     * @param currentPlayer The player whose move is being checked.
     * @return count  of four consecutive stones
     */

    public int numFourInARow(char symbol) {
        int fourInARow = 0;
        List<Pair<Integer, Integer>> directions = List.of(
                new Pair<>(1, 0),
                new Pair<>(0, 1),
                new Pair<>(1, 1),
                new Pair<>(1, -1)
        );

        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                if (board.getCell(i, j) != symbol) continue;

                for (Pair<Integer, Integer> direction : directions) {
                    int dx = direction.getKey();
                    int dy = direction.getValue();
                    int count = 1; // Counting the current cell itself
                    count += board.countConsecutiveStones(i, j, dx, dy, symbol);

                    if (count == 4) {
                        // Making sure it is NOT a part of five in a row
                        int nextX = i + 4 * dx;
                        int nextY = j + 4 * dy;
                        int prevX = i - dx;
                        int prevY = j - dy;
                        if ((nextX < 0 || nextX >= 19 || nextY < 0 || nextY >= 19 || board.getCell(nextX, nextY) != symbol) &&
                                (prevX < 0 || prevX >= 19 || prevY < 0 || prevY >= 19 || board.getCell(prevX, prevY) != symbol)) {
                            fourInARow++;
                        }
                    }
                }
            }
        }

        return fourInARow;
    }


    /**
     * Checks if the current player has made at least five captures.
     * @param currentPlayer The player to check for captures.
     * @return true if the player has made five or more captures, false otherwise.
     */
    public boolean checkForFiveCaptures(Player currentPlayer) {
        if (currentPlayer.getCaptures() >= 5) {
            return true;
        }
        return false;
    }

    /**
     * Checks for and handles captures made by the current player's move.
     * @param symbol The symbol of the current player.
     * @param currentPlayer The player whose move is being checked.
     * @return true if a capture is made, false otherwise.
     */
    public boolean checkForCapture(char symbol, Player currentPlayer) {
        Pair<Integer, Integer> lastMove = currentPlayer.getLocation();
        int x = lastMove.getKey();
        int y = lastMove.getValue();
        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
        boolean capture = false;

        // Checking in different possible directions
        List<Pair<Integer, Integer>> directions = List.of(
                new Pair<>(1, 0), new Pair<>(0, 1), new Pair<>(1, 1), new Pair<>(1, -1),
                new Pair<>(-1, 0), new Pair<>(0, -1), new Pair<>(-1, -1), new Pair<>(-1, 1)
        );

        for (Pair<Integer, Integer> direction : directions) {
            int dx = direction.getKey();
            int dy = direction.getValue();

            if (board.captureStones(x, y, dx, dy, symbol)) {
                capture = true;
                currentPlayer.addCaptures();

                String playerColor = (symbol == 'W') ? "White" : "Black";
                String opponentColor = (symbol == 'W') ? "Black" : "White";
                Logger.getInstance().addLog(currentPlayer.getPlayerType() + ", " + playerColor + " captured a pair of " + opponentColor + " stones!");
            }
        }
        return capture;
    }


    /**
     * calculates the score of the current player and adds it to the player's  attribute
     * @param symbol The symbol of the current player.
     * @param player The player whose score is being calculated.
     */

    public void calculateScore(char symbol, Player player) {
        // 5 points for 5 in a row
        // This is already handled in the checkForFiveInARow function, no need to do anything here

        // 1 point per capture
        int capturePoints = player.getCaptures();
        player.addPoints(capturePoints);

        // 1 point per 4 in a row
        int fourInARowPoints = numFourInARow(symbol);
        player.addPoints(fourInARowPoints);
    }


}
