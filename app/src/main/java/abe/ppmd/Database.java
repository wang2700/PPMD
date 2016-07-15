package abe.ppmd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;

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


    }

    private void scanPhoto(File storageDir) {
        int index;
        Bitmap image;
        final int THUMBNAIL_SIZE_WIDTH = 75;
        final int THUMBNAIL_SIZE_HEIGHT = 100;
        allFiles = storageDir.getAbsoluteFile().listFiles();
        images = new Bitmap[allFiles.length];
        Log.i("No. of File",Integer.toString(allFiles.length));
        Log.i("1st file",allFiles[0].getAbsolutePath().toString());
        for (index = 0; index < allFiles.length; index++){
            Log.i("Image","Read Image");
            images[index] = BitmapFactory.decodeFile(allFiles[index].getAbsolutePath().toString());
        }
    }

}
