package abe.ppmd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;
/**
 * Created by Zhihang Song on 2016-07-15.
 */
public class Analysis{
    private Bitmap rotatedImage;
    private double threshold;
    private Bitmap finalImage;
    String variety;
    double Moisture;
    double Nitrogen;


    private static Context context;

    public Analysis(Context C) {
        context = C;
    }

    public void calculation(Bitmap rotatedImage, double threshold, String variety) {

        int R, G, B;
        int color;
        double n = 0;
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
                if (RGB >= threshold & (I >= 100)) {
                    n++;
                    if (calculateRGB(c, r, finalImage) >= 0.9 /* &
                                      calculateRGB(c,r,finalimage) <= 1.2*/) {
                        finalImage.setPixel(c, r, Color.rgb(255, 100, 0));
                        Moisture = (RGB - 15) * 100 / RGB;
                        Nitrogen = (RGB + 100)/2;
                    }
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
        Intent resultScreen = new Intent(context,ResultScreen.class);
        resultScreen.putExtra("finalImage",finalImage);
        resultScreen.putExtra("Moisture",Moisture);
        resultScreen.putExtra("Nitrogen",Nitrogen);
        context.startActivity(resultScreen);
    }

    }
