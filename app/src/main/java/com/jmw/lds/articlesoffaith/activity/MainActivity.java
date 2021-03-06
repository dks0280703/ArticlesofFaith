package com.jmw.lds.articlesoffaith.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.jmw.lds.articlesoffaith.MyAppWidget;
import com.jmw.lds.articlesoffaith.MyAppWidgetConfigureActivity;
import com.jmw.lds.articlesoffaith.R;
import com.jmw.lds.articlesoffaith.adapter.MainAdapter;
import com.jmw.lds.articlesoffaith.model.Article;
import com.jmw.lds.articlesoffaith.toolbox.FlavorHelper;
import com.jmw.lds.articlesoffaith.toolbox.StyleHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.v7.widget.RecyclerView.OnScrollListener;


public class MainActivity extends AbsParseDataActivity implements MainAdapter.OnItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_ARTICLE = "article";

    private MainAdapter mAdapter;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.toolbar_view)
    Toolbar toolbar;

    List<Article> mList;


    /**
     * This is where we set up an adapter with the list and assign it to a listview.
     * @param list this list gets feed to the adapter
     */
    @Override
    protected void setUpListView(List<Article> list) {
        mAdapter = new MainAdapter(getApplicationContext(), R.layout.row_article, list);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mList = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.i(TAG, "OnResume Called");

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if(FlavorHelper.isNOTLollipop()){
            StyleHelper.brandGlowEffect(this.getApplicationContext(), getResources().getColor(R.color.primary));
        }

        setUpToolBar(toolbar, null);


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
    protected void onResume(){
        super.onResume();
        if(getIntent()!=null && getIntent().getExtras()!=null){

            Article article = (Article) getIntent().getExtras().getSerializable(EXTRA_ARTICLE);
            if(article instanceof Article){

                Log.d(TAG, "onResume is going to  article "+article.getId()+" title "+article.getTitle());

                int flag = article.getId()-1;
                mRecyclerView.scrollToPosition(flag);
                onItemCLick(null, flag);
                Log.i(TAG, "Flag = "+flag);
                getIntent().removeExtra(EXTRA_ARTICLE);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemCLick(View view, final int position) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_ARTICLE, mList.get(position));
                Bundle translageBundle = ActivityOptions.makeCustomAnimation(MainActivity.this, R.anim.slide_in_left, R.anim.slide_out_left).toBundle();
                //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, "painting");
                startActivity(intent, translageBundle);
            }
        }, 200);



    }


}
