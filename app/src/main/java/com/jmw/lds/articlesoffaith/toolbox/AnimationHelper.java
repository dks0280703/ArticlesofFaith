package com.jmw.lds.articlesoffaith.toolbox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by justin on 11/19/14.
 */
public enum AnimationHelper {
    INSTANCE;

    private OnFinishListener onFinishListener;

    public static final int SHORT_DURATION = 250;
    public static final int MED_DURATION = 500;
    public static final int LONG_DURATION = 1000;

    public static final int SHORT_DELAY = 200;

    public interface OnFinishListener{

        abstract public void onFadeOut();
        abstract public void onFadeIn();
    }

    public void setOnFinishListener(OnFinishListener onFinishListener){
        this.onFinishListener = onFinishListener;
    }


    private void onFadeOut(){
        if(onFinishListener!=null){
            onFinishListener.onFadeOut();
        }
    }

    private void onFadeIn(){
        if(onFinishListener!=null){
            onFinishListener.onFadeIn();
        }
    }

    AnimationHelper(){}

    public void fadeOutImageAndFinish(ImageView imageView, int duration){
        if(imageView == null)
            return;

        if(duration ==0)
            duration = MED_DURATION;

        final View view = imageView;
        view.animate().alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                        try{
                            ViewGroup parent = (ViewGroup) view.getParent();
                            parent.removeView(view);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        onFadeOut();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                    }
                });
    }

    public void fadeIn(View view, int duration){
        if(view == null)
            return;
        if(duration == 0)
            duration = MED_DURATION;

        final int time = duration;
        final View v = view;
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(time).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                onFadeIn();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });

    }

    public void fadeInImageWithDelay(ImageView imageView, int duration, int delay){

        if(imageView == null)
            return;

        if(duration ==0)
            duration = MED_DURATION;


        final int time = duration;
        final View view = imageView;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setAlpha(0f);
                view.setVisibility(View.VISIBLE);
                view.animate().alpha(1f).setDuration(time).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        onFadeIn();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                });

            }
        }, delay);






    }
}
