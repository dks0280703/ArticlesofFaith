package com.jmw.lds.articlesoffaith.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.jmw.lds.articlesoffaith.toolbox.FontHelper;


/**
 * Created by justin on 11/11/14.
 */
public class MyTextView extends TextView{

    public static final String TAG = MyTextView.class.getSimpleName();

    public MyTextView(Context context) {
        super(context);

    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setTypeface(Typeface typeface) {
        //Log.i(TAG, "Typeface being called");

        try{
            if(typeface.isBold()){
                super.setTypeface(FontHelper.loadFontFromAssets(FontHelper.ROBOTO_REG));
            } else {
                super.setTypeface(FontHelper.loadFontFromAssets(FontHelper.ROBOTO_LIGHT));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
