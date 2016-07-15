package abe.ppmd;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by jerry on 7/15/2016.
 */
public class DataBasePreviewAdapter extends ArrayAdapter {
    public DataBasePreviewAdapter(Context context, Bitmap[] images) {
        super(context, R.layout.database_list_view, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater previewInflater = LayoutInflater.from(getContext());
        View previewView = previewInflater.inflate(R.layout.database_list_view,parent,false);
        Bitmap eachImage = (Bitmap)getItem(position);
        ImageView previewImageView = (ImageView) previewView.findViewById(R.id.image_preview);
        previewImageView.setImageBitmap(eachImage);
        return previewView;
    }
}
