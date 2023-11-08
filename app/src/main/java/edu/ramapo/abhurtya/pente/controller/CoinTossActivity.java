package edu.ramapo.abhurtya.pente.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

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
                // Animation has ended, now start the next activity
                Intent startGameIntent = new Intent(CoinTossActivity.this, MainActivity.class);
                startGameIntent.putExtra("firstPlayerSymbol", (choice == 1) ? 'H' : 'C'); // Pass the choice made by the user
                startActivity(startGameIntent);
                finish(); // Finish this activity so it's not in the back stack
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
