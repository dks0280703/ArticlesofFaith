package com.jmw.lds.articlesoffaith;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jmw.lds.articlesoffaith.toolbox.BitmapHelper;
import com.jmw.lds.articlesoffaith.toolbox.PixelHelper;

/**
 * Created by justin on 11/19/14.
 *
 * Not Currently being used. Because having issues getting it to read from Bitmap asset.
 * Using Drawable Resources for now.
 */
public class MyAppWidgetIntentService extends IntentService {

    private static final String TAG = MyAppWidgetIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MyAppWidgetIntentService() {
        super(MyAppWidgetIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String filePath = intent.getExtras().getString(MyAppWidget.EXTRA_FILE);
        int id = intent.getExtras().getInt(MyAppWidget.EXTRA_ID);

        Bitmap bitmap = getBitmapFromAsset(this, filePath);


        Intent sendIntent = new Intent();
        sendIntent.putExtra(MyAppWidget.EXTRA_BITMAP, bitmap);
        sendIntent.getIntExtra(MyAppWidget.EXTRA_ID, id);
        sendIntent.setAction(MyAppWidget.ACTION_BITMAP);
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendIntent);
    }


    private Bitmap getBitmapFromAsset(Context context, String path) {

        // The height and width is 72 px @mdpi
        int size = (int) PixelHelper.getPixelsFromDips(context, 72.0f);
        // Decode
        Bitmap unscaledBitmap = BitmapHelper.decodeSampledBitmapFromFile(context, path, size, size);
        // Scale Image
        Bitmap bitmap = BitmapHelper.createScaledBitmap(unscaledBitmap, size, size, BitmapHelper.ScalingLogic.CROP);
        return bitmap;
    }

}
