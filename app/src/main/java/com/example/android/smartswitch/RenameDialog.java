package com.example.android.smartswitch;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RenameDialog extends AppCompatDialogFragment {

    private EditText renameEditText;
    private RenameDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rename,null);

        builder.setView(view)
                .setTitle(R.string.dialog_rename_device_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.rename, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceName = renameEditText.getText().toString();
                        listener.renameDevice(deviceName);
                    }
                });

        renameEditText = view.findViewById(R.id.dialog_rename);

        return builder.create();
    }

    public interface RenameDialogListener{
        void renameDevice(String deviceName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RenameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement RenameDialogListener");
        }
    }

}
