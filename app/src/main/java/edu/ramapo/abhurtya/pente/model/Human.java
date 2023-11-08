package edu.ramapo.abhurtya.pente.model;

import java.util.Scanner;

public class Human extends Player {

    private Pair<Integer, Integer> location;
    private Scanner scanner;

    public Human() {
        this.location = new Pair<>(-1, -1);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void play(Board board, char symbol) {
        String input;
        int x, y;
        char column;
        int row;

        while (true) {
            System.out.print("Enter your move or type 'help': ");
            input = scanner.nextLine();

            // Providing help to the user
            if ("help".equalsIgnoreCase(input)) {
                Pair<Integer, Integer> suggestedMove = strategy(board, symbol);
                column = (char)('A' + suggestedMove.getValue());
                row = suggestedMove.getKey() + 1;
                System.out.println("Suggested move: " + column + row);
                continue; // Continue to allow user to decide after viewing suggestion
            }

            if (input.length() < 2) {
                System.out.println("Invalid input. Enter coordinates in the correct format (e.g: E5)");
                continue;
            }

            column = Character.toUpperCase(input.charAt(0));
            try {
                row = Integer.parseInt(input.substring(1));
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter coordinates in the correct format (e.g: E5).");
                continue;
            }

            if (column < 'A' || column > 'S' || row < 1 || row > 19) {
                System.out.println("Invalid input. Enter coordinates within the board range (A1-S19).");
                continue;
            }

            // Convert to internal representation
            y = column - 'A';
            x = row - 1;

            // Check if the move is valid
            if (!board.isValidMove(x, y, symbol)) {
                System.out.println("Oops! That spot is taken or move is invalid. Choose another spot.");
                continue;
            }

            break;
        }

        // Now we can set the location
        setLocation(x, y);
        System.out.println("You chose position: " + column + row);
    }

    public void setLocation(int x, int y) {
        this.location = new Pair<>(x, y);
    }

    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    @Override
    public String getPlayerType() {
        return "Human";
    }
}
