package com.app.devchat.chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class AnonymousLoginConfirmDialog extends DialogFragment {

    public interface AnonymousLoginConfirmDialogListener{
        void onDialogConfirm(int confirmation);
    }

    public AnonymousLoginConfirmDialog() {
    }

    public static AnonymousLoginConfirmDialog newInstance(){
        return new AnonymousLoginConfirmDialog();
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("DevChat Login");
        builder.setMessage("Login anonymously?");

        builder.setPositiveButton("OK", (dialog, which) -> {
            ((AnonymousLoginConfirmDialogListener) getActivity()).onDialogConfirm(which);
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> ((AnonymousLoginConfirmDialogListener) getActivity()).onDialogConfirm(which));

        builder.setNeutralButton("QUIT APP", (dialog, which) -> {
            ((AnonymousLoginConfirmDialogListener) getActivity()).onDialogConfirm(which);
        });

        return builder.create();
    }
}
