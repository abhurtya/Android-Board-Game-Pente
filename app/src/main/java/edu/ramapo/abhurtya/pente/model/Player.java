package edu.ramapo.abhurtya.pente.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

import edu.ramapo.abhurtya.pente.utils.Logger;


/**
 * Represents a player in the Pente game.
 * This is an abstract class that is extended by Human and Computer.
 * Implements Serializable for saving game state.
 */

public abstract class Player implements Serializable {

    private int points;
    private int captures;

    private char symbol;

    /**
     * Default constructor for Player. Initializes points and captures to 0 and symbol to '*'.
     */
    public Player() {
        this.points = 0;
        this.captures = 0;
        this.symbol = '*';
    }

    // Abstract methods
    public abstract void play(Board board, char symbol);
    public abstract Pair<Integer, Integer> getLocation();

    public abstract void setLocation(int x, int y);
    public abstract String getPlayerType();

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public int getPoints() {
        return points;
    }

    public int getCaptures() {
        return captures;
    }

    public void addPoints(int p) {
        this.points += p;
    }

    public void addCaptures() {
        this.captures++;
    }

    public void setPoints(int p) {
        this.points = p;
    }

    public void setCaptures(int c) {
        this.captures = c;
    }

    /**
     * Strategy method to determine the player's next move.
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @param isComputer A flag indicating if the strategy is for the computer player.
     * @return The best move as a Pair of integers (row, column).
     */

    public Pair<Integer, Integer> strategy(Board board, char symbol, boolean isComputer) {
        Pair<Integer, Integer> bestMove;
        String moveType = isComputer ? "Computer played at " : "AI suggests Human to play at ";

        // Strategy for first and second moves
        if (board.checkFirstMoveSecondMove(symbol) == 0 && symbol == 'W') {
            Logger.getInstance().addLog(moveType+ "J10 to make the first move");
            return firstMoveStrategy();
        } else if (board.checkFirstMoveSecondMove(symbol) == 1 && symbol == 'W' && board.getCell(9, 9) == 'W') {
            bestMove = secondMoveStrategy(board);
            //convert to string like 9,9 to J10
            Logger.getInstance().addLog(moveType+ (char)(bestMove.getValue()+65) + (19 - bestMove.getKey()) +" to make the second move");
            return bestMove;
        }

        bestMove = findBestStrategy(board, symbol , isComputer);
        if (isMoveOk(bestMove)) {
            return bestMove;
        }

        //base case
        bestMove = randomStrategy(board);
        Logger.getInstance().addLog(moveType+ (char)(bestMove.getValue()+65) + (19-bestMove.getKey()) +" to make a random Move");

        return bestMove;
    }

    /**
     * Strategy method to determine the player's next best move. goes thorungh all the strategies and returns until found best move
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @param isComputer A flag indicating if the strategy is for the computer player.
     * @return The best move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> findBestStrategy(Board board, char symbol , boolean isComputer   ) {
        Pair<Integer, Integer> bestMove;
        String moveType = isComputer ? "Computer played at " : "AI suggests Human to play at ";

        bestMove = fiveRowStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            Logger.getInstance().addLog(moveType + (char)(bestMove.getValue()+65) + (19-bestMove.getKey()) + " to make 5 in a row");
            return bestMove;
        }

        bestMove = blockFiveRowStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            Logger.getInstance().addLog(moveType+ (char)(bestMove.getValue()+65) + (19-bestMove.getKey()) +" to block Opponent's 5 in a row win");
            return bestMove;
        }

        bestMove = blockCaptureStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            Logger.getInstance().addLog(moveType+ (char)(bestMove.getValue()+65) + (19-bestMove.getKey()) +" to block Opponent's Capture");
            return bestMove;
        }

        bestMove = checkCaptureStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            Logger.getInstance().addLog(moveType+ (char)(bestMove.getValue()+65) + (19-bestMove.getKey()) +" to capture Opponent's Stone");
            return bestMove;
        }

        bestMove = snakeStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            Logger.getInstance().addLog(moveType+ (char)(bestMove.getValue()+65) + (19-bestMove.getKey()) +" to make Continuous Chain");
            return bestMove;
        }

        bestMove = blockSnakeStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            Logger.getInstance().addLog(moveType+ (char)(bestMove.getValue()+65) + (19-bestMove.getKey()) +" to block Opponent's Continuous Chain");
            return bestMove;
        }

        bestMove = defaultStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            Logger.getInstance().addLog(moveType + (char)(bestMove.getValue()+65) + (19- bestMove.getKey()) + " next to its own stone");
            return bestMove;
        }

        return bestMove; // Default to random strategy
    }

    /**
     * Checks if the recommended move is valid.
     * @param move The recommended move to be checked.
     * @return true if the move is valid, false otherwise.
     */
    private boolean isMoveOk(Pair<Integer, Integer> move) {
        return move.getKey() != -1;
    }

    /**
     * Strategy for the first move.
     * @return The first move as a Pair of integers (row, column) which is always J10.
     */
    private Pair<Integer, Integer> firstMoveStrategy() {
        return new Pair<>(9, 9); // J10
    }

