package com.windwarriors.appetite.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.IOException;
import java.io.InputStream;

// Download an image from a url link asynchronously, and load it into the ImageView received
// in the constructor
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    ProgressBar _progressBar;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    public DownloadImageTask(ImageView bmImage, ProgressBar progressBar) {
        this.bmImage = bmImage;
        this._progressBar = progressBar;
    }

    protected Bitmap doInBackground(String... urls) {
        // Setup Bitmap factory options to reduce memory required for an image
        BitmapFactory.Options options = new BitmapFactory.Options();
        // Resamples the photo to one quarter of its original size
        options.inSampleSize = 4;

        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        InputStream in = null;
        try {
            in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);

        if(_progressBar != null)
            _progressBar.setVisibility(View.GONE);
    }
}