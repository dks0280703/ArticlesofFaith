package com.jmw.lds.articlesoffaith;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.jmw.lds.articlesoffaith.activity.DetailActivity;
import com.jmw.lds.articlesoffaith.activity.MainActivity;
import com.jmw.lds.articlesoffaith.model.Article;
import com.jmw.lds.articlesoffaith.toolbox.BitmapHelper;
import com.jmw.lds.articlesoffaith.toolbox.PixelHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link MyAppWidgetConfigureActivity MyAppWidgetConfigureActivity}
 */
public class MyAppWidget extends AppWidgetProvider {

    public static final String TAG = MyAppWidget.class.getSimpleName();
    public static final String ACTION_BITMAP = "com.jmw.lds.action.bitmap";
    public static final String EXTRA_BITMAP = "bitmap_image";
    public static final String EXTRA_ID = "app_widget_id";
    public static final String EXTRA_FILE = "app_widget_file_path";
    public static final int INTENT_FLAG = 0x99999;
    public static RemoteViews views;
    public static AppWidgetManager appWidgetManager;
    private MyServiceBroadcastReceiver myServiceBroadcastReceiver;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            MyAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }


    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        myServiceBroadcastReceiver = new MyServiceBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BITMAP);
        LocalBroadcastManager.getInstance(context).registerReceiver(myServiceBroadcastReceiver, filter);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        LocalBroadcastManager.getInstance(context).unregisterReceiver(myServiceBroadcastReceiver);
    }

    static void updateAppWidget(Context context, AppWidgetManager widgetManager,
                                int appWidgetId) {

        appWidgetManager = widgetManager;
        CharSequence widgetText = MyAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);

        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setImageViewResource(R.id.appwidget_thumb, R.drawable.thumb_widget_af_11);


        Article article = new Article();
        article.setId(11);
        article.setTitle("Articles of Faith 1:11");
        article.setSubTitle("privilege of worshiping");
        article.setThumb("thumb/thumb_af_11.jpg");
        article.setImage("image/banner_af_11.jpg");
        article.setBody("We claim the privilege of worshiping Almighty God according to the dictates of our own conscience, and allow all men the same privilege, let them worship how, where, or what they may.");



        Intent intent = Intent.makeRestartActivityTask(new ComponentName(context, MainActivity.class));
        intent.putExtra(MainActivity.EXTRA_ARTICLE, article);
        intent.putExtra(EXTRA_ID, 11);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_root, pendingIntent);





//        Intent intent = new Intent(context, MyAppWidgetIntentService.class);
//        intent.putExtra(EXTRA_FILE, "thumb/thumb_af_11.jpg");
//        intent.putExtra(EXTRA_ID, appWidgetId);
//        context.startService(intent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


    // TODO Come back here and have it load from asset rather than resource
    private class MyServiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (TextUtils.equals(action, ACTION_BITMAP)) {
                Log.i(TAG, "OnRecieve for action bitmap");
                Bitmap bitmap = (Bitmap) intent.getParcelableExtra(EXTRA_BITMAP);
                int appWidgetId = (int) intent.getIntExtra(EXTRA_ID, 0);
                if (bitmap != null) {
                    Log.i(TAG, "Bitmap not null");
                }
                if (bitmap instanceof Bitmap) {
                    Log.i(TAG, "IT is a bitmap");
                }
                Bitmap clone = bitmap.copy(Bitmap.Config.ARGB_4444, false);
                views.setImageViewBitmap(R.id.appwidget_thumb, clone);
                appWidgetManager.updateAppWidget(appWidgetId, views);
                //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_thumb);
            }
        }
    }


}


