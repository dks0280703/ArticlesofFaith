package com.jmw.lds.articlesoffaith.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.jmw.lds.articlesoffaith.AppController;
import com.jmw.lds.articlesoffaith.R;
import com.jmw.lds.articlesoffaith.model.Article;
import com.jmw.lds.articlesoffaith.toolbox.FlavorHelper;
import com.jmw.lds.articlesoffaith.toolbox.FontHelper;
import com.jmw.lds.articlesoffaith.toolbox.PixelHelper;
import com.jmw.lds.articlesoffaith.widget.MyTextView;
import com.jmw.volley.artbox.BitmapHelper;
import com.jmw.volley.toolbox.JMWImageLoader;

import java.util.List;

/**
 * Created by justin on 11/8/14.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private static final String TAG = MainAdapter.class.getSimpleName();
    private List<Article> mList;
    private int mRowLayout;
    private Context mContext;
    int thumbWidth;
    int thumbHeight;
    OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        public void onItemCLick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public MainAdapter(Context context, @LayoutRes int rowLayout, List<Article> list){
        this.mContext = context;
        this.mRowLayout = rowLayout;
        this.mList = list;

        int w = (int) PixelHelper.getPixelsFromDips(context.getApplicationContext(), 72.0f);
        thumbWidth = thumbHeight = w;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Article article = mList.get(position);
        viewHolder.body.setText(article.getBody());
        //FontHelper.applyFontToTextView(viewHolder.body, FontHelper.ROBOTO_REG);

        JMWImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(mContext.getApplicationContext(), article.getThumb(), new JMWImageLoader.ImageListener() {
            @Override
            public void onResponse(JMWImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    final Bitmap bitmap = BitmapHelper.getCroppedBitmap(response.getBitmap());
                    viewHolder.thumb.setImageBitmap(bitmap);
                    setShadow(viewHolder);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "We have an error "+error.getMessage());
            }
        }, thumbWidth, thumbHeight);

        // Causes weird behavior with shadows
        //imageLoader.get(mContext, article.getThumb(), JMWImageLoader.getImageListener(viewHolder.thumb, R.drawable.thumb_default, R.drawable.thumb_default));

        // Hide divider if last row
        if(position == getItemCount()-1){
            viewHolder.divider.setVisibility(View.GONE);
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setShadow(ViewHolder viewHolder){
        if(FlavorHelper.hasLollipop()) {
            final ViewHolder vh = viewHolder;
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    Rect rect = vh.thumb.getDrawable().getBounds();
                    outline.setOval(rect);
                }
            };

            float depth = PixelHelper.getPixelsFromDips(AppController.getInstanceContext(), 2);
            vh.thumb.setOutlineProvider(viewOutlineProvider);
            vh.thumb.setElevation(depth);
            //vh.thumb.setTranslationZ(depth*2);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MyTextView body;
        public ImageView thumb;
        public View divider;



        public ViewHolder(View itemView) {
            super(itemView);

            body = (MyTextView) itemView.findViewById(R.id.body);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            divider = itemView.findViewById(R.id.divider);

            // Setup Click
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null){
                onItemClickListener.onItemCLick(v, getPosition());
            }
        }
    }
}
