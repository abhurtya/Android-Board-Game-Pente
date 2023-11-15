package edu.ramapo.abhurtya.pente.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ramapo.abhurtya.pente.utils.Logger;

/**
 * Reads a Pente game file and loads the game state into the board and players.
 */

public class PenteFileReader  {

    /**
     * Loads a Pente game from a file.
     * @param scanner The scanner to read the game data.
     * @param board The board object to be updated with the file data.
     * @param human The human player object to be updated with file data.
     * @param computer The computer player object to be updated with file data.
     * @param nextPlayer An array to store the next player's type.
     * @return true if the game loads successfully, false otherwise.
     */
    public static boolean loadGameFromScanner(Scanner scanner, Board board, Player human, Player computer, String[] nextPlayer) {

        try {
            // Skip the "Board:" line
            scanner.nextLine();

            for (int i = 0; i < 19; ++i) {
                String line = scanner.nextLine(); // Read the next line from the file
                for (int j = 0; j < 19; ++j) {
                    if (line.charAt(j) == 'O') {
                        board.setCell(i, j, '*');
                    } else {
                        board.setCell(i, j, line.charAt(j));
                    }
                }
                System.out.println(); // Move to the next line after processing each row
            }

            // Skip to "Human:" line
            scanner.nextLine(); // Skips the empty line before "Human:"
            scanner.nextLine(); // Skips the "Human:" line
            int humanCaptures = Integer.parseInt(scanner.nextLine().split(":")[1].trim());
            int humanScore = Integer.parseInt(scanner.nextLine().split(":")[1].trim());
            human.setCaptures(humanCaptures);
            human.setPoints(humanScore);

            // Skip to "Computer:" line
            scanner.nextLine(); // Skips the empty line before "Computer:"
            scanner.nextLine(); // Skips the "Computer:" line
            int computerCaptures = Integer.parseInt(scanner.nextLine().split(":")[1].trim());
            int computerScore = Integer.parseInt(scanner.nextLine().split(":")[1].trim());
            computer.setCaptures(computerCaptures);
            computer.setPoints(computerScore);


            // Skip to next player info
            scanner.nextLine(); // Skips the empty line before "Next Player:"
            String nextPlayerInfo = scanner.nextLine(); // Reads "Next Player: Human - White"
            String[] nextPlayerParts = nextPlayerInfo.split("-");
            nextPlayer[0] = nextPlayerParts[0].split(":")[1].trim(); // Store next player
            char nextPlayerSymbol = nextPlayerParts[1].trim().equals("White") ? 'W' : 'B';

            if (nextPlayer[0].equals("Human")){
                human.setSymbol(nextPlayerSymbol);
                computer.setSymbol(nextPlayerSymbol == 'W' ? 'B' : 'W');
            }
            else{
                computer.setSymbol(nextPlayerSymbol);
                human.setSymbol(nextPlayerSymbol == 'W' ? 'B' : 'W');
            }

            return true;
        } catch (Exception e) {
            Logger.getInstance().addLog("Failed to open file for reading.");
            return false;
        }
    }
}
