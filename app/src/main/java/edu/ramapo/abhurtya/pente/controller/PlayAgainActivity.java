package edu.ramapo.abhurtya.pente.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import edu.ramapo.abhurtya.pente.R;

/**
 * Activity to handle the coin toss process at the beginning of the game.
 * This activity allows the user to choose heads or tails and shows the result of the coin toss.
 */

public class PlayAgainActivity extends AppCompatActivity {

    /**
     * Initializes the CoinTossActivity and sets up the UI components.
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again);

        // Retrieve points from the intent that started this activity
        int humanPoints = getIntent().getIntExtra("humanPoints", 0);
        int computerPoints = getIntent().getIntExtra("computerPoints", 0);

        TextView humanPointsTextView = findViewById(R.id.human_points_text_view);
        TextView computerPointsTextView = findViewById(R.id.computer_points_text_view);

        humanPointsTextView.setText("Human Points: " + humanPoints);
        computerPointsTextView.setText("Computer Points: " + computerPoints);

        Button yesButton = findViewById(R.id.yes_button);
        Button noButton = findViewById(R.id.no_button);

        yesButton.setOnClickListener(v -> {
            // Yes button
            Intent intent = new Intent();
            intent.putExtra("playAgain", true);
            setResult(RESULT_OK, intent);
            finish();
        });

        noButton.setOnClickListener(v -> {
            // for No button
            Intent intent = new Intent();
            intent.putExtra("playAgain", false);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}

