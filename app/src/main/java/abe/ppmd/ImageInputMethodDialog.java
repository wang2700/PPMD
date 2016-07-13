package abe.ppmd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by jerry on 7/10/2016.
 */
public class ImageInputMethodDialog extends DialogFragment {
    private String userChosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Bitmap imageImported;

    Context context;
    @SuppressLint("ValidFragment")
    public ImageInputMethodDialog(Context myContext) {
        context = myContext;
    }

    public ImageInputMethodDialog() {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder inputMethod = new AlertDialog.Builder(getActivity());
        final CharSequence[] photoMenu = {getString(R.string.choose_sourcce_camera), getString(R.string.choose_source_gallery), "Cancel"};
        inputMethod.setTitle("Choose a source");
        inputMethod.setItems(photoMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            }else if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {

        imageImported = (Bitmap) data.getExtras().get("data");
        transferImageToNext(imageImported);
    }

    private void transferImageToNext(Bitmap imageImported) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageImported.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] imageArray = bytes.toByteArray();
        Intent setupScreen = new Intent(context,SetupAnalysis.class);
        setupScreen.putExtra("picture",imageArray);
        startActivity(setupScreen);
    }

    private void onSelectFromGalleryResult(Intent data) {
        imageImported = null;
        if (data != null){
            try{
                imageImported = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(),data.getData());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        transferImageToNext(imageImported);
    }

}

