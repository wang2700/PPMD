package abe.ppmd;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

public class ResultScreen extends AppCompatActivity {

    private Bitmap finalImage;
    private double moisture;
    private double nitrogen;
    private ImageView ResultImage;
    private double threshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        finalImage = (Bitmap) extras.get("finalImage");
        //moisture = (double) extras.get("Moisture");
        //nitrogen = (double) extras.get("Nitrogen");
        threshold = (double) extras.get("Threshold");

        ResultImage = (ImageView)findViewById(R.id.result_image_view) ;
        ResultImage.setImageBitmap(finalImage);
        if (threshold == 30){
            Toast.makeText(this,"Analysis finished with default threshold value of: " + threshold,Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Analysis finished with User-defined threshold value of: " + threshold,Toast.LENGTH_LONG).show();
        }

    }
}
