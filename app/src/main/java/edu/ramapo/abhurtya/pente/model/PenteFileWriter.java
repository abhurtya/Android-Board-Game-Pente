package edu.ramapo.abhurtya.pente.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class PenteFileWriter {

    public static boolean saveGame(Board board, Player human, Player computer, String nextPlayer, String nextPlayerSymbol) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter FileName: ");
        String filename = input.nextLine();

        try (PrintWriter outFile = new PrintWriter(new FileWriter(filename))) {
            outFile.println("Board:");
            for (int i = 18; i >= 0; --i) {
                for (int j = 0; j < 19; ++j) {
                    char cell = board.getCell(i, j);
                    outFile.print(cell == '*' ? 'O' : cell);
                }
                outFile.println();
            }

            outFile.println("\nHuman:");
            outFile.println("Captured pairs: " + human.getCaptures());
            outFile.println("Score: " + human.getPoints());

            outFile.println("\nComputer:");
            outFile.println("Captured pairs: " + computer.getCaptures());
            outFile.println("Score: " + computer.getPoints());

            outFile.println("\nNext Player: " + nextPlayer + " - " + nextPlayerSymbol);

            return true;
        } catch (IOException e) {
            System.out.println("Failed to open file for writing.");
            return false;
        } finally {
            // Close the scanner
            // input.close();
        }
    }
}
