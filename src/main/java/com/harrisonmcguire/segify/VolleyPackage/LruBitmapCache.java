package com.harrisonmcguire.segify.VolleyPackage;

/**
 * Created by Harrison on 2/17/2015.
 */

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements
        ImageCache {

        // establish cache and max cache size for parsing json data
        // volley.jar cache
        public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }


    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    //cache size
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    // volley.jar getting bitmap images from urls
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    // volley.jar setting bitmap images from urls
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
