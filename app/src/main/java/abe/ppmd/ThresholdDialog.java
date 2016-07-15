package abe.ppmd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by jerry on 7/15/2016.
 */
public class ThresholdDialog extends DialogFragment {



    private double threshold;
    private Context context;

    @SuppressLint("ValidFragment")
    public ThresholdDialog(Context context){
        this.context = context;
    }

    public ThresholdDialog() {
    }

    public double getThreshold() {
        return threshold;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText thresholdInput = new EditText(context);
        AlertDialog.Builder setThreshold = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        setThreshold.setView(inflater.inflate(R.layout.set_threshold_edit_text,null))
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        threshold = Double.parseDouble(thresholdInput.getText().toString());
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return setThreshold.create();
    }
}