    /**
     * Strategy for the second move.
     * @param board The game board.
     * @return The second move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> secondMoveStrategy(Board board) {
        int[][] directions = { {-4, 0}, {4, 0}, {0, -4}, {0, 4} };

        Random rnd = new Random();
        for (int i = 0; i < directions.length; i++) {
            int index = rnd.nextInt(directions.length);
            int[] temp = directions[index];
            directions[index] = directions[i];
            directions[i] = temp;
        }

        for (int[] direction : directions) {
            int x = 9 + direction[0];
            int y = 9 + direction[1];

            if (board.isValidMove(x, y)) {
                return new Pair<>(x, y);
            }
        }

        return new Pair<>(-1, -1); // should never reach here because it's the second move
    }

    /**
     * Strategy for a random move.
     * @param board The game board.
     * @return A random move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> randomStrategy(Board board) {
        Random rnd = new Random();
        int x, y;
        do {
            x = 2 + rnd.nextInt(15);
            y = 2 + rnd.nextInt(15);
        } while (!board.isValidMove(x, y));
        return new Pair<>(x, y);
    }

    /**
     * Strategy for a default move.
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @return A default move as a Pair of integers (row, column) which will be next to its own stone.
     */
    private Pair<Integer, Integer> defaultStrategy(Board board, char symbol) {
        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board.getCell(i, j) == symbol) {
                    List<Pair<Integer, Integer>> directions = Arrays.asList(
                            new Pair<>(1, 0),
                            new Pair<>(0, 1),
                            new Pair<>(1, 1),
                            new Pair<>(1, -1)
                    );

                    for (Pair<Integer, Integer> direction : directions) {
                        int dx = direction.getKey();
                        int dy = direction.getValue();
                        int x = i + dx;
                        int y = j + dy;

                        if (board.isValidMove(x, y) && !board.isCapturePossible(x, y, dx, dy, opponentSymbol) && !board.isCapturePossible(x, y, -dx, -dy, opponentSymbol)) {
                            return new Pair<>(x, y);
                        }
                    }
                }
            }
        }
        return new Pair<>(-1, -1);
    }

    /**
     * Strategy for a five in a row.
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @return A five in a row move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> fiveRowStrategy(Board board, char symbol) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board.getCell(i, j) == '*') {
                    List<Pair<Integer, Integer>> directions = Arrays.asList(
                            new Pair<>(1, 0),
                            new Pair<>(0, 1),
                            new Pair<>(1, 1),
                            new Pair<>(1, -1)
                    );

                    for (Pair<Integer, Integer> direction : directions) {
                        int dx = direction.getKey();
                        int dy = direction.getValue();

                        int totalConsecutiveStones = 1 + board.countConsecutiveStones(i, j, dx, dy, symbol) + board.countConsecutiveStones(i, j, -dx, -dy, symbol);
                        if (totalConsecutiveStones >= 5) {
                            return new Pair<>(i, j);
                        }
                    }
                }
            }
        }
        return new Pair<>(-1, -1);
    }


    /**
     * Strategy for blocking a five in a row.
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @return A blocking move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> blockFiveRowStrategy(Board board, char symbol) {
        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
        return fiveRowStrategy(board, opponentSymbol);
    }

    /**
     * Strategy for capturing an opponent's stone.
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @return A capturing move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> checkCaptureStrategy(Board board, char symbol) {
        Pair<Integer, Integer> bestMove = new Pair<>(-1, -1);
        int[][] directions = { {1, 0}, {0, 1}, {1, 1}, {1, -1}, {-1, 0}, {0, -1}, {-1, -1}, {-1, 1} };

        int maxCapturePossible = 0;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board.getCell(i, j) == '*') {
                    int capturePossible = 0;
                    for (int[] direction : directions) {
                        int dx = direction[0];
                        int dy = direction[1];
                        if (board.isCapturePossible(i, j, dx, dy, symbol)) {
                            capturePossible++;
                        }
                    }
                    if (capturePossible > maxCapturePossible) {
                        maxCapturePossible = capturePossible;
                        bestMove = new Pair<>(i, j);
                    }
                }
            }
        }
        return bestMove;  // If no capture found, returns (-1,-1)
    }

    /**
     * Strategy for blocking an opponent's capture.
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @return A blocking move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> blockCaptureStrategy(Board board, char symbol) {
        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
        return checkCaptureStrategy(board, opponentSymbol);
    }

    /**
     * Strategy for creating a chain of consequetive stones.
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @return A chain move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> snakeStrategy(Board board, char symbol) {
        int[][] directions = { {1, 0}, {0, 1}, {1, 1}, {1, -1} };

        for (int targetLength = 4; targetLength >= 3; --targetLength) {
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    if (board.getCell(i, j) == '*') {
                        for (int[] direction : directions) {
                            int dx = direction[0];
                            int dy = direction[1];
                            int totalConsecutiveStones = 1 + board.countConsecutiveStones(i, j, dx, dy, symbol) + board.countConsecutiveStones(i, j, -dx, -dy, symbol);
                            if (totalConsecutiveStones == targetLength) {
                                char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
                                if (!board.isCapturePossible(i, j, dx, dy, opponentSymbol) && !board.isCapturePossible(i, j, -dx, -dy, opponentSymbol)) {
                                    return new Pair<>(i, j);
                                }
                            }
                        }
                    }
                }
            }
        }
        return new Pair<>(-1, -1);
    }

    /**
     * Strategy for blocking an opponent's chain of consequetive sotnes.
     * @param board The game board.
     * @param symbol The symbol of the player.
     * @return A blocking move as a Pair of integers (row, column).
     */
    private Pair<Integer, Integer> blockSnakeStrategy(Board board, char symbol) {
        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
        return snakeStrategy(board, opponentSymbol);
    }


}