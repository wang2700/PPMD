package abe.ppmd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
        AlertDialog.Builder setThreshold = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.set_threshold_edit_text, null);
        final EditText thresholdInput = (EditText) dialog.findViewById(R.id.threshold_edit_text);
        setThreshold.setView(dialog)
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Log.i("**Threshold",thresholdInput.getText().toString());

                        threshold = Double.valueOf(thresholdInput.getText().toString());

                        Log.i("**Threshold2",Double.toString(threshold));

                        // dialogInterface.cancel();
                    }
                })
                .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dialogInterface.cancel();
                    }
                });
        Log.i("**FinalThreshold",Double.toString(threshold));
        return setThreshold.create();
    }
}
