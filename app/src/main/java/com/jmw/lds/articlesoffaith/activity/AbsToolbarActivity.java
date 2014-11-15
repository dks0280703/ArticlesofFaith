package com.jmw.lds.articlesoffaith.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.jmw.lds.articlesoffaith.R;
import com.jmw.lds.articlesoffaith.widget.MyTextView;

/**
 * Created by justin on 11/15/14.
 */
public abstract class AbsToolbarActivity extends ActionBarActivity{

    private static final String TAG = AbsToolbarActivity.class.getSimpleName();

    public void setUpToolBar(Toolbar toolbar, String title){

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.START | Gravity.TOP);
        View customNav = LayoutInflater.from(this).inflate(R.layout.toolbar_custom_layout, null); // layout which contains your button.
        actionBar.setCustomView(customNav, lp);
        actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        if(title != null && !TextUtils.equals(title, new String())) {
            MyTextView textView = (MyTextView) findViewById(R.id.custom_action_bar_title);
            textView.setText(title);
            Log.i(TAG, "Here we are");
        }else{
            Log.i(TAG, "It's null");
        }


    }

}
