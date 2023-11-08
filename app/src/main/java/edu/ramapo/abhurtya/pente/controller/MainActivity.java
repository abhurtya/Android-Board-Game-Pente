package edu.ramapo.abhurtya.pente.controller;

import android.os.Bundle;
import android.widget.Toast ;
import androidx.appcompat.app.AppCompatActivity;

import edu.ramapo.abhurtya.pente.R;
import edu.ramapo.abhurtya.pente.model.Player;
import edu.ramapo.abhurtya.pente.model.Computer;
import edu.ramapo.abhurtya.pente.model.Human;
import edu.ramapo.abhurtya.pente.model.Round;
import edu.ramapo.abhurtya.pente.view.BoardView;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements BoardView.BoardViewListener {

    private Round round;
    private BoardView boardView;
    private Player humanPlayer;
    private Player computerPlayer;

    private char currentSymbol;
    private boolean isHumanTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humanPlayer = new Human();
        computerPlayer = new Computer();

        round = new Round(humanPlayer, computerPlayer);

        currentSymbol = 'W';

        // Get the coin toss result from the intent
        char firstPlayerSymbol = getIntent().getCharExtra("firstPlayerSymbol", 'H'); // Default to 'H'
        isHumanTurn = firstPlayerSymbol == 'H';

        boardView = new BoardView(findViewById(R.id.gridLayout_game_board), this, round.getBoard());
        boardView.setBoardViewListener(this);
        boardView.createBoard();

        startGame();

    }

    private void startGame() {
        if (!isHumanTurn) {
            makeComputerMove();
        }
    }

    private void makeComputerMove() {
        computerPlayer.play(round.getBoard(), currentSymbol);
        updateGameState(computerPlayer);
    }

    private void updateGameState(Player player) {
        // Update the board
        round.getBoard().setCell(player.getLocation().getKey(), player.getLocation().getValue(), currentSymbol);

        // Check for captures or wins
        boolean captureMade = round.checkForCapture(currentSymbol, player);
        if (captureMade) {
            round.displayCaptures();
        }
        boolean won = round.checkForFiveInARow(currentSymbol, player) || round.checkForFiveCaptures(player);
        if (won) {
            Toast.makeText(this, player.getPlayerType() + " wins!", Toast.LENGTH_LONG).show();
            finishGame();
            return;
        }

        // Refresh the board view
        boardView.refreshBoard();

        // Switch turns
        swapTurns();
    }

    private void swapTurns() {
        currentSymbol = (currentSymbol == 'W') ? 'B' : 'W';
        isHumanTurn = !isHumanTurn;

        // If it's the computer's turn, make a move
        if (!isHumanTurn) {
            makeComputerMove();
        }
    }

    private void finishGame() {
        // End the round and maybe prompt for a new game or exit
        // Here, we just show a toast and exit the activity for simplicity
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
        finish(); // Ends the current activity
    }


    // Implement the onCellClicked method from the BoardViewListener interface
    @Override
    public void onCellClicked(int row, int col) {

        if (isHumanTurn) {
            if (round.getBoard().isValidMove(row, col, currentSymbol)) {
                humanPlayer.setLocation(row, col);
                updateGameState(humanPlayer);
            } else {
                Toast.makeText(this, "Invalid move", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to the coin toss
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
