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
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Bitmap imageImported;
    private ImageView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        selectPhoto = (Button)findViewById(R.id.take_photo_button);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment myFragment = new ImageInputMethodDialog();
                myFragment.show(getFragmentManager(), "theDialog");
            }
        });
        test = (ImageView) findViewById(R.id.test);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            }else if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {

        imageImported = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageImported.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        test.setImageBitmap(imageImported);
    }

    private void onSelectFromGalleryResult(Intent data) {
        imageImported = null;
        if (data != null){
            try{
                imageImported = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),data.getData());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        test.setImageBitmap(imageImported);
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
