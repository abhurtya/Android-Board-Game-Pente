package edu.ramapo.abhurtya.pente.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.ramapo.abhurtya.pente.utils.Logger;

public class PenteFileWriter {

    public static boolean saveGame(String filename, Board board, Player human, Player computer, String nextPlayer, char nextPlayerSymbol) {

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

            String nextPlayerSymbolStr = nextPlayerSymbol == 'W' ? "White" : "Black";
            outFile.println("\nNext Player: " + nextPlayer + " - " + nextPlayerSymbolStr);

            return true;
        } catch (IOException e) {
            Logger.getInstance().addLog("Failed to open file for writing.");
            return false;
        }
    }
}
