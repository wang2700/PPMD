package abe.ppmd;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StartUpScreen extends AppCompatActivity {

    private Button selectPhoto;
    private Bitmap imageImported;
    private int REQUEST_CAMERA = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        selectPhoto = (Button)findViewById(R.id.take_photo_button);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Camera Intent","Camera Intent Started");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult","onActivityResult started");
        Log.i("Activity.RESULT_OK",Integer.toString(Activity.RESULT_OK));
        Log.i("RequestCode",Integer.toString(requestCode));
        Log.i("ResultCode",Integer.toString(resultCode));
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }
    }
    private void onCaptureImageResult(Intent data) {
        Log.i("image camera","Getting image from camera");
        imageImported = (Bitmap) data.getExtras().get("data");
        Log.i("transfer","Beginning transfer image");
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //imageImported.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        //byte[] imageArray = bytes.toByteArray();
        Intent setupScreen = new Intent(this,SetupAnalysis.class);
        //setupScreen.putExtra("picture",imageArray);
        setupScreen.putExtra("picture",imageImported);
        startActivity(setupScreen);
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

        return super.onOptionsItemSelected(item);
    }
}
