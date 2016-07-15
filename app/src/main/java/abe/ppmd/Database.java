package abe.ppmd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;

public class Database extends AppCompatActivity {


    private File[] allFiles;
    private Bitmap[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("Storage Dir",storageDir.toString());
        scanPhoto(storageDir);

        ListAdapter imageAdapter = new DataBasePreviewAdapter(this,images);
        ListView previewListView = (ListView)findViewById(R.id.image_list_view);
        previewListView.setAdapter(imageAdapter);


    }

    private void scanPhoto(File storageDir) {
        int index;

        final int THUMBNAIL_SIZE_WIDTH = 75;
        final int THUMBNAIL_SIZE_HEIGHT = 100;
        allFiles = storageDir.getAbsoluteFile().listFiles();
        images = new Bitmap[allFiles.length];
        Log.i("No. of File",Integer.toString(allFiles.length));
        Log.i("1st file",allFiles[0].getAbsolutePath().toString());
        for (index = 0; index < allFiles.length; index++){
            Log.i("Image","Read Image");
            images[index] = BitmapFactory.decodeFile(allFiles[index].getAbsolutePath().toString());
            images[index] = rotateImportImage(getCameraPhotoOrientation(this,Uri.fromFile(allFiles[index]),allFiles[index]),images[index]);
        }
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
        Bitmap scaledImage;
        switch (rotateDegree){
            case 90: scaledImage = Bitmap.createScaledBitmap(importImage,400,300,true);
                break;
            case 180: scaledImage = Bitmap.createScaledBitmap(importImage,300,400,true);
                break;
            case 270: scaledImage = Bitmap.createScaledBitmap(importImage,400,300,true);
                break;
            default: scaledImage = Bitmap.createScaledBitmap(importImage,300,400,true);
        }
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        rotatedImage = Bitmap.createBitmap(scaledImage,0,0,scaledImage.getWidth(),scaledImage.getHeight(),matrix,true);
        return rotatedImage;
    }
}
