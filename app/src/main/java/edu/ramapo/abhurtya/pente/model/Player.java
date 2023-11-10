package edu.ramapo.abhurtya.pente.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;



public abstract class Player {

    private int points;
    private int captures;

    private char symbol;

    public Player() {
        this.points = 0;
        this.captures = 0;
        this.symbol = '*';
    }

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

    public Pair<Integer, Integer> strategy(Board board, char symbol) {
        // Strategy for first and second moves
        if (board.checkFirstMoveSecondMove(symbol) == 0 && symbol == 'W') {
            System.out.println("used strategy firstmove");
            return firstMoveStrategy();
        } else if (board.checkFirstMoveSecondMove(symbol) == 1 && symbol == 'W' && board.getCell(9, 9) == 'W') {
            System.out.println("used strategy second move");
            return secondMoveStrategy(board);
        }

        Pair<Integer, Integer> bestMove = findBestStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            return bestMove;
        }

        bestMove = defaultStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            return bestMove;
        }

        return randomStrategy(board);
    }

    private Pair<Integer, Integer> findBestStrategy(Board board, char symbol) {
        Pair<Integer, Integer> bestMove;

        bestMove = fiveRowStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            System.out.println("Strategy: Make a winning row of 5");
            return bestMove;
        }

        bestMove = blockFiveRowStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            System.out.println("Strategy: Block Opponent's 5 in a row win");
            return bestMove;
        }

        bestMove = blockCaptureStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            System.out.println("Strategy: Defend Against Capture");
            return bestMove;
        }

        bestMove = checkCaptureStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            System.out.println("Strategy: Capture Opponent's Stones");
            return bestMove;
        }

        bestMove = snakeStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            System.out.println("Strategy: Form a Chain of Stones");
            return bestMove;
        }

        bestMove = blockSnakeStrategy(board, symbol);
        if (isMoveOk(bestMove)) {
            System.out.println("Strategy: Block Opponent's Chain");
            return bestMove;
        }

        System.out.println("Strategy: (Default strategy)");
        return bestMove; // Default to random strategy
    }

    private boolean isMoveOk(Pair<Integer, Integer> move) {
        return move.getKey() != -1;
    }

    private Pair<Integer, Integer> firstMoveStrategy() {
        return new Pair<>(9, 9); // J10
    }

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

    private Pair<Integer, Integer> randomStrategy(Board board) {
        Random rnd = new Random();
        int x, y;
        do {
            x = 2 + rnd.nextInt(15);
            y = 2 + rnd.nextInt(15);
        } while (!board.isValidMove(x, y));
        return new Pair<>(x, y);
    }

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


    // Block Win Strategy
    private Pair<Integer, Integer> blockFiveRowStrategy(Board board, char symbol) {
        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
        return fiveRowStrategy(board, opponentSymbol);
    }

    // Check Capture Strategy
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

    // Block Capture Strategy
    private Pair<Integer, Integer> blockCaptureStrategy(Board board, char symbol) {
        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
        return checkCaptureStrategy(board, opponentSymbol);
    }

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

    // Block Snake Strategy
    private Pair<Integer, Integer> blockSnakeStrategy(Board board, char symbol) {
        char opponentSymbol = (symbol == 'W') ? 'B' : 'W';
        return snakeStrategy(board, opponentSymbol);
    }


}