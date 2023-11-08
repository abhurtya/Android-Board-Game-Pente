package edu.ramapo.abhurtya.pente.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.os.Handler;
import edu.ramapo.abhurtya.pente.R;


public class CoinTossResultDialogFragment extends DialogFragment {

    private static final String ARG_CHOICE = "choice";
    private static final String ARG_RESULT = "result";

    public static CoinTossResultDialogFragment newInstance(String choice, String result) {
        CoinTossResultDialogFragment fragment = new CoinTossResultDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHOICE, choice);
        args.putString(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Dialog dialog = new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                // Prevent back button from closing the dialog to mimic page behavior
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

        // You might want to set a timer to automatically dismiss the dialog after a few seconds
        new Handler().postDelayed(() -> dismiss(), 3000);

        return dialog;
    }
}

