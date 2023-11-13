package edu.ramapo.abhurtya.pente.controller;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import edu.ramapo.abhurtya.pente.R;
import edu.ramapo.abhurtya.pente.model.PenteFileWriter;
import edu.ramapo.abhurtya.pente.model.Board;
import edu.ramapo.abhurtya.pente.model.Player;


import android.os.Environment;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class SaveGameActivity extends AppCompatActivity {

    private EditText filenameEditText;
    private Button saveButton;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);

        filenameEditText = findViewById(R.id.filenameEditText);
        saveButton = findViewById(R.id.saveButton);

        requestStoragePermission();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExternalStorageWritable()) {
//                    requestStoragePermission();
                } else {
                    Toast.makeText(SaveGameActivity.this, "External storage not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL);
        } else {
            saveGame();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveGame();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveGame() {
        String filename = filenameEditText.getText().toString();
        if (filename.isEmpty()) {
            Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory(), filename);

        // retrieving data:
         Board board = (Board) getIntent().getSerializableExtra("board");
         Player humanPlayer = (Player) getIntent().getSerializableExtra("humanPlayer");
         Player computerPlayer = (Player) getIntent().getSerializableExtra("computerPlayer");
         String nextPlayer = getIntent().getStringExtra("nextPlayer");
         char nextPlayerSymbol = getIntent().getCharExtra("nextPlayerSymbol", 'W');


         boolean success = PenteFileWriter.saveGame(file.getAbsolutePath(), board, humanPlayer, computerPlayer, nextPlayer, nextPlayerSymbol);

         if (success) {
             Toast.makeText(this, "Game saved successfully!", Toast.LENGTH_SHORT).show();
             finish(); // Close activity after saving
         } else {
             Toast.makeText(this, "Failed to save game.", Toast.LENGTH_SHORT).show();
         }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
