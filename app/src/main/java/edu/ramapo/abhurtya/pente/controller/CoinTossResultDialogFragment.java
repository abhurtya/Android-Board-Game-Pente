package edu.ramapo.abhurtya.pente.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.os.Handler;
import edu.ramapo.abhurtya.pente.R;

/**
 * A DialogFragment to show the result of the coin toss in the game.
 * This dialog displays the player's choice, the actual result of the toss, and who will start the game.
 */

public class CoinTossResultDialogFragment extends DialogFragment {

    private static final String ARG_CHOICE = "choice";
    private static final String ARG_RESULT = "result";

    /**
     * Creates a new instance of CoinTossResultDialogFragment with the provided choice and result.
     * @param choice The player's choice (heads or tails).
     * @param result The actual result of the coin toss.
     * @return A new instance of CoinTossResultDialogFragment.
     */
    public static CoinTossResultDialogFragment newInstance(String choice, String result) {
        CoinTossResultDialogFragment fragment = new CoinTossResultDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHOICE, choice);
        args.putString(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Constructs the dialog with the coin toss result.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     * @return The created dialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Dialog dialog = new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                // Prevent back button from closing the dialog
            }
        };
        dialog.setContentView(R.layout.dialog_coin_toss_result);
        dialog.setCancelable(false);

        String choice = getArguments().getString(ARG_CHOICE);
        String result = getArguments().getString(ARG_RESULT);

        TextView choiceView = dialog.findViewById(R.id.text_choice);
        TextView resultView = dialog.findViewById(R.id.text_result);
        TextView firstPlayerView = dialog.findViewById(R.id.text_first_player);

        choiceView.setText(getString(R.string.you_chose, choice));
        resultView.setText(getString(R.string.coin_result_was, result));
        firstPlayerView.setText(result.equals(choice) ? R.string.you_will_start_first : R.string.computer_will_start_first);

        // Dismiss the dialog after 3 seconds
        new Handler().postDelayed(() -> dismiss(), 3000);

        return dialog;
    }
}

