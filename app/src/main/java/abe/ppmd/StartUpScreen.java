package abe.ppmd;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StartUpScreen extends AppCompatActivity {

    private Button selectPhoto;
    private int REQUEST_CAMERA = 0;
    private File photoFile;
    private String mCurrentPhotoPath;
    private String plantSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        selectPhoto = (Button)findViewById(R.id.take_photo_button);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Camera Intent","Camera Intent Started");
                selectPlant();
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
        if(requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
            if (photoFile.exists()){
                Intent setupScreen = new Intent(this,SetupAnalysis.class);
                setupScreen.putExtra("plant",plantSelected);
                setupScreen.putExtra("file_dir",photoFile);
                startActivity(setupScreen);
            }
        }
    }
    public void photoCapture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.i("Plant Selected",plantSelected);
        if (takePictureIntent.resolveActivity(getPackageManager())!= null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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

    public void selectPlant(){

        final CharSequence[] plants = {"Corn","Soy Bean","Tomato"};
        final AlertDialog.Builder selectPlant = new AlertDialog.Builder(this);
        selectPlant.setTitle(R.string.select_plant_title)
                .setItems(plants, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        plantSelected = plants[i].toString();
                        photoCapture();
                    }
                })
                .show();
    }
}
