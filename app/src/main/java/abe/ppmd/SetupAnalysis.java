package abe.ppmd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class SetupAnalysis extends AppCompatActivity {

    private Bitmap rotatedImage;
    private File photoFile;
    private double threshold = 50;
    private String plant;
    private ThresholdDialog thresholdInput = new ThresholdDialog(this);
    int width, height;
    double aspectRatio;
    //Bitmap resizedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int importPhotoOrientation;
        String orientation;
        Bitmap imageImported;
        setContentView(R.layout.activity_setup_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Receive image file path and plant from StartupScreen
        Bundle extras = getIntent().getExtras();
        photoFile = (File) extras.get("file_dir");
        plant = (String) extras.get("plant");
        Log.i("**Setup","Plant:" + plant);
        imageImported = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

        //check image orientation and rotate
        importPhotoOrientation = getCameraPhotoOrientation(this,Uri.fromFile(photoFile),photoFile);
        orientation = Integer.toString(importPhotoOrientation);
        Log.i("Photo Orientation",orientation);

        // Rotate image.
        rotatedImage = rotateImportImage(importPhotoOrientation,imageImported);
        Log.i("Before display",Boolean.toString(rotatedImage == null));
        width = rotatedImage.getWidth();
        height = rotatedImage.getHeight();
        aspectRatio = width / height;

        // If it is necessary:
        // Check if the aspect ratio (w : h) of he image is 3 : 4 (0.75).
        // If not, popup alert message and require for changing camera settings and
        // retake image.
        /*
        if(aspectRatio != 0.75){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Cannot continue analyzing! \n Please set your camera aspect ratio to 4:3");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            alertDialog.show();
        }
        */

        // resize image
        if (aspectRatio == 0.75) {
            Bitmap resizedImage = scaleDown(rotatedImage, 600, 800, true);
            ImageView image = (ImageView)findViewById(R.id.imported_image);
            image.setImageBitmap(resizedImage);
        }
        else {
            Bitmap resizedImage = scaleDown(rotatedImage, 450, 800, true);
            ImageView image = (ImageView)findViewById(R.id.imported_image);
            image.setImageBitmap(resizedImage);
        }

        // Check LOG
        Log.i("Picture Height",Integer.toString(imageImported.getHeight()));
        Log.i("Picture Width",Integer.toString(imageImported.getWidth()));
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, File imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);

            ExifInterface exif = new ExifInterface(imagePath.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public Bitmap rotateImportImage (int rotateDegree, Bitmap importImage){
        Bitmap rotatedImage;
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        rotatedImage = Bitmap.createBitmap(importImage,0,0,importImage.getWidth(),importImage.getHeight(),matrix,true);
        return rotatedImage;
    }

    public void  setThreshold(View v){
        thresholdInput.show(getFragmentManager(),"threshold");
    }

    public void analyzePhoto(View view) {
        Analysis calculate = new Analysis(this);

        threshold = thresholdInput.getThreshold();
        Log.i("**Setup-Threshold:",Double.toString(threshold));

        calculate.calculation(rotatedImage, threshold, plant);
    }

    public Bitmap getRotatedImage() {
        Log.i("Before return",Boolean.toString(rotatedImage == null));
        return rotatedImage;
    }

    public static Bitmap scaleDown(Bitmap finalImage, float MaxWidth, float MaxHeight, boolean filter) {
        float ratio = Math.min(
                MaxWidth / finalImage.getWidth(),
                MaxHeight / finalImage.getHeight());
        int width = Math.round(ratio * finalImage.getWidth());
        int height = Math.round(ratio * finalImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(finalImage, width, height, filter);

        return newBitmap;
    }

    public double getThreshold() {
        return threshold;
    }

    public String getPlant() {
        return plant;
    }

    public SetupAnalysis (){
    }
}
