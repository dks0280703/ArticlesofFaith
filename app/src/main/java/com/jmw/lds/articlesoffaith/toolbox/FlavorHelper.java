package com.jmw.lds.articlesoffaith.toolbox;

import android.os.Build;

/**
 * Created by justin on 11/11/14.
 */
public class FlavorHelper {

    private FlavorHelper(){};

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasKitKat(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasJellyBean(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isNOTLollipop(){
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    }
}
