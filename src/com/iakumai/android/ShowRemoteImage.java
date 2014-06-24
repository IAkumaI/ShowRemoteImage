package com.iakumai.android;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * Show images by URL in ImageView
 * 
 * Example:
 * 	new ShowRemoteImage((ImageView) findViewById(R.id.imageView1), "http://some.link/to/image.png");
 * 
 * @author Valerii Ozarnichuk
 */
public class ShowRemoteImage extends AsyncTask<String, Void, Bitmap> {
	ImageView imageView;
	
	Bitmap imageBitmap;
    
    static LruCache<String, Bitmap> cache;

    @SuppressWarnings("static-access")
	public ShowRemoteImage(ImageView imageView, String url) {
        this.imageView = imageView;
        
        int cacheSize = (int)(Runtime.getRuntime().maxMemory() / 1024) / 6;
        
        if (this.cache == null) {
        	this.cache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        
        imageBitmap = cache.get(url);
        
        if (imageBitmap != null) {
        	Log.d("ShowRemoteImage", "Image from cache: " + url);
        	this.imageView.setImageBitmap(imageBitmap);
        } else {
        	this.imageView.setImageBitmap(null);
        	this.execute(url);
        }
    }
    
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        
        if (imageBitmap != null) {
        	Log.d("ShowRemoteImage", "Image from cache: " + url);
        	return imageBitmap;
        }
        
        try {
            imageBitmap = BitmapFactory.decodeStream(new URL(url).openStream());
            cache.put(url, imageBitmap);
            Log.d("ShowRemoteImage", "Image loaded: " + url);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        
        return imageBitmap;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}