package abe.ppmd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ResultScreen extends AppCompatActivity {

    private Bitmap finalImage;
    private double moisture;
    private double nitrogen;
    private ImageView ResultImage;
    private double threshold;
    private File photoFile;
    private String plant;
    private Bitmap rotatedImage;
    Bitmap imageImported;

    //private ThresholdDialog thresholdInput = new ThresholdDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int importPhotoOrientation;
        String orientation;

        setContentView(R.layout.activity_result_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle extras = getIntent().getExtras();
        photoFile = (File) extras.get("file_dir");
        plant = (String) extras.get("plant");
        Log.i("0**Setup", "Plant:" + plant);
        imageImported = BitmapFactory.decodeFile(photoFile.getAbsolutePath());


        //check image orientation and rotate
        importPhotoOrientation = getCameraPhotoOrientation(this, Uri.fromFile(photoFile),photoFile);
        orientation = Integer.toString(importPhotoOrientation);
        Log.i("Photo Orientation",orientation);

        // Resize image.

        Bitmap resizedImage = scaleDown(imageImported, 960, 1280, true);

        // Rotate image.
        rotatedImage = rotateImportImage(importPhotoOrientation,resizedImage);
        Log.i("Before display",Boolean.toString(rotatedImage == null));

        analyzePhoto();

        // Former version, get lower resolution bitmap from Analysis.class
        /*
        Bundle extras = getIntent().getExtras();
        finalImage = (Bitmap) extras.get("finalImage");
        moisture = (double) extras.get("Moisture");
        nitrogen = (double) extras.get("Nitrogen");
        threshold = (double) extras.get("Threshold");
        */
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


    public void analyzePhoto() {
        //Analysis calculate = new Analysis(this);

        // Receive information from "SetupAnalysis.java"
        Bundle extras = getIntent().getExtras();
        threshold = (double)extras.get("threshold");

        Log.i("**Setup","Plant:" + plant);
        Log.i("**Setup-Threshold:",Double.toString(threshold));

        calculation(rotatedImage, threshold, plant);
    }

    public void showImage(){
        ResultImage = (ImageView)findViewById(R.id.result_image_view) ;
        ResultImage.setImageBitmap(finalImage);
        if (threshold == 30){
            Toast.makeText(this,"Analysis finished with default threshold value of: " + threshold,Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Analysis finished with User-defined threshold value of: " + threshold,Toast.LENGTH_LONG).show();
        }

    }

    public static Bitmap scaleDown(Bitmap finalImage, float MaxWidth, float MaxHeight, boolean filter){
        float ratio = Math.min(
                MaxWidth / finalImage.getWidth(),
                MaxHeight / finalImage.getHeight());
        int width = Math.round( ratio * finalImage.getWidth());
        int height = Math.round( ratio * finalImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(finalImage,width,height,filter);

        return newBitmap;

    }

    // Analyze the photo.
    public void calculation(Bitmap rotatedImage, double threshold, String variety) {

        Log.i("***Ana Threshold:",Double.toString(threshold));
        Log.i("**Ana Threshold_2:",Double.toString(threshold));

        int R, G, B;
        int color;
        int n = 0;
        long RGB;
        double I;
        int height = rotatedImage.getHeight();
        int width = rotatedImage.getWidth();
        finalImage = Bitmap.createBitmap(rotatedImage.getWidth(),
                rotatedImage.getHeight(), rotatedImage.getConfig());
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {

                color = rotatedImage.getPixel(c, r);
                R = Color.red(color);
                G = Color.green(color);
                B = Color.blue(color);
                finalImage.setPixel(c, r, Color.rgb(R, G, B));

                if ((G > 0 & (G > R) & (G > B))){
                    RGB = 100 * (G * G - R * B) / (G * G);
                    I = R * R + G * G + B * B;
                    if (RGB >= threshold & (I >= 3000)) {
                        n++;
                        //if (calculateRGB(c, r, finalImage) >= 0.9) {
                        finalImage.setPixel(c, r, Color.rgb(255, G, B));
                        moisture = (RGB - 15) * 100 / RGB;
                        nitrogen = (RGB + 100)/2;
                        //}
                    }
                }
            }
        }
        if (n == 0) {
            Toast.makeText(this, "No Green pixel found!", Toast.LENGTH_LONG).show();
        }
        showImage();
    }

    public double calculateRGB(int x, int y, Bitmap rotatedImage) {
        int R, G, B;
        double r;
        int color;
        color = rotatedImage.getPixel(x, y);
        R = Color.red(color) ;
        G = Color.green(color) ;
        B = Color.blue(color) ;
        r = 100 * (G * G - R * B) / (G * G);
        return r;
    }
}
