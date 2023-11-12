package edu.ramapo.abhurtya.pente.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import edu.ramapo.abhurtya.pente.R;

public class WelcomeActivity extends AppCompatActivity {

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
