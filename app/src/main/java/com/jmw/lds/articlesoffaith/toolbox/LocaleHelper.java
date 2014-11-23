package com.jmw.lds.articlesoffaith.toolbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jmw.lds.articlesoffaith.AppController;
import com.jmw.lds.articlesoffaith.R;
import com.jmw.lds.articlesoffaith.model.Article;

import java.util.Locale;

/**
 * Created by justin on 11/23/14.
 */
public class LocaleHelper {

    private static final String TAG = LocaleHelper.class.getSimpleName();
    private static final String DATA_PATH = "data/data.json";
    private static final String PREF_KEY = "key_language";
    private static final String PREFS_NAME = "com.jmw.lds.articlesoffaith.language";


    public static final String getData(){

        String path = DATA_PATH;
        String[] languages = AppController.getInstanceContext().getResources().getStringArray(R.array.locale);
        String[] files = AppController.getInstanceContext().getResources().getStringArray(R.array.file);
        // Get the current Language being used
        String locale = loadLanguagePref();
        try {
            for (int i = 0; i < languages.length; i++) {
                if (TextUtils.equals(locale, languages[i])) {
                    path = files[i];
                }
            }
        }catch (Exception e){}


        Log.i(TAG, "Locale Language is " + locale);

        return path;
    }


    public static void saveLanguagePref(String language) {
        SharedPreferences.Editor prefs = AppController.getInstanceContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        // Store Language
        prefs.putString(PREF_KEY, language);
        prefs.commit();

        Log.d(TAG, "Saved Langauge is "+language);
    }

    public static void deleteLanguagePref() {
        SharedPreferences.Editor prefs = AppController.getInstanceContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.remove(PREF_KEY);
        prefs.commit();
    }

    public static String loadLanguagePref() {
        SharedPreferences prefs = AppController.getInstanceContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String language = prefs.getString(PREF_KEY, Locale.getDefault().getLanguage());
        if (language == null) {
            Log.e(TAG, "Not able to read the prefence");
            return null;
        }

        Log.i(TAG, "Locale Language is " + language);

        return language;
    }
}
