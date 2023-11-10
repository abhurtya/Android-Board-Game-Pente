package edu.ramapo.abhurtya.pente.model;

import java.util.List;
import java.util.Scanner;

import edu.ramapo.abhurtya.pente.utils.Logger;

public class Round {
    private Board board;
    private Player humanPlayer;
    private Player computerPlayer;

    private Logger logger;
    private boolean endRound = false;
    private static Scanner scanner = new Scanner(System.in);

    public Round(Player human, Player computer) {
        this.humanPlayer = human;
        this.computerPlayer = computer;
        resetPlayers();
        this.logger = new Logger();
        this.board = new Board(logger);
    }

    public Round(Player human, Player computer, Board loadedBoard) {
        this.humanPlayer = human;
        this.computerPlayer = computer;
        this.humanPlayer.setPoints(0);
        this.computerPlayer.setPoints(0);
        this.board = loadedBoard;
        this.logger = new Logger();
    }

    private void resetPlayers() {
        this.humanPlayer.setCaptures(0);
        this.computerPlayer.setCaptures(0);
        this.humanPlayer.setPoints(0);
        this.computerPlayer.setPoints(0);
    }

    public void resetRound(){
        resetPlayers();
        this.board.resetBoard();
    }

    public char tossHumanComputer( int userChoice) {
        // 1 head 2 tails
        int tossResult = (int) (Math.random() * 2) + 1;
        char firstPlayerSymbol = (tossResult == userChoice) ? 'H' : 'C';

        return firstPlayerSymbol;
    }


    public Board getBoard(){
        return board;
    }


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
                System.out.println(currentPlayer.getPlayerType() + " wins!--due to 5 in a row");
                // 5 points to the player with 5 in a row
                currentPlayer.addPoints(5);
                return true;
            }
        }
        return false;
    }

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

    public boolean checkForFiveCaptures(Player currentPlayer) {
        if (currentPlayer.getCaptures() >= 5) {
            System.out.println(currentPlayer.getPlayerType() + " wins!--due to 5 captures of opponent stones");
            return true;
        }
        return false;
    }

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
                System.out.println(currentPlayer.getPlayerType() + " with symbol " + symbol + " captured a pair of " + opponentSymbol + " stones!\n\n");
            }
        }
        return capture;
    }


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


    public Pair<Integer, Integer> play(char firstPlayerSymbol) {
        if (firstPlayerSymbol == ' ') {
            firstPlayerSymbol = tossHumanComputer(1);
        }
        char currentSymbol = 'W'; // The first player plays white stones.
        Player currentPlayer = (firstPlayerSymbol == 'H') ? humanPlayer : computerPlayer;

        playGame(currentPlayer, currentSymbol);

        return new Pair<>(humanPlayer.getPoints(), computerPlayer.getPoints());
    }

    public Pair<Integer, Integer> resume(Player currentPlayer, char currentSymbol) {
        playGame(currentPlayer, currentSymbol);

        return new Pair<>(humanPlayer.getPoints(), computerPlayer.getPoints());
    }

}
