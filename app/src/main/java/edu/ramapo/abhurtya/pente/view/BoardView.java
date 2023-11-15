package edu.ramapo.abhurtya.pente.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import edu.ramapo.abhurtya.pente.R;
import edu.ramapo.abhurtya.pente.model.Board;
import android.graphics.drawable.Drawable;
import android.os.Handler;

/**
 * Handles the visual representation of the Pente game board in the user interface.
 * This class is responsible for creating and updating the board view and handling user interactions with the board.
 */

public class BoardView {

    private GridLayout gridLayout;
    private Context context;
    private Board board;

    /**
     * Constructs a new BoardView with the given parameters.
     * @param gridLayout The GridLayout that will represent the game board.
     * @param context The context of the application, used for creating views
     * @param board The game board model.
     */

    public BoardView(GridLayout gridLayout, Context context, Board board) {
        this.gridLayout = gridLayout;
        this.context = context;
        this.board = board;

        this.gridLayout.setColumnCount(20);
        this.gridLayout.setRowCount(20);
    }



    // Listener to handle board interaction events
    private BoardViewListener listener;

    /**
     * The listener interface provides a way for BoardView to communicate with MainActivity
     * without needing to know the details of the latter(without direct couplin). When an event i.e. a cell click happens,
     * BoardView simply calls the listener's method (i.e. onCellClicked),
     *  and MainActivity takes the necessary action
     */
    public interface BoardViewListener {
        void onCellClicked(int row, int col);
    }
    public void setBoardViewListener(BoardViewListener listener) {
        this.listener = listener;
    }

    private void notifyCellClicked(int row, int col) {
        if (listener != null) {
            listener.onCellClicked(row, col); // Notify the listener (MainActivity) of the cell click
        }
    }

    /**
     * Creates the initial visual representation of the game board.This includes setting up individual cells and their initial states.
     */
    public void createBoard() {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                View view = createCell(row, col);
                gridLayout.addView(view);
            }
        }
    }

    /**
     * Creates a single cell in the game board.
     * @param row The row index of the cell to be created.
     * @param col The column of the cell to be created.
     * @return The created view representing the cell
     */
    private View createCell(int row, int col) {
        TextView view;
        if (row == 0 && col == 0) {
            // This is the top-left corner, leave it blank
            view = new TextView(context);
        } else if (row == 0) {
            // first row: column labels
            TextView label = new TextView(context);
            label.setText(String.valueOf((char)('A' + col - 1)));
            view = label;
        } else if (col == 0) {
            // first column :row labels
            TextView label = new TextView(context);
            label.setText(String.valueOf(20 - row)); // 20 - row, because we are labeling from 19 to 1
            view = label;
        } else {
            // normal board cell
            TextView cell = new TextView(context);

            // Set cell background based on the current state
            char state = board.getCell(row - 1, col - 1);
            switch (state) {
                case 'W': // white
                    cell.setBackgroundResource(R.drawable.white_stone);
                    break;
                case 'B': //  black
                    cell.setBackgroundResource(R.drawable.black_stone);
                    break;
                default: // If the cell is empty
                    cell.setBackgroundResource(R.drawable.cell_background);
                    break;
            }

            cell.setTag(new CellTag(row - 1, col - 1));
            // Add click listeners to the cell
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CellTag tag = (CellTag) v.getTag();

                    if (tag != null) {
                        //Notify listener(MainActivity) of the cell click
                        notifyCellClicked(tag.row, tag.col);
                    } else {
                        // Handle the case where tag is null if necessary
                        Log.d("cell click", "null tag");
                    }

                }
            });
            view = cell;
        }
        return view;
    }

    /**
     * Updates the visual representation of a single cell on the game board.
     * @param row The row index of the cell to be updated.
     * @param col The column index of the cell to be updated.
     * @param state The cell state 'W' for white, 'B' for black, '*' for empty.
     */
    public void updateCell(int row, int col, char state) {
        // Since the board is actually 19x19 and the labels are on the first row and column,
        // we need to add 1 to both row and col to get the correct position in the GridLayout
        int actualRow = row + 1;
        int actualCol = col + 1;

        // Calculate the index considering the offset from labels
        int index = (actualRow * gridLayout.getColumnCount()) + actualCol;

        // Get the child TextView from the GridLayout at the calculated index
        View view = gridLayout.getChildAt(index);

        // Check if the view is an instance of TextView before casting
        if (view instanceof TextView) {
            TextView cellView = (TextView) view;
            // Update the background of the cell based on the color
            switch (state) {
                case 'W':
                    cellView.setBackgroundResource(R.drawable.white_stone); // Drawable resource for white stone
                    break;
                case 'B':
                    cellView.setBackgroundResource(R.drawable.black_stone); // Drawable resource for black stone
                    break;
                default:
                    cellView.setBackgroundResource(R.drawable.cell_background); // Drawable for an empty cell
                    break;
            }
        }
    }

    /**
     * Refreshes the entire board view based on the current state of the game board model.
     */
    public void refreshBoard() {

        for (int row = 0; row < 19; row++) {
            for (int col = 0; col < 19; col++) {
                char state = board.getCell(row, col); // Retrieve the state from the model
                updateCell(row, col, state); // Update the cell on the board view
            }
        }
    }

    /**
     * Highlights the given cell on the board view.
     * @param row The row index of the cell to be highlighted.
     * @param col The column index of the cell to be highlighted.
     * @param duration The duration of the highlight in milliseconds.
     */

    public void highlightCell(int row, int col, int duration) {
        int actualRow = row + 1;
        int actualCol = col + 1;
        int index = (actualRow * gridLayout.getColumnCount()) + actualCol;
        View view = gridLayout.getChildAt(index);

        if (view instanceof TextView) {
            TextView cellView = (TextView) view;
            cellView.setBackgroundResource(R.drawable.highlighted_cell);
        }
    }

    /**
     * Helper class to store the row and column information as a tag in the cell view.
     * This allows for easy identification of which cell was clicked.
     */
    private class CellTag {
        public final int row;
        public final int col;


        CellTag(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
