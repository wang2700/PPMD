package abe.ppmd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.net.Uri;

import java.io.File;

public class SetupAnalysis extends AppCompatActivity {

    private Bitmap rotatedImage;
    private File photoFile;
    private float threshold;
    private String plant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int importPhotoOrientation;
        String orientation;
        Bitmap imageImported;
        setContentView(R.layout.activity_setup_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Receive image file path from StartupScreen
        Bundle extras = getIntent().getExtras();
        photoFile = (File) extras.get("file_dir");
        plant = (String) extras.get("plant");
        Log.i("Setup","Plant:" + plant);
        imageImported = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

        //check image orientation and rotate
        importPhotoOrientation = getCameraPhotoOrientation(this,Uri.fromFile(photoFile),photoFile);
        orientation = Integer.toString(importPhotoOrientation);
        Log.i("Photo Orientation",orientation);
        rotatedImage = rotateImportImage(importPhotoOrientation,imageImported);

        //display image
        ImageView image = (ImageView)findViewById(R.id.imported_image);
        image.setImageBitmap(rotatedImage);
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

    public void setThreshold(View view) {



    }
}
