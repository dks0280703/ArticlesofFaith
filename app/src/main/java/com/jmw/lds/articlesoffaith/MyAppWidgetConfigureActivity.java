package com.jmw.lds.articlesoffaith;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jmw.lds.articlesoffaith.activity.AbsParseDataActivity;
import com.jmw.lds.articlesoffaith.model.Article;
import com.jmw.lds.articlesoffaith.toolbox.GsonHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * The configuration screen for the {@link MyAppWidget MyAppWidget} AppWidget.
 */
public class MyAppWidgetConfigureActivity extends AbsParseDataActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.toolbar_view)
    Toolbar toolbar;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.article_spinner)
    Spinner spinner;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.submit)
    Button submit;

    private static final String TAG = MyAppWidgetConfigureActivity.class.getSimpleName();
    private static final String PREFS_NAME = "com.jmw.lds.articlesoffaith.MyAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private List<Article> mList;
    private Article mArticle;


    public MyAppWidgetConfigureActivity() {
        super();
    }

    @Override
    protected void setUpListView(List<Article> list) {
        mList = list;
        // Right away set article default
        mArticle = mList.get(0);

        // Setup the Spinner
        ArrayAdapter adapter = new ArticleAdapter(this, R.layout.row_select_article, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        // Set here to be sure that they cannot submit without a choice
        submit.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.my_app_widget_configure);
        ButterKnife.inject(this);
        setUpToolBar(toolbar, getString(R.string.appwidget_text));

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }




    public static void saveArticlePref(Context context, int appWidgetId, Article article) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        // StoreJSON
        GsonHelper gsonHelper = new GsonHelper();
        Gson gson = gsonHelper.getGson();
        String jsonString = gson.toJson(article);
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, jsonString);
        prefs.commit();
    }

    public static void deleteArticlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }

    public static Article loadArticlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String jsonString = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (jsonString == null) {
            Log.e(TAG, "Not able to read the prefence");
            return null;
        }

        // Get JSON
        GsonHelper gsonHelper = new GsonHelper();
        Gson gson = gsonHelper.getGson();
        Article article = gson.fromJson(jsonString, Article.class);
        return article;
    }


    @Override
    public void onClick(View v) {

        final Context context = MyAppWidgetConfigureActivity.this;
        deleteArticlePref(context, mAppWidgetId);
        saveArticlePref(context, mAppWidgetId, mArticle);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        MyAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);


        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mArticle = mList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mArticle = mList.get(0);
    }


    public class ArticleAdapter extends ArrayAdapter<Article>
    {

        private List<Article> objects;
        private int layout;

        public ArticleAdapter(Context context, int resource, List<Article> objects) {
            super(context, resource, objects);
            this.objects = objects;
            this.layout = resource;
        }

        @Override
        public int getCount(){ return (objects !=null && !objects.isEmpty())?objects.size():0; }

        public View getDropDownView(int position, View convertView, ViewGroup parent){
            View row = convertView;
            /*
            LayoutInflater inflater = getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
             */
            row = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            TextView textView = (TextView) row.findViewById(R.id.list_item);
            textView.setText(objects.get(position).getTitle());
            return row;
        }


        public View getView(int position, View convertView, ViewGroup parent){
            View row = convertView;
            /*
            LayoutInflater inflater = getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
             */
            row = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            TextView textView = (TextView) row.findViewById(R.id.list_item);
            textView.setText(objects.get(position).getTitle());
            return row;
        }

    }
}



