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
import android.widget.EditText;
import android.text.InputType;
import android.view.View;
import java.io.File;
import android.database.Cursor;
import android.provider.OpenableColumns;
import android.os.Handler;
import android.widget.TextView;
import android.os.Looper;
import android.net.Uri;
import java.io.InputStream;
import java.io.IOException;
import java.util.Scanner;
import android.widget.Button;
import java.io.FileOutputStream;


import edu.ramapo.abhurtya.pente.R;
import edu.ramapo.abhurtya.pente.model.Pair;
import edu.ramapo.abhurtya.pente.model.Player;
import edu.ramapo.abhurtya.pente.model.Computer;
import edu.ramapo.abhurtya.pente.model.Human;
import edu.ramapo.abhurtya.pente.model.Round;
import edu.ramapo.abhurtya.pente.view.BoardView;
import edu.ramapo.abhurtya.pente.model.PenteFileReader;
import edu.ramapo.abhurtya.pente.model.PenteFileWriter;
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
    private TextView colorCodeTextView;

    private Button saveButton;
    private Button quitButton;
    private Button helpButton;

    private boolean isHumanTurn;
    private int humanTournamentScore =0;
    private int computerTournamentScore =0;
    private int roundNumber = 0;

    private Handler handler = new Handler();

    //runnable help obtained from oracle docs
    //https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/lang/Runnable.html
    private Runnable updateLogsRunnable = new Runnable() {
        @Override
        public void run() {
            updateLogDisplay();
            handler.postDelayed(this, 2000); // Schedule this runnable again after 2 seconds
        }
    };

    //startActivityForResult() has been deprecated so use new method instead of that.
    //https://stackoverflow.com/questions/61455381/how-to-replace-startactivityforresult-with-activity-result-apis
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

    private ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri uri = data.getData();
                            // Handle the URI, load the game from the file
                            loadGameFromFile(uri);
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
        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> onSaveButtonClicked());
        quitButton = findViewById(R.id.button_quit);
        quitButton.setOnClickListener(v -> finish());
        helpButton = findViewById(R.id.button_help);
        helpButton.setOnClickListener(v -> onButtonHelpClicked());

        Logger.getInstance().clearLogs();
        logTextView = findViewById(R.id.logTextView);

        boolean isLoadGame = getIntent().getBooleanExtra("isLoadGame", false);
        if (isLoadGame) {
            launchFilePicker();
        } else {
            startNewRound();
        }

        handler.post(updateLogsRunnable);
    }

    private void launchFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //.txt extension only
        intent.setType("text/plain");
        filePickerLauncher.launch(intent);
    }

    private void loadGameFromFile(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             Scanner scanner = new Scanner(inputStream)) {

            String[] nextPlayer = new String[1];
            if (PenteFileReader.loadGameFromScanner(scanner, round.getBoard(), humanPlayer, computerPlayer, nextPlayer)) {
                isHumanTurn = nextPlayer[0].equals("Human");
                char color = nextPlayer[0].equals("Human") ? humanPlayer.getSymbol() : computerPlayer.getSymbol();
                String dialogMsg = "Game loaded from file.\n It is " + nextPlayer[0] + "'s turn playing color "+ color +".";;

                showTemporaryDialog(dialogMsg, 1);
                boardView.refreshBoard();
                roundNumber++;
                //log name of file in Logger
                Logger.getInstance().addLog("Game loaded from file: " + getFileName(uri));
                logRoundNumber(roundNumber);
                setColorCode();
                startGame();
            } else {
                showTemporaryDialog("Failed to load game from file.", 2);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error loading file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //cursor use help obtained from:
//    https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
    private String getFileName(Uri uri) {
        String displayName = "";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }

        return displayName;
    }



    private void startNewRound(){
        roundNumber++;
        logRoundNumber(roundNumber);

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
            Logger.getInstance().addLog(dialogMessage);
            setPlayerSymbols();
            startGame();
        }
    }

    private void logRoundNumber(int roundNumber) {
        Logger.getInstance().addLog("----------------------------------------------------");
        Logger.getInstance().addLog("Round " + roundNumber + " started.");
        Logger.getInstance().addLog("----------------------------------------------------");
    }

    private void setPlayerSymbols() {
        if (isHumanTurn) {
            humanPlayer.setSymbol('W');
            computerPlayer.setSymbol('B');
        } else {
            humanPlayer.setSymbol('B');
            computerPlayer.setSymbol('W');
        }

        setColorCode();
    }

    private void setColorCode(){
        colorCodeTextView = findViewById(R.id.colorCodeTextView);
        char humanColor = humanPlayer.getSymbol() == 'W' ? '⚪' : '⚫';
        char computerColor = computerPlayer.getSymbol() == 'W' ? '⚪' : '⚫';

        colorCodeTextView.setText("   Human: " + humanColor + ", Computer: " + computerColor + "   ");
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
            String opponentPlayer = player.getPlayerType().equals("Human") ? "Computer" : "Human";
            showTemporaryDialog(player.getPlayerType() + " captured " +opponentPlayer+  "'s pair!", 2);
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

    private ActivityResultLauncher<Intent> playAgainLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            boolean playAgain = data.getBooleanExtra("playAgain", false);
                            if (playAgain) {
                                startNewRound();
                            } else {
                                Intent intent = new Intent(MainActivity.this, ShowTournamentSummaryActivity.class);
                                intent.putExtra("humanPoints", humanTournamentScore);
                                intent.putExtra("computerPoints", computerTournamentScore);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            });

    private void finishGame() {

        round.calculateScore(humanPlayer.getSymbol(), humanPlayer);
        round.calculateScore(computerPlayer.getSymbol(), computerPlayer);

        humanTournamentScore += humanPlayer.getPoints();
        computerTournamentScore += computerPlayer.getPoints();

        updateTourScoreDisplay();

        // Delay starting PlayAgainActivity
        //help obtained from
        //https://stackoverflow.com/questions/3072173/how-to-call-a-method-after-a-delay-in-android
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, PlayAgainActivity.class);
            intent.putExtra("humanPoints", humanPlayer.getPoints());
            intent.putExtra("computerPoints", computerPlayer.getPoints());

                playAgainLauncher.launch(intent);
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
                String invalidReason = "Oops "+ (char) (col + 'A') + (19 - row )+ ": " + round.getBoard().whyInvalid(row, col, humanPlayer.getSymbol() );

                showTemporaryDialog(invalidReason, 2);
                Logger.getInstance().addLog(invalidReason);
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

//    https://stackoverflow.com/questions/768969/passing-a-bundle-on-startactivity
    public void onSaveButtonClicked() {
        Intent intent = new Intent(MainActivity.this, SaveGameActivity.class);
        intent.putExtra("board", round.getBoard());
        intent.putExtra("humanPlayer", humanPlayer);
        intent.putExtra("computerPlayer", computerPlayer);
        intent.putExtra("nextPlayer", isHumanTurn ? "Human" : "Computer");
        intent.putExtra("nextPlayerSymbol", isHumanTurn ? humanPlayer.getSymbol() : computerPlayer.getSymbol());
        startActivity(intent);
    }

    public void onButtonHelpClicked() {
        //if button help clicked get computer's strategy move and display it in temporary dialog

        Pair<Integer, Integer> suggestedMove= humanPlayer.strategy(round.getBoard(), humanPlayer.getSymbol(), false);
        if (suggestedMove != null) {
            boardView.highlightCell(suggestedMove.getKey(), suggestedMove.getValue(), 2000);
            showTemporaryDialog("Suggested move: " + (char) (suggestedMove.getValue() + 'A') + (19 - suggestedMove.getKey()), 2);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateLogsRunnable); // Stop the runnable when the activity is destroyed
    }

}