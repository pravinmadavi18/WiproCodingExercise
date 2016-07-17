package com.wipro.networkhandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * ConnectionManager class manages the Singleton instance of Volley RequestQueue and ImageLoader
 * @author Pravin Madavi
 * @version 1.0
 */
public class ConnectionManager {

    //Singleton instance used in overall application
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    //Get the Singleton instance of Volley RequestQueue
    public static RequestQueue getInstance(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    //Get the Singleton instance of Volley ImageLoader
    public static ImageLoader getImageLoader(Context context) {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(getInstance(context), new ImageLoader.ImageCache() {

                //Cache to hold the images
                private final LruCache<String, Bitmap>
                        mCache = new LruCache<String, Bitmap>(20);

                @Override
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url,bitmap);
                }
            });
        }
        return mImageLoader;
    }
}
