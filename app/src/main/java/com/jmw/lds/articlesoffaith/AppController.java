package com.jmw.lds.articlesoffaith;

import android.app.Application;
import android.content.Context;

import com.jmw.volley.JMWLruImageCache;
import com.jmw.volley.JMWRequestQueue;
import com.jmw.volley.toolbox.JMWImageLoader;
import com.jmw.volley.toolbox.JMWVolley;

/**
 * Created by justin on 11/11/14.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private JMWRequestQueue mRequestQueue;
    private JMWImageLoader mImageLoader;
    private JMWLruImageCache mLruImageCache;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    /**
     * Get the instance of this Application
     * @return
     */
    public static synchronized AppController getInstance(){ return  mInstance; }

    /**
     * Get the application context for this instance
     * @return the application context for the instance of this application
     */
    public static Context getInstanceContext(){ return  getInstance().getApplicationContext(); }


    public JMWRequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = JMWVolley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public JMWImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mLruImageCache = new JMWLruImageCache();
            mImageLoader = new JMWImageLoader(mRequestQueue, mLruImageCache);
        }
        return  this.mImageLoader;
    }
}
