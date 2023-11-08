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

public class CoinTossActivity extends AppCompatActivity {

    private ImageView coinImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_toss);

        coinImage = findViewById(R.id.coin_image); // Make sure you have a coin ImageView in your layout

        Button headsButton = findViewById(R.id.button_heads);
        Button tailsButton = findViewById(R.id.button_tails);

        headsButton.setOnClickListener(v -> onCoinTossChoice(1)); // 1 for heads
        tailsButton.setOnClickListener(v -> onCoinTossChoice(2)); // 2 for tails
    }

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
                    Intent startGameIntent = new Intent(CoinTossActivity.this, MainActivity.class);
                    startGameIntent.putExtra("firstPlayerSymbol", isHumanStarting ? 'H' : 'C');
                    startActivity(startGameIntent);
                    finish();
                }, 3000); // Adjust this value to control how long the dialog is shown
            }
        });
    }


    private void animateCoin(Animator.AnimatorListener animatorListener) {
        // This example flips the coin around the Y axis 3 times
        ObjectAnimator flip = ObjectAnimator.ofFloat(coinImage, "rotationY", 0f, 1080f);
        flip.setDuration(3000); // animation duration in milliseconds
        flip.addListener(animatorListener); // add listener to handle animation end
        flip.start();
    }
}
