package abe.ppmd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Zhihang Song on 2016-07-15.
 * !!! This class is no longer used after the calculation() and all the
 * other related methods have been moved
 * to "ResultScreen.java".
 */


public class Analysis{
    private Bitmap rotatedImage;
    private Bitmap finalImage;
    String variety;
    double Moisture;
    double Nitrogen;
    Bitmap resizedBitmap;
    double NewThreshold = 30;


    private static Context context;
    public Analysis(Context C) {
        context = C;
    }


    public void calculation(Bitmap rotatedImage, double threshold, String variety) {

        NewThreshold = threshold;

        Log.i("***Ana Threshold:",Double.toString(threshold));
        Log.i("**Ana Threshold_2:",Double.toString(NewThreshold));

        int R, G, B;
        int color;
        int n = 0;
        long RGB;
        double I;
        int height = rotatedImage.getHeight();
        int width = rotatedImage.getWidth();
        finalImage = Bitmap.createBitmap(rotatedImage.getWidth(),
                rotatedImage.getHeight(), rotatedImage.getConfig());
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {

                color = rotatedImage.getPixel(c, r);
                R = Color.red(color);
                G = Color.green(color);
                B = Color.blue(color);
                finalImage.setPixel(c, r, Color.rgb(R, G, B));

                if ((G > 0 & (G > R) & (G > B))){
                    RGB = 100 * (G * G - R * B) / (G * G);
                    I = R * R + G * G + B * B;
                    if (RGB >= NewThreshold & (I >= 3000)) {
                        n++;
                        //if (calculateRGB(c, r, finalImage) >= 0.9) {
                            finalImage.setPixel(c, r, Color.rgb(R, G, 255));
                            Moisture = (RGB - 15) * 100 / RGB;
                            Nitrogen = (RGB + 100)/2;
                        //}
                    }
            }
            }
        }
        if (n == 0) {
            Toast.makeText(context, "No Green pixel found!", Toast.LENGTH_LONG).show();
        }
        sendResult();
    }

    public double calculateRGB(int x, int y, Bitmap rotatedImage) {
        int R, G, B;
        double r;
        int color;
        color = rotatedImage.getPixel(x, y);
        R = Color.red(color) ;
        G = Color.green(color) ;
        B = Color.blue(color) ;
        r = 100 * (G * G - R * B) / (G * G);
        return r;
    }

    public void sendResult (){
        // Bitmap resizedBitmap = scaleDown(finalImage, 300, 400, true);
        Intent resultScreen = new Intent(context,ResultScreen.class);
        // resultScreen.putExtra("finalImage",finalImage);
        resultScreen.putExtra("Threshold",NewThreshold);
        //resultScreen.putExtra("Moisture",Moisture);
        //resultScreen.putExtra("Nitrogen",Nitrogen);
        context.startActivity(resultScreen);
    }

    public static Bitmap scaleDown(Bitmap finalImage, float MaxWidth, float MaxHeight, boolean filter){
        float ratio = Math.min(
                MaxWidth / finalImage.getWidth(),
                MaxHeight / finalImage.getHeight());
        int width = Math.round( ratio * finalImage.getWidth());
        int height = Math.round( ratio * finalImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(finalImage,width,height,filter);

        return newBitmap;

    }

    }

