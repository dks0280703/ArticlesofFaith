package com.jmw.lds.articlesoffaith.toolbox;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jmw.lds.articlesoffaith.AppController;

import java.util.Hashtable;

/**
 * Created by justin on 11/11/14.
 * Font Helper for adding fonts to widgets
 */
public class FontHelper {

    /** Tag for the class */
    private static final String TAG = FontHelper.class.getSimpleName();

    public static final String ROBOTO_THIN = "font/Roboto Thin.TTF";
    public static final String ROBOTO_LIGHT = "font/Roboto Light.TTF";
    public static final String ROBOTO_REG = "font/Roboto Regular.TTF";


    /**
     * Store the opened typefaces (fonts)
     */
    // Store the opened typefaces (fonts)
    private static final Hashtable<String, Typeface> mCache = new Hashtable<>();

    /**
     * Load the given font from assets
     * @param fontName the name of the font in assets
     * @return Typeface object representing the font painting
     */
    public static Typeface loadFontFromAssets(String fontName) {
        // make sure we load each font only once
        synchronized (mCache) {
            if (!mCache.containsKey(fontName)) {
                Context context = AppController.getInstanceContext();
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
                mCache.put(fontName, typeface);
            }

            return mCache.get(fontName);
        }
    }

    /**
     * Helper to apply the font to either the children of a ViewGroup or a single TextView
     * @param context application context
     * @param root the root view of view to start drilling down and applying the font
     * @param fontPath path in assets to the font
     */
    public static void applyFont(final Context context, final View root, final String fontPath){
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    applyFont(context, viewGroup.getChildAt(i), fontPath);
                }
            }else if(root instanceof TextView) {
                ((TextView) root).setTypeface(loadFontFromAssets(fontPath));
            }
        }catch (Exception e) {
            Log.e(TAG, String.format("Error occurred when trying to apply %s font for %s view", fontPath, root));
        }
    }

    /**
     * Helper to apply font to a textView
     * @param textView to apply the font to
     * @param fontPath path in assets to the font
     */
    public static void applyFontToTextView(TextView textView, String fontPath) {
        try {
            if (textView instanceof TextView) {
                textView.setTypeface(loadFontFromAssets(fontPath));
            }
        }catch (Exception e){
            Log.e(TAG, String.format("Error occurred when trying to apply %s font for textView", fontPath));
            e.printStackTrace();
        }
    }

    /**
     * Helper to apply font to a button
     * @param button to appy the font to
     * @param fontPath path in assets to the font
     */
    public static void applyFontToButton(Button button, String fontPath){
        try {
            if (button instanceof Button) {
                button.setTypeface(loadFontFromAssets(fontPath));
            }
        }catch (Exception e){
            Log.e(TAG, String.format("Error occurred when trying to apply %s font for button", fontPath));
        }
    }



}
