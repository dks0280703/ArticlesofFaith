package com.jmw.lds.articlesoffaith.toolbox;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by dks0280703 on 5/1/14.
 */
public class PixelHelper {

    public static float getDipsFromPixels(Context context, float pixelsValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density  = metrics.density;
        return pixelsValue / density;

    }

    public static float getPixelsFromDips(Context context, float dipsValue){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipsValue, metrics);
    }

    public static int getPixelsForDensityBasedOnMDPISize(Context context, float size){
        //Activity activity = (Activity)context;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)((Math.round(size))*scale+0.5f);
    }

    public static int getLargeImageForDensityBasedOnScreenSize(Context context){
        //int sdk = android.os.Build.VERSION.SDK_INT;
        int width = 0;
        //int height = 0;

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;

        Activity activity = (Activity)context;
        final float scale = activity.getResources().getDisplayMetrics().density;
        int padding =(int)(40*scale+0.5);


        return width - padding;
    }


    public static int getScreenWidthInPixels(Context context){

        int width = 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;

        return width;
    }



    public static float getScreenWidthInDips(Context context){

        int width = 0;
        float density  = context.getResources().getDisplayMetrics().density;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth  = displayMetrics.widthPixels / density;
        return dpWidth;
    }

    public static int getScreenHeightInPixels(Context context){

        int height = 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        height = displayMetrics.heightPixels;


        return height;
    }


    public static float getScreenHeightInDips(Context context){

        int height = 0;
        float density = context.getResources().getDisplayMetrics().density;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / density;
        return dpHeight;
    }


}
