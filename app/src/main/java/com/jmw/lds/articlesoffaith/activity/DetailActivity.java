package com.jmw.lds.articlesoffaith.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jmw.lds.articlesoffaith.AppController;
import com.jmw.lds.articlesoffaith.R;
import com.jmw.lds.articlesoffaith.model.Article;
import com.jmw.lds.articlesoffaith.toolbox.AnimationHelper;
import com.jmw.lds.articlesoffaith.toolbox.FlavorHelper;
import com.jmw.lds.articlesoffaith.toolbox.FontHelper;
import com.jmw.lds.articlesoffaith.toolbox.PixelHelper;
import com.jmw.volley.toolbox.JMWImageLoader;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by justin on 11/15/14.
 */
public class DetailActivity extends AbsToolbarActivity implements AnimationHelper.OnFinishListener{

    private static final String TAG = DetailActivity.class.getSimpleName();

    private AnimationHelper animationHelper;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.toolbar_view)
    Toolbar toolbar;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.root_layout)
    RelativeLayout relativeLayout;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.banner)
    ImageView banner;

    // This is temporary to server as just getting data
    protected void setUpArticle(Article article) {

        setUpToolBar(toolbar, article.getTitle());
        toolbar.getLayoutParams().height =  PixelHelper.getScreenHeightInPixels(this)/3;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Let's resize the image
        int screenWidth = PixelHelper.getScreenWidthInPixels(this);
        int screenHeight = PixelHelper.getScreenHeightInPixels(this);
        int margin = (int)PixelHelper.getPixelsFromDips(this, 32.0f);

        // Let's make a 4:3 size image with 16dp margin on both side
        int imageWidth = screenWidth - margin;
        int imageHeight = (imageWidth*3)/4;

        banner.getLayoutParams().width = imageWidth;
        banner.getLayoutParams().height = imageHeight;
        setBanner(this, article, imageWidth, imageHeight);

        // Define margins
        int space = (int)PixelHelper.getPixelsFromDips(this, 16.0f);
        int topMargin = imageHeight/2;

        if(screenHeight < 1920)
            topMargin += space;


        // Now let's create the body
        TextView body = new TextView(this);
        body.setId(generateId());
        body.setText(article.getBody());
        body.setTextColor(getResources().getColor(R.color.abc_primary_text_material_light));
        body.setTextSize(20.0f);
        FontHelper.applyFontToTextView(body, FontHelper.ROBOTO_LIGHT);
        // Now setup the layout params
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(space, topMargin, space, 0); // Left, Top, Right, Bottom;
        params.addRule(RelativeLayout.BELOW, toolbar.getId());
        relativeLayout.addView(body, params);


        // Now let's create the Sub Title
        TextView subTitle = new TextView(this);
        FontHelper.applyFontToTextView(subTitle, FontHelper.ROBOTO_LIGHT);
        subTitle.setId(generateId());
        String key = getString(R.string.key_words);
        String words = article.getSubTitle().toUpperCase();
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(" ");
        sb.append(words);
        subTitle.setText(sb.toString());
        subTitle.setTextColor(getResources().getColor(R.color.abc_secondary_text_material_light));
        subTitle.setTextSize(12.0f);
        // Now setup the layout params
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(space, space, space, space); // Left, Top, Right, Bottom;
        params.addRule(RelativeLayout.BELOW, body.getId());
        relativeLayout.addView(subTitle, params);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        animationHelper = AnimationHelper.INSTANCE;
        animationHelper.setOnFinishListener(this);

        banner.setVisibility(View.INVISIBLE);

        try {
            Article article = (Article) getIntent().getSerializableExtra(MainActivity.EXTRA_ARTICLE);
            setUpArticle(article);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if(id == android.R.id.home){
            animationHelper.fadeOutImageAndFinish(banner, 0);
        }

        return super.onOptionsItemSelected(item);
    }


    private void setBanner(Context context, Article article, int width, int height){

        JMWImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(context.getApplicationContext(), article.getImage(), new JMWImageLoader.ImageListener() {
            @Override
            public void onResponse(JMWImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    banner.setImageBitmap(response.getBitmap());
                    setShadow(banner);

                    animationHelper.fadeInImageWithDelay(banner, 0, 200);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "We have an error " + error.getMessage());
            }
        }, width, height);

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setShadow(ImageView imageView){
        if(FlavorHelper.hasLollipop()) {
            final ImageView image = imageView;
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    Rect rect = image.getDrawable().getBounds();
                    outline.setRect(rect);

                }
            };

            float depth = PixelHelper.getPixelsFromDips(AppController.getInstanceContext(), 8);
            image.setOutlineProvider(viewOutlineProvider);
            image.setElevation(depth);
        }
    }


    private final AtomicInteger mNextGeneratedInteger = new AtomicInteger(1);

    public int generateId(){

        for(;;){
            final int result = mNextGeneratedInteger.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newvalue = result+1;
            if(newvalue> 0x00FFFFFF) newvalue = 1; // Roll over to 1, not 0.
            if (mNextGeneratedInteger.compareAndSet(result, newvalue)) {
                return result;
            }
        }
    }



    @Override
    public void onBackPressed(){
        animationHelper.fadeOutImageAndFinish(banner, 0);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    @Override
    public void onFadeOut() {
        DetailActivity.this.finish();
    }

    @Override
    public void onFadeIn() {

    }
}
