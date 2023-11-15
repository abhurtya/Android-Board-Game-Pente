package edu.ramapo.abhurtya.pente.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import edu.ramapo.abhurtya.pente.R;

/**
 * Activity to handle the welcome screen.
 * This activity allows the user to start a new game or load a saved game.
 */
public class WelcomeActivity extends AppCompatActivity {

    /**
     * Initializes the WelcomeActivity and sets up the UI components.
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button startNewGameButton = findViewById(R.id.button_start_new_game);
        startNewGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra("isLoadGame", false); // Indicate not to load a game
            startActivity(intent);
        });

        Button loadGameButton = findViewById(R.id.button_load_game);
        loadGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra("isLoadGame", true); // load game
            startActivity(intent);
        });
    }
}
