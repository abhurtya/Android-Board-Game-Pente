package edu.ramapo.abhurtya.pente.controller;

import android.os.Bundle;
import android.widget.Toast ;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.app.Activity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResult;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.os.Handler;
import android.widget.TextView;
import android.os.Looper;

import edu.ramapo.abhurtya.pente.R;
import edu.ramapo.abhurtya.pente.model.Player;
import edu.ramapo.abhurtya.pente.model.Computer;
import edu.ramapo.abhurtya.pente.model.Human;
import edu.ramapo.abhurtya.pente.model.Round;
import edu.ramapo.abhurtya.pente.view.BoardView;
import edu.ramapo.abhurtya.pente.utils.Logger;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements BoardView.BoardViewListener {

    private Round round;
    private BoardView boardView;
    private Player humanPlayer;
    private Player computerPlayer;

    private TextView logTextView;
    private TextView humanCapturesTextView;
    private TextView computerCapturesTextView;
    private TextView humanTourScoreTextView;
    private TextView computerTourScoreTextView;

    private boolean isHumanTurn;
    private int humanTournamentScore =0;
    private int computerTournamentScore =0;
    private int roundNumber = 0;

    private Handler handler = new Handler();
    private Runnable updateLogsRunnable = new Runnable() {
        @Override
        public void run() {
            updateLogDisplay();
            handler.postDelayed(this, 2000); // Schedule this runnable again after 2 seconds
        }
    };

    private ActivityResultLauncher<Intent> coinTossResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result from CoinTossActivity
                        Intent data = result.getData();
                        if (data != null) {
                            char firstPlayerSymbol = data.getCharExtra("firstPlayerSymbol", 'H');
                            isHumanTurn = firstPlayerSymbol == 'H';
                            setPlayerSymbols();
                            startGame();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humanPlayer = new Human();
        computerPlayer = new Computer();

        round = new Round(humanPlayer, computerPlayer);
        boardView = new BoardView(findViewById(R.id.gridLayout_game_board), this, round.getBoard());
        boardView.setBoardViewListener(this);


        boardView.createBoard();

        humanCapturesTextView = findViewById(R.id.humanCapturesTextView);
        computerCapturesTextView = findViewById(R.id.computerCapturesTextView);
        humanTourScoreTextView = findViewById(R.id.humanTourScoreTextView);
        computerTourScoreTextView = findViewById(R.id.computerTourScoreTextView);

        Logger.getInstance().clearLogs();
        logTextView = findViewById(R.id.logTextView);

        startNewRound();

        handler.post(updateLogsRunnable);
    }

    private void startNewRound(){
        roundNumber++;
        Logger.getInstance().addLog("----------------------------------------------------");
        Logger.getInstance().addLog("Round " + roundNumber + " started.");
        Logger.getInstance().addLog("----------------------------------------------------");

        if (roundNumber > 1) {
            round.resetRound();
            boardView.refreshBoard();

        }

        updateCapturesDisplay();

        if (roundNumber == 1 || humanTournamentScore == computerTournamentScore) {
            // start the CoinTossActivity
            Intent coinTossIntent = new Intent(this, CoinTossActivity.class);
            coinTossResultLauncher.launch(coinTossIntent);

        } else {
            // Determine who starts based on scores
            isHumanTurn = humanTournamentScore > computerTournamentScore;
            String dialogMessage = isHumanTurn ? "YOU get to start b/c you have more points."
                    : "Computer starts b/c computer has more points.";
            showTemporaryDialog(dialogMessage, 3);
            setPlayerSymbols();
            startGame();
        }
    }

    private void setPlayerSymbols() {
        if (isHumanTurn) {
            humanPlayer.setSymbol('W');
            computerPlayer.setSymbol('B');
        } else {
            humanPlayer.setSymbol('B');
            computerPlayer.setSymbol('W');
        }
    }

    private void startGame() {

        if (!isHumanTurn) {
            makeComputerMove();
        }
    }

    private void makeComputerMove() {
        computerPlayer.play(round.getBoard(), computerPlayer.getSymbol());

        updateGameState(computerPlayer, computerPlayer.getSymbol());
    }

    private void updateGameState(Player player, char symbol) {
        // Update the board
        round.getBoard().setCell(player.getLocation().getKey(), player.getLocation().getValue(), symbol);

        // Check for captures or wins
        boolean captureMade = round.checkForCapture(symbol, player);
        if (captureMade) {
            showTemporaryDialog(player.getPlayerType() + " captured a pair!", 2);
            updateCapturesDisplay();
        }

        // Refresh the board view
        boardView.refreshBoard();


        if (round.checkForFiveInARow(symbol, player)) {
            showTemporaryDialog(player.getPlayerType() + " wins due to five in a row!", 2);
            finishGame();
            return;
        } else if (round.checkForFiveCaptures(player)) {
            showTemporaryDialog(player.getPlayerType() + " wins due to five captures!", 2);
            finishGame();
            return;
        }

        // Switch turns
        swapTurns();
    }

    private void updateCapturesDisplay() {
        humanCapturesTextView.setText("Human:" + String.valueOf(humanPlayer.getCaptures()) + " captures");
        computerCapturesTextView.setText("Computer:" + String.valueOf(computerPlayer.getCaptures()) + " captures");

    }

    private void swapTurns() {
        isHumanTurn = !isHumanTurn;

        // If it's the computer's turn, make a move
        if (!isHumanTurn) {
            makeComputerMove();
        }
    }

    private void finishGame() {

        round.calculateScore(humanPlayer.getSymbol(), humanPlayer);
        round.calculateScore(computerPlayer.getSymbol(), computerPlayer);

        humanTournamentScore += humanPlayer.getPoints();
        computerTournamentScore += computerPlayer.getPoints();

        updateTourScoreDisplay();


        // Show a dialog to ask if the user wants to play again
//        new AlertDialog.Builder(this)
//                .setTitle("Game Over")
//                .setMessage("Play again?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        startNewRound(); // Start a new round
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish(); // Close the activity
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();

        // Delay starting PlayAgainActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, PlayAgainActivity.class);
                startActivityForResult(intent, 1);
            }
        }, 8000); // Delay for 6 seconds

    }

    private void updateTourScoreDisplay() {
        humanTourScoreTextView.setText("Human: " + String.valueOf(humanTournamentScore) + " points");
        computerTourScoreTextView.setText("Computer: " + String.valueOf(computerTournamentScore) + " points");
    }



    // Implement the onCellClicked method from the BoardViewListener interface
    @Override
    public void onCellClicked(int row, int col) {

        if (isHumanTurn) {
            if (round.getBoard().isValidMove(row, col, humanPlayer.getSymbol())) {
                humanPlayer.setLocation(row, col);
                Logger.getInstance().addLog("You played at " + (char) (col + 'A') + (19 - row ) + ".");
                updateGameState(humanPlayer, humanPlayer.getSymbol());
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

    private void showTemporaryDialog(String message, int seconds) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .create();

        dialog.show();

        // Handler to dismiss the dialog after the specified number of seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, seconds * 1000); //  milliseconds
    }

    private void updateLogDisplay() {
        String logs = Logger.getInstance().showLogs();
        logTextView.setText(logs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateLogsRunnable); // Stop the runnable when the activity is destroyed
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean playAgain = data.getBooleanExtra("playAgain", false);
            if (playAgain) {
                startNewRound();
            } else {
                finish();
            }
        }
    }





}