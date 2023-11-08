package edu.ramapo.abhurtya.pente.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PenteFileReader {

    public static boolean loadGame(Board board, Player human, Player computer, String[] nextPlayer, char[] nextPlayerSymbol) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter FileName: ");
        String filename = input.nextLine();

        try {
            Scanner inFile = new Scanner(new File(filename));
            // Skip the "Board:" line
            inFile.nextLine();

            for (int i = 18; i >= 0; --i) {
                String line = inFile.nextLine();
                for (int j = 0; j < 19; ++j) {
                    if (line.charAt(j) == 'O') {
                        board.setCell(i, j, '*');
                    } else {
                        board.setCell(i, j, line.charAt(j));
                    }
                }
            }

            // Skip to "Human:" line
            inFile.nextLine(); // Skips the empty line before "Human:"
            inFile.nextLine(); // Skips the "Human:" line
            int humanCaptures = Integer.parseInt(inFile.nextLine().split(":")[1].trim());
            int humanScore = Integer.parseInt(inFile.nextLine().split(":")[1].trim());
            human.setCaptures(humanCaptures);
            human.setPoints(humanScore);

            // Skip to "Computer:" line
            inFile.nextLine(); // Skips the empty line before "Computer:"
            inFile.nextLine(); // Skips the "Computer:" line
            int computerCaptures = Integer.parseInt(inFile.nextLine().split(":")[1].trim());
            int computerScore = Integer.parseInt(inFile.nextLine().split(":")[1].trim());
            computer.setCaptures(computerCaptures);
            computer.setPoints(computerScore);


            // Skip to next player info
            inFile.nextLine(); // Skips the empty line before "Next Player:"
            String nextPlayerInfo = inFile.nextLine(); // Reads "Next Player: Human - White"
            String[] nextPlayerParts = nextPlayerInfo.split("-");
            nextPlayer[0] = nextPlayerParts[0].split(":")[1].trim(); // Store next player
            nextPlayerSymbol[0] = nextPlayerParts[1].trim().equals("White") ? 'W' : 'B';


            // inFile.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open file for reading.");
            return false;
        } finally {

            // input.close();
        }
    }
}
