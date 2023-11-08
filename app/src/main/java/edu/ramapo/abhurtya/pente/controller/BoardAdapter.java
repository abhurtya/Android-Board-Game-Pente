//package edu.ramapo.abhurtya.pente.controller;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Gravity;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//import edu.ramapo.abhurtya.pente.R;
//import edu.ramapo.abhurtya.pente.model.Board;
//
//public class BoardAdapter extends BaseAdapter {
//
//    private Context context;
//    private Board board;
//    private LayoutInflater inflater;
//
//    public BoardAdapter(Context context, Board board) {
//        this.context = context;
//        this.board = board;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return 20 * 20; // Size of the board
//    }
//
//    @Override
//    public Object getItem(int position) {
//        int x = position / 20;
//        int y = position % 20;
//        if (x >= 0 && y >= 0) {
//            return board.getCell(x, y);
//        }
//        return null; //excluding labels
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Initialize the view as before
//        View cellView = convertView;
//        if (cellView == null) {
//            cellView = inflater.inflate(R.layout.grid_item, parent, false);
//        }
//
//        TextView cellTextView = cellView.findViewById(R.id.cell_text);
//
//        // Calculate the row and column taking the new grid size into account
//        int row = position / 20;
//        int col = position % 20;
//
//        // Check if it's a label cell
//        if (row == 0 || col == 0) {
//            cellTextView.setBackgroundColor(Color.LTGRAY); // Set your desired background color for labels
//
//            if (row == 0 && col > 0) {
//                // Top row labels (A-S, skipping I)
//                String columns = "KBCDEFGHJKLMNOPQRST";
//                cellTextView.setText(String.valueOf(columns.charAt(col - 1)));
//                cellTextView.setGravity(Gravity.CENTER);
//            } else if (col == 0 && row > 0) {
//                // First column labels (1-19)
//                cellTextView.setText(String.valueOf(20 - row));
//                cellTextView.setGravity(Gravity.TOP);
//            } else {
//                // Top left corner is empty
//                cellTextView.setText("");
//            }
//        } else {
//            // This is the board cell (1-19)
//            int boardRow = row - 1;
//            int boardCol = col - 1;
//            char state = board.getCell(boardRow, boardCol);
//
//            // Adjust your color and text based on the state
//            switch (state) {
//                case 'W': // White stone
//                    cellTextView.setBackgroundColor(Color.WHITE);
//                    cellTextView.setText("W");
//                    break;
//                case 'B': // Black stone
//                    cellTextView.setBackgroundColor(Color.BLACK);
//                    cellTextView.setTextColor(Color.WHITE);
//                    cellTextView.setText("B");
//                    break;
//                default: // Empty cell
//                    cellTextView.setBackground(null); // Or a default background resource
//                    cellTextView.setText("");
//                    break;
//            }
//            cellTextView.setGravity(Gravity.CENTER); // Center the text in the GridView cell
//        }
//
//        return cellView;
//    }
//
//}
