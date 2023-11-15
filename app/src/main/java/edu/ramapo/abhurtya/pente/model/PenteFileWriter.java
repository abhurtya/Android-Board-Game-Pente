package edu.ramapo.abhurtya.pente.model;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Writes a Pente game to a file.
 */
public class PenteFileWriter {

    /**
     * Saves a Pente game state to a txt file.
     * @param writer The writer to write the game data.
     * @param board The board object to be written to the file.
     * @param human The human player object to be written to the file.
     * @param computer The computer player object to be written to the file.
     * @param nextPlayer The next player's type.
     * @param nextPlayerSymbol The next player's symbol.
     * @return true if the game saves successfully, false otherwise.
     */
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
