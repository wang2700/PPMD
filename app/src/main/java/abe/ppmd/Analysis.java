package abe.ppmd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

/**
 * Created by Zhihang Song on 2016-07-15.
 */
public class Analysis {
    private Bitmap rotatedImage;
    private double threshold;
    String variety;

    private static Context context;
    public Analysis(Context C){
        context = C;
    }

    public String calculation (){
        SetupAnalysis getvalue = new SetupAnalysis();
        rotatedImage = getvalue.getRotatedImage();
        threshold = getvalue.getThreshold();
        variety = getvalue.getPlant();

        int R, G, B;
        int color;
        double n = 0;
        double RGB;
        double I;
        String f = "";
        int height = rotatedImage.getHeight();
        int width = rotatedImage.getWidth();
        Bitmap finalimage = Bitmap.createBitmap(rotatedImage.getWidth(),
                rotatedImage.getHeight(),rotatedImage.getConfig());
        for (int r = 0; r < height; r++){
            for (int c = 0; c < width; c++){

                color = rotatedImage.getPixel(c,r);
                R = Color.red(color);
                G = Color.green(color);
                B = Color.blue(color);
                finalimage.setPixel(c,r,Color.rgb(R,G,B));

                RGB = 100 * (G * G - R * B) / (G * G);
                I = R * R + G * G + B * B;

                if(RGB >= threshold & (G > R) & (G > B) & (I >= 100)){
                    n++;
                    if (calculateRGB(c,r, finalimage) >= 0.9 /* &
                                      calculateRGB(c,r,finalimage) <= 1.2*/){
                        finalimage.setPixel(c,r, Color.rgb(255,100,0));
                    }
                }
                if (n == 0){
                    Toast.makeText(context,"No Green pixel found!",Toast.LENGTH_LONG).show();
                }
                else {

                }
        }
        }
        return f;
        }
    public double calculateRGB(int x, int y, Bitmap rotatedImage){
        int R, G, B;
        double r;
        int color;
        color = rotatedImage.getPixel(x,y);
        R = Color.red(color);
        G = Color.green(color);
        B = Color.blue(color);
        r = 100 * (G * G - R * B) / (G * G);
        return r;
        }
    }
