package com.jmw.lds.articlesoffaith.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.jmw.lds.articlesoffaith.R;
import com.jmw.lds.articlesoffaith.toolbox.FlavorHelper;
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

        if(title != null && !TextUtils.equals(title, new String())) {
            MyTextView textView = (MyTextView) findViewById(R.id.custom_action_bar_title);
            textView.setText(title);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        //setupTransitions();
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public void setupTransitions(){
//        if(FlavorHelper.hasLollipop()){
//
//            // set transitions
//            getWindow().setEnterTransition(new Explode());
//            getWindow().setExitTransition(new Explode());
//        }
//    }

}
