package abe.ppmd;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Created by jerry on 7/10/2016.
 */
public class ImageInputMethodDialog extends DialogFragment {
    private String userChosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder inputMethod = new AlertDialog.Builder(getActivity());
        final CharSequence[] photoMenu = {getString(R.string.choose_sourcce_camera), getString(R.string.choose_source_gallery), "Cancel"};
        inputMethod.setTitle("Choose a source");
        inputMethod.setItems(photoMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                //boolean result = Utility.checkPermission(ImageInputMethodDialog.this);
                if (photoMenu[item].equals(getString(R.string.choose_sourcce_camera))) {
                    userChosenTask = getString(R.string.choose_sourcce_camera);
                        cameraIntent();
                } else if (photoMenu[item].equals(getString(R.string.choose_source_gallery))) {
                    userChosenTask = getString(R.string.choose_source_gallery);
                        galleryIntent();
                }
            }
        });
        return inputMethod.create();
    }

    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"),SELECT_FILE);

    }

    private void cameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
}

