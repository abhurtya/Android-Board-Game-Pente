package edu.ramapo.abhurtya.pente.model;

import java.util.List;
import java.util.Scanner;

public class Round {
    private Board board;
    private Player humanPlayer;
    private Player computerPlayer;
    private boolean endRound = false;
    private static Scanner scanner = new Scanner(System.in);

    public Round(Player human, Player computer) {
        this.humanPlayer = human;
        this.computerPlayer = computer;
        resetPlayers();
        this.board = new Board();
    }

    public Round(Player human, Player computer, Board loadedBoard) {
        this.humanPlayer = human;
        this.computerPlayer = computer;
        this.humanPlayer.setPoints(0);
        this.computerPlayer.setPoints(0);
        this.board = loadedBoard;
    }

    private void resetPlayers() {
        this.humanPlayer.setCaptures(0);
        this.computerPlayer.setCaptures(0);
        this.humanPlayer.setPoints(0);
        this.computerPlayer.setPoints(0);
    }
    public char tossHumanComputer() {
        char firstPlayerSymbol;
        int call;
        // Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Toss a coin! (1 == heads, 2 == tails): ");
            call = scanner.nextInt();

            if (call == 1 || call == 2) {
                break; // Exit the loop if the input is valid
            } else {
                System.out.println("Invalid choice. Enter 1 for heads, 2 for tails");
                scanner.nextLine(); // Consume the newline
            }
        }

        int tossResult = (int) (Math.random() * 2) + 1;
        System.out.println("Coin toss result: " + (tossResult == 1 ? "Heads" : "Tails"));

        firstPlayerSymbol = (tossResult == call) ? 'H' : 'C';
        System.out.println((firstPlayerSymbol == 'H') ? "You will play first." : "Computer will play first." + "\n");

        return firstPlayerSymbol;
    }

    public void displayCaptures() {
        System.out.println("Captures -\t Human: " + humanPlayer.getCaptures() + ",\t Computer: " + computerPlayer.getCaptures());
    }

    public void takeTurn(Player currentPlayer, char symbol) {
        String userInput;
        System.out.println("\nEnter 'quit' to exit, or 'save' to save game and exit.\nPress any key to continue: ");
        userInput = scanner.nextLine();
        System.out.println();

        if (userInput.equals("save")) {

            String nextPlayer = currentPlayer.getPlayerType().equals("Human") ? "Computer" : "Human";
            //symbol is the current symbol, so must save the opposite symbol
            String nextPlayerStone = (symbol == 'W') ? "Black" : "White";
            if (PenteFileWriter.saveGame(board, humanPlayer, computerPlayer, nextPlayer, nextPlayerStone)) {
                System.out.println("Game saved successfully!");
                //mark endRound member flag true
                endRound = true;

                System.exit(0); // Exiting the program
            } else {
                System.out.println("Failed to save the game.");
            }

        } else if (userInput.equals("quit")) {
            System.out.println("Exited without saving successfully");
            System.exit(0); // Exiting the program
        }

        currentPlayer.play(board, symbol);
        Pair<Integer, Integer> location = currentPlayer.getLocation();
        int x = location.getKey();
        int y = location.getValue();

        board.setCell(x, y, symbol);
        board.displayBoard();
        displayCaptures();

        if (checkForCapture(symbol, currentPlayer)) {
            board.displayBoard();
            displayCaptures();
        }
    }

    public boolean checkForEndOfRound() {
        return endRound;
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

    public void playGame(Player currentPlayer, char currentSymbol) {
        board.displayBoard();

        do {
            takeTurn(currentPlayer, currentSymbol);

            if (checkForFiveInARow(currentSymbol, currentPlayer) || checkForFiveCaptures(currentPlayer)) {
                break;
            }
            // Swap current player and symbol
            currentPlayer = (currentPlayer == humanPlayer) ? computerPlayer : humanPlayer;
            currentSymbol = (currentSymbol == 'W') ? 'B' : 'W';

        } while (!checkForEndOfRound());

        // We are here means, game has ended
        char opponentSymbol = (currentSymbol == 'W') ? 'B' : 'W';
        Player opponentPlayer = (currentPlayer == humanPlayer) ? computerPlayer : humanPlayer;

        calculateScore(currentSymbol, currentPlayer);
        calculateScore(opponentSymbol, opponentPlayer);
    }

    public Pair<Integer, Integer> play(char firstPlayerSymbol) {
        if (firstPlayerSymbol == ' ') {
            firstPlayerSymbol = tossHumanComputer();
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
