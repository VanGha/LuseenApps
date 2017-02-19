package com.luseen.vanik.luseenapp.Dialogs;

import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;


public class CustomDialog extends AlertDialog {

    AlertDialog.Builder dialogBuilder;
    String trueButton, falseButton;

    public CustomDialog(Context context, String message, final String trueButton, String falseButton) {
        super(context);

        this.trueButton = trueButton;
        this.falseButton = falseButton;

        dialogBuilder = new AlertDialog.Builder(context);

        dialogBuilder.setMessage(message);

        if (trueButton != null) {

            dialogBuilder.setPositiveButton(trueButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });

            if (falseButton != null) {

                dialogBuilder.setNegativeButton(falseButton, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

            }

        }

        dialogBuilder.create().show();

    }

    public AlertDialog.Builder getDialogBuilder() {
        return dialogBuilder;
    }

}
