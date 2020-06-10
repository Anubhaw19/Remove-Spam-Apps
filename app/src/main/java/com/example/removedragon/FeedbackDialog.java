package com.example.removedragon;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class FeedbackDialog extends AppCompatDialogFragment {

    private EditText feedbackText;
    public sendButtonClicked listner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog , null);
        builder.setView(view)
                .setTitle("Give Feedback or Report an app")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String feedback = feedbackText.getText().toString();
                        listner.sendFeedback(feedback);
                    }
                });
        feedbackText = view.findViewById(R.id.edit_text_fedback);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner =(sendButtonClicked) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface sendButtonClicked{
        void sendFeedback(String message);
    }
}
