package com.jmw.lds.articlesoffaith.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.jmw.lds.articlesoffaith.AppController;
import com.jmw.lds.articlesoffaith.R;
import com.jmw.lds.articlesoffaith.model.Article;
import com.jmw.lds.articlesoffaith.toolbox.PixelHelper;
import com.jmw.lds.articlesoffaith.widget.MyTextView;
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

        JMWImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(mContext.getApplicationContext(), article.getThumb(), new JMWImageLoader.ImageListener() {
            @Override
            public void onResponse(JMWImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    viewHolder.thumb.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "We have an error "+error.getMessage());
            }
        }, thumbWidth, thumbHeight);


        // Hide divider if last row
        if(position == getItemCount()-1){
            viewHolder.divider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public MyTextView body;
        public ImageView thumb;
        public View divider;

        public ViewHolder(View itemView) {
            super(itemView);

            body = (MyTextView) itemView.findViewById(R.id.body);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
