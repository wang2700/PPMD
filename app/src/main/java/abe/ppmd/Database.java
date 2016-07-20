package abe.ppmd;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;

public class Database extends AppCompatActivity {


    private File[] allFiles;
    private Bitmap[] images;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String photoPath;

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

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    // Get the path to the selected photo in Gallery.
    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_OK && resultCode == PICK_IMAGE){
            imageUri = data.getData();
            photoPath = getPath(imageUri);
            Log.i("*() Check Photo path",Boolean.toString(photoPath == null));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_up_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.load_from_gallery){
            openGallery();
        }

        return super.onOptionsItemSelected(item);
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
