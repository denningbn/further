package com.example.further;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class EncryptDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Enter Encryption Password?")
                .setPositiveButton("Encrypt", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //TODO
                        //get encryption string
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int id)
                    {
                        //TODO
                        //Cancel
                    }
                });

        return builder.create();
    }

}
