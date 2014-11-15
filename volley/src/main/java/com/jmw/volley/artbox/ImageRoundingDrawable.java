package com.jmw.volley.artbox;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by justin on 11/15/14.
 */
public class ImageRoundingDrawable extends Drawable {

    private static final String TAG = ImageRoundingDrawable.class.getSimpleName();
    private Paint paintObject;


    public ImageRoundingDrawable(Bitmap sourceImage) {

        Log.d(TAG, "SourceImage = " + sourceImage.getHeight());

        BitmapShader imageShader = new BitmapShader(sourceImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paintObject = new Paint();
        paintObject.setAntiAlias(true);
        paintObject.setShader(imageShader);

    }

    @Override
    public void draw(Canvas canvas) {
        int canvasH = getBounds().height();
        int canvasW = getBounds().width();
        RectF drawRect = new RectF(0.0f, 0.0f, canvasW, canvasH);
        canvas.drawRoundRect(drawRect, 50, 50, paintObject);
    }

    @Override
    public void setAlpha(int alpha) {
        // Not implementing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // Not implementing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
