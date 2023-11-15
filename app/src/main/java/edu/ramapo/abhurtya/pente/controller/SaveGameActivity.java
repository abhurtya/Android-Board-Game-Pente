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
import android.widget.Toast;
import android.provider.MediaStore;
import android.content.ContentValues;
import android.os.Build;
import android.net.Uri;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import android.os.Environment;

/**
 * Activity to handle the saving of the game.
 * This activity allows the user to enter a filename and save the game to a text file.
 */

public class SaveGameActivity extends AppCompatActivity {

    private EditText filenameEditText;
    private Button saveButton;

    /**
     * Initializes the SaveGameActivity and sets up the UI components.
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);

        filenameEditText = findViewById(R.id.filenameEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGame();
            }
        });
    }

    /**
     * Saves the game to a text file.
     * The filename is taken from the filenameEditText.
     * If the filename does not end with .txt, it is appended to the filename.
     * The game is saved to the Downloads folder.
     */
    private void saveGame() {
        String filename = filenameEditText.getText().toString();
        if (filename.isEmpty()) {
            Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        try {
            //help obtained from Github user fiftyonemoon
            //https://gist.github.com/fiftyonemoon/433b563f652039e32c07d1d629f913fb
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            }

            Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri);
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

                    Board board = (Board) getIntent().getSerializableExtra("board");
                    Player humanPlayer = (Player) getIntent().getSerializableExtra("humanPlayer");
                    Player computerPlayer = (Player) getIntent().getSerializableExtra("computerPlayer");
                    String nextPlayer = getIntent().getStringExtra("nextPlayer");
                    char nextPlayerSymbol = getIntent().getCharExtra("nextPlayerSymbol", 'W');

                    boolean success = PenteFileWriter.saveGame(writer, board, humanPlayer, computerPlayer, nextPlayer, nextPlayerSymbol);

                    if (success) {
                        Toast.makeText(this, "Game saved successfully to Downloads", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity after saving
                    } else {
                        Toast.makeText(this, "Failed to save game.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Failed to create file in Downloads", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
