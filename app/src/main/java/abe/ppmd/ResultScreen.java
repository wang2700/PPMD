package abe.ppmd;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class ResultScreen extends AppCompatActivity {

    private Bitmap finalImage;
    private double moisture;
    private double nitrogen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        finalImage = (Bitmap)extras.get("finalImage");
        moisture = (double) extras.get("Moisture");
        nitrogen = (double) extras.get("Nitrogen");

        ImageView imageResult = (ImageView)findViewById(R.id.result_image_view);
        imageResult.setImageBitmap(finalImage);

    }

}
