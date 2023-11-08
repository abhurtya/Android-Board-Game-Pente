package edu.ramapo.abhurtya.pente.controller;

import android.os.Bundle;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;

import edu.ramapo.abhurtya.pente.R;
import edu.ramapo.abhurtya.pente.model.Board;
import edu.ramapo.abhurtya.pente.utils.Logger;
import edu.ramapo.abhurtya.pente.view.BoardView;

public class MainActivity extends AppCompatActivity implements BoardView.BoardViewListener {


    private Board board;
    private Logger logger;
    private BoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Logger and Board
        logger = new Logger();
        board = new Board(logger);

        boardView = new BoardView(findViewById(R.id.gridLayout_game_board), this, board);
        boardView.setBoardViewListener(this);
        boardView.createBoard();

    }

    // Implement the onCellClicked method from the BoardViewListener interface
    @Override
    public void onCellClicked(int row, int col) {
        // Here the controller interacts with the model and updates the view accordingly
        char currentPlayer = 'B'; // or 'W', determine the current player logic
        boolean placed = board.setCell(row, col, currentPlayer);
        if (placed) {
            // Tell the view to update the cell
//            boardView.updateCell(row , col, currentPlayer);
            boardView.refreshBoard();
        }
    }
}
