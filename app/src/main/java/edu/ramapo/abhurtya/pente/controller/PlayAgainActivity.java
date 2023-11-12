package edu.ramapo.abhurtya.pente.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.ramapo.abhurtya.pente.R;


public class PlayAgainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to your custom layout
        setContentView(R.layout.activity_play_again);

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

