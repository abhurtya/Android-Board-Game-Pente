package edu.ramapo.abhurtya.pente.controller;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import edu.ramapo.abhurtya.pente.R;

public class ShowTournamentSummaryActivity extends AppCompatActivity {

    private TextView humanTournamentPointsTextView;
    private TextView computerTournamentPointsTextView;

    private TextView wonTournamentTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_summary);

        humanTournamentPointsTextView = findViewById(R.id.human_tournament_points_text_view);
        computerTournamentPointsTextView = findViewById(R.id.computer_tournament_points_text_view);
        wonTournamentTextView = findViewById(R.id.won_tournament_textview);

        // Retrieve the tournament points from the intent
        int humanPoints = getIntent().getIntExtra("humanPoints", 0);
        int computerPoints = getIntent().getIntExtra("computerPoints", 0);

        // Display the points
        humanTournamentPointsTextView.setText("Human Points: " + humanPoints);
        computerTournamentPointsTextView.setText("Computer Points: " + computerPoints);
        if (humanPoints > computerPoints) {
            wonTournamentTextView.setText("You won the tournament!");
        } else if (humanPoints < computerPoints) {
            wonTournamentTextView.setText("You lost the tournament!");
        } else {
            wonTournamentTextView.setText("The tournament was a tie!");
        }

        Button mainMenuButton = findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(v -> {
            finish();
        });
    }
}
