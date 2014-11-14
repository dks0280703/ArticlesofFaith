package com.jmw.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.jmw.volley.toolbox.JMWImageLoader;

/**
 * Created by dks0280703 on 10/15/14.
 */
public class JMWLruImageCache extends LruCache<String, Bitmap> implements
        ImageLoader.ImageCache, JMWImageLoader.ImageCache {
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        // This is a change

        return cacheSize;
    }

    public JMWLruImageCache() {
        this(getDefaultLruCacheSize());
    }

    public JMWLruImageCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}