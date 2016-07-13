package abe.ppmd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class SetupAnalysis extends AppCompatActivity {

    private Bitmap imageImported;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        byte[] imageArray = extras.getByteArray("picture");
        imageImported = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
        ImageView image = (ImageView)findViewById(R.id.imported_image);
        image.setImageBitmap(imageImported);
    }

}
