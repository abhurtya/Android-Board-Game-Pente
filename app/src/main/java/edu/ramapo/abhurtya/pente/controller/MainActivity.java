package edu.ramapo.abhurtya.pente.controller;

import android.os.Bundle;
import android.widget.GridLayout;
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

        currentSymbol = round.tossHumanComputer(1);

        // Get the coin toss result from the intent
        char firstPlayerSymbol = getIntent().getCharExtra("firstPlayerSymbol", 'H'); // Default to 'H'
        isHumanTurn = firstPlayerSymbol == 'H';

        Log.d("MainActivity", "First player symbol: " + firstPlayerSymbol);
        boardView = new BoardView(findViewById(R.id.gridLayout_game_board), this, round.getBoard());
        boardView.setBoardViewListener(this);
        boardView.createBoard();

        //If the computer start first, trigger computer's turn
        if (!isHumanTurn) {
            computerTurn();
        }

    }

    private void computerTurn() {
        // Here you will implement the logic for the computer's turn
        // Once the computer's turn is over, set isHumanTurn to true
        // You may need to implement an AI or some kind of automated decision-making for the computer's moves

        // After the computer has made its move, refresh the board
        boardView.refreshBoard();

        // If the game isn't over, it becomes the human's turn again
        isHumanTurn = true;
    }

    // Implement the onCellClicked method from the BoardViewListener interface
    @Override
    public void onCellClicked(int row, int col) {
        // Here the controller interacts with the model and updates the view accordingly
        char currentPlayer = 'B'; // or 'W', determine the current player logic
        boolean placed = round.getBoard().setCell(row, col, currentPlayer);
        if (placed) {
            // Tell the view to update the cell
//            boardView.updateCell(row , col, currentPlayer);
            boardView.refreshBoard();
        }
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to the coin toss
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
