package edu.ramapo.abhurtya.pente.model;

import java.io.BufferedWriter;
import java.io.IOException;

public class PenteFileWriter {

    public static boolean saveGame(BufferedWriter writer, Board board, Player human, Player computer, String nextPlayer, char nextPlayerSymbol) {
        try {
            writer.write("Board:\n");
            for (int i = 18; i >= 0; --i) {
                for (int j = 0; j < 19; ++j) {
                    char cell = board.getCell(i, j);
                    writer.write(cell == '*' ? 'O' : cell);
                }
                writer.newLine();
            }

            writer.write("\nHuman:\n");
            writer.write("Captured pairs: " + human.getCaptures() + "\n");
            writer.write("Score: " + human.getPoints() + "\n");

            writer.write("\nComputer:\n");
            writer.write("Captured pairs: " + computer.getCaptures() + "\n");
            writer.write("Score: " + computer.getPoints() + "\n");

            String nextPlayerSymbolStr = nextPlayerSymbol == 'W' ? "White" : "Black";
            writer.write("\nNext Player: " + nextPlayer + " - " + nextPlayerSymbolStr + "\n");

            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
