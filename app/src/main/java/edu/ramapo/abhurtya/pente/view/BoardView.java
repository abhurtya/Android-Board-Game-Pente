package edu.ramapo.abhurtya.pente.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import edu.ramapo.abhurtya.pente.R;
import edu.ramapo.abhurtya.pente.model.Board;

public class BoardView {

    private GridLayout gridLayout;
    private Context context;
    private Board board;

    public BoardView(GridLayout gridLayout, Context context, Board board) {
        this.gridLayout = gridLayout;
        this.context = context;
        this.board = board;

        this.gridLayout.setColumnCount(20);
        this.gridLayout.setRowCount(20);
    }

    public void createBoard() {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                View view = createCell(row, col);
                gridLayout.addView(view);
            }
        }
    }

    private View createCell(int row, int col) {
        TextView view;
        if (row == 0 && col == 0) {
            // This is the top-left corner, leave it blank or add some other view if necessary
            view = new TextView(context);
        } else if (row == 0) {
            // This is the first row which will contain column labels
            TextView label = new TextView(context);
            label.setText(String.valueOf((char)('A' + col - 1)));
            view = label;
        } else if (col == 0) {
            // This is the first column which will contain row labels
            TextView label = new TextView(context);
            label.setText(String.valueOf(20 - row)); // 20 - row, because we are labeling from 19 to 1
            view = label;
        } else {
            // normal board cell
            TextView cell = new TextView(context);

            // Set cell background based on the current state
            char state = board.getCell(row - 1, col - 1);
            switch (state) {
                case 'W': // white stone
                    cell.setBackgroundResource(R.drawable.white_stone); // Replace with your white stone drawable
                    Log.d("yoyo", "did white code");
                    break;
                case 'B': // a black stone
                    cell.setBackgroundResource(R.drawable.black_stone); // Replace with your black stone drawable
                    Log.d("yoyo", "did black code");
                    break;
                default: // If the cell is empty
                    cell.setBackgroundResource(R.drawable.cell_background);
                    break;
            }

            cell.setTag(new CellTag(row - 1, col - 1));
            // Add click listeners and other cell properties
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CellTag tag = (CellTag) v.getTag();

                    if (tag != null) {
                        notifyCellClicked(tag.row, tag.col);
                    } else {
                        // Handle the case where tag is null if necessary
                        Log.d("cell click", "null tag");
                    }

                }
            });
            view = cell;
        }
        // Return the created view
        return view;
    }

    public void updateCell(int row, int col, char player) {
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
            // Update the background of the cell based on the player
            switch (player) {
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

    public void refreshBoard() {
        // Assuming your board model starts from index 0 and goes to 18 for a 19x19 board
        for (int row = 0; row < 19; row++) {
            for (int col = 0; col < 19; col++) {
                char state = board.getCell(row, col); // Retrieve the state from the model
                updateCell(row, col, state); // Update the cell on the board view
            }
        }
    }

    private BoardViewListener listener;
    public interface BoardViewListener {
        void onCellClicked(int row, int col);
    }
    public void setBoardViewListener(BoardViewListener listener) {
        this.listener = listener;
    }

    private void notifyCellClicked(int row, int col) {
        if (listener != null) {
            listener.onCellClicked(row, col);
        }
    }

    // Helper class to store coordinates in the tag
    private class CellTag {
        public final int row;
        public final int col;


        CellTag(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
