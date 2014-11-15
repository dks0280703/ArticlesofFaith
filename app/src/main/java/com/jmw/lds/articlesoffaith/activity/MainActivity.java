package com.jmw.lds.articlesoffaith.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.jmw.lds.articlesoffaith.R;
import com.jmw.lds.articlesoffaith.adapter.MainAdapter;
import com.jmw.lds.articlesoffaith.model.Article;
import com.jmw.lds.articlesoffaith.toolbox.FlavorHelper;
import com.jmw.lds.articlesoffaith.toolbox.PixelHelper;
import com.jmw.lds.articlesoffaith.toolbox.StyleHelper;
import com.jmw.lds.articlesoffaith.widget.MyTextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AbsParseDataActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainAdapter mAdapter;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.toolbar_view)
    Toolbar toolbar;


    /**
     * This is where we set up an adapter with the list and assign it to a listview.
     * @param list this list gets feed to the adapter
     */
    @Override
    protected void setUpListView(List<Article> list) {
        mAdapter = new MainAdapter(getApplicationContext(), R.layout.row_article, list);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if(FlavorHelper.isNOTLollipop()){
            StyleHelper.brandGlowEffect(this.getApplicationContext(), getResources().getColor(R.color.primary));
        }

        setUpToolBar(toolbar, null);
        //toolbar.getLayoutParams().height =  PixelHelper.getScreenHeightInPixels(this)/3;




        // Set this because my list is a fixed size and it will imporove performance
        mRecyclerView.setHasFixedSize(true);
        // Setup the RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add animations
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(false);
        // Add decorations
        //mRecyclerView.addItemDecoration(new MyItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setUpToolBar(Toolbar toolbar, String title){

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.START | Gravity.TOP);
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
