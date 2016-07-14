package abe.ppmd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class SetupAnalysis extends AppCompatActivity {

    private Bitmap imageImported;
    private File photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        photoFile = (File) extras.get("file_dir");
        imageImported = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        ImageView image = (ImageView)findViewById(R.id.imported_image);
        image.setImageBitmap(imageImported);
        Log.i("Picture Height",Integer.toString(imageImported.getHeight()));
        Log.i("Picture Width",Integer.toString(imageImported.getWidth()));
    }

}
