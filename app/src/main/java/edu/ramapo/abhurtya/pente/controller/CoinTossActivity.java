package edu.ramapo.abhurtya.pente.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;

import edu.ramapo.abhurtya.pente.R;

/**
 * Activity to handle the coin toss process at the beginning of the game.
 * This activity allows the user to choose heads or tails and shows the result of the coin toss.
 */
public class CoinTossActivity extends AppCompatActivity {

    private ImageView coinImage;

    /**
     * Initializes the CoinTossActivity and sets up the UI components.
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_toss);

        coinImage = findViewById(R.id.coin_image);

        Button headsButton = findViewById(R.id.button_heads);
        Button tailsButton = findViewById(R.id.button_tails);

        headsButton.setOnClickListener(v -> onCoinTossChoice(1)); // 1 for heads
        tailsButton.setOnClickListener(v -> onCoinTossChoice(2)); // 2 for tails
    }

    /**
     * Handles the coin toss choice made by the user.
     * Initiates the coin animation and determines the result of the toss.
     * @param choice The user's choice: 1 for heads, 2 for tails.
     */
    private void onCoinTossChoice(int choice) {
        animateCoin(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Get the result of the coin toss
                String choiceStr = (choice == 1) ? "heads" : "tails";
                int tossResult = (int) (Math.random() * 2) + 1;

                String resultStr = (tossResult == 1) ? "heads" : "tails";
                boolean isHumanStarting = choice == tossResult;

                // Show the result dialog
                CoinTossResultDialogFragment dialog = CoinTossResultDialogFragment.newInstance(choiceStr, resultStr);
                dialog.show(getSupportFragmentManager(), "coinTossResult");

                // Start the game after the dialog has been dismissed
                new Handler().postDelayed(() -> {

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("firstPlayerSymbol", isHumanStarting ? 'H' : 'C');
                    setResult(RESULT_OK, resultIntent);  // Set the result with the chosen symbol
                    finish();

                }, 3000);
            }
        });
    }

    /**
     * Animates the coin image with a flipping effect.
     * @param animatorListener Listener to handle the completion of the animation.
     */
    private void animateCoin(Animator.AnimatorListener animatorListener) {
        // flips the coin around the Y axis 3 times
        ObjectAnimator flip = ObjectAnimator.ofFloat(coinImage, "rotationY", 0f, 1080f);
        flip.setDuration(3000); // milliseconds
        flip.addListener(animatorListener); // add listener to handle animation end
        flip.start();
    }
}
