package abe.ppmd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class SetupAnalysis extends AppCompatActivity {

    private Bitmap rotatedImage;
    private File photoFile;
    private double threshold = 30;
    private String plant;
    private ThresholdDialog thresholdInput = new ThresholdDialog(this);
    int width, height;
    double aspectRatio;
    Bitmap resizedImage;
    Button startAnalyze;

    int methodCode;
    int importPhotoOrientation;
    String orientation;
    Bitmap imageImported;
    String photoPath;
    String selectedPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Receive image file path and plant from StartupScreen or Database;
        Bundle extras = getIntent().getExtras();
        methodCode = (int)extras.get("methodCode");
        Log.i("*** check methodCode",Integer.toString(methodCode));
        // "0" means the info comes from camera; "1" means comes from gallery.
        switch (methodCode){
            case 0:
                photoFile = (File) extras.get("file_dir");
                imageImported = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                //check image orientation and rotate
                importPhotoOrientation = getCameraPhotoOrientation(this,Uri.fromFile(photoFile),photoFile);
                orientation = Integer.toString(importPhotoOrientation);
                Log.i("*0* Photo Orientation",orientation);
                break;

            case 1:
                photoPath = (String) extras.get("path_from_gallery");
                photoFile = new File(photoPath);
                Log.i("*** path",photoPath);
                imageImported = BitmapFactory.decodeFile(photoPath);
                importPhotoOrientation = getCameraPhotoOrientation(this,Uri.fromFile(photoFile),photoFile);
                orientation = Integer.toString(importPhotoOrientation);
                Log.i("*0* Photo Orientation",orientation);
                break;

        }

        // Rotate image.
        rotatedImage = rotateImportImage(importPhotoOrientation,imageImported);
        Log.i("Before display",Boolean.toString(rotatedImage == null));
        width = rotatedImage.getWidth();
        height = rotatedImage.getHeight();
        aspectRatio = width / height;

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

        startAnalyze = (Button)findViewById(R.id.analyze_button);
        startAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPlant();
            }
        });

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


    public void  setThreshold(View v){
        thresholdInput.show(getFragmentManager(),"threshold");
    }

    public void startAnalyzing() {
        threshold = thresholdInput.getThreshold();

        Intent startAnalyze = new Intent(this,ResultScreen.class);
        int methodCode = 0;
        startAnalyze.putExtra("file_dir",photoFile);
        startAnalyze.putExtra("threshold",threshold);
        startAnalyze.putExtra("methodCode",methodCode);
        startAnalyze.putExtra("plant",selectedPlant);
        startActivity(startAnalyze);
    }

    public String selectPlant() {
        final CharSequence[] plants = {"Corn", "Soy Bean", "Tomato"};
        final AlertDialog.Builder selectPlant = new AlertDialog.Builder(this);
        selectPlant.setTitle(R.string.select_plant_title)
                .setItems(plants, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedPlant = plants[i].toString();
                        startAnalyzing();
                    }
                })
                .show();
        // Log.i("** Check plant",plantSelected);
        return selectedPlant;
    }


    public double getThreshold() {
        return threshold;
    }

    public String getPlant() {
        return plant;
    }

    public SetupAnalysis (){}
}
