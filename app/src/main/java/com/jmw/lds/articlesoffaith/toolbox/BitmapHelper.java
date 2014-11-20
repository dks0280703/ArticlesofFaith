package com.jmw.lds.articlesoffaith.toolbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by justin on 11/19/14.
 */
public class BitmapHelper {

    public static Bitmap decodeSampledBitmapFromFile(Context context, String file, int reqWidth, int reqHeight){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        AssetManager assetManager = context.getAssets();
        InputStream inputStream;
        Bitmap bitmap = null;
        try {
            inputStream = assetManager.open(file);
            BitmapFactory.decodeStream(inputStream, null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        }catch (IOException e){}

        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if(height> reqHeight || width > reqWidth){
            if(width>height){
                inSampleSize = Math.round((float)height/(float)reqHeight);
            }else{
                inSampleSize = Math.round((float)width/(float)reqWidth);
            }
        }

        return inSampleSize;
    }


    /* SCALING */
    public static enum ScalingLogic{
        CROP, FIT
    }


    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLocgic){
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLocgic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLocgic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }


    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic){
        final float srcAspect = (float)srcWidth/(float)srcHeight;
        final float dstAspect = (float)dstWidth/(float)dstHeight;
        if(scalingLogic == ScalingLogic.FIT){
            if(srcAspect > dstAspect){
                return srcWidth/dstWidth;
            }else{
                return srcHeight/dstHeight;
            }
        }else{
            if(srcAspect > dstAspect){
                return srcHeight/dstHeight;
            }else{
                return srcWidth/dstWidth;
            }
        }
    }


    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic){
        if(scalingLogic == ScalingLogic.CROP){
            final  float srcAspect = (float)srcWidth/(float)srcHeight;
            final float dstAspect = (float)dstWidth/(float)dstHeight;

            if(srcAspect > dstAspect){
                final int srcRectWidth = (int)(srcHeight*dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth)/2;
                return new Rect(srcRectLeft, 0, +srcRectWidth, srcHeight);
            }else{
                final int srcRectHeight = (int)(srcWidth/dstAspect);
                final int srcRectTop = (int)(srcHeight - srcRectHeight)/2;
                return new Rect(0, srcRectTop, srcWidth, srcRectTop+srcRectHeight);
            }
        }else{
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic){
        if(scalingLogic == ScalingLogic.FIT){
            final float srcAspect = (float)srcWidth/(float)srcHeight;
            final float dstAspect = (float)dstWidth/(float)dstHeight;
            if(srcAspect > dstAspect){
                return new Rect(0,0,dstWidth,(int)(dstWidth/srcAspect));
            }else{
                return new Rect(0,0,(int)(dstHeight*srcAspect),dstHeight);
            }

        }else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }
}
