package com.jmw.lds.articlesoffaith.toolbox;

import android.util.Log;

import com.jmw.lds.articlesoffaith.R;

import java.lang.reflect.Field;

/**
 * Created by justin on 11/20/14.
 */
public class ResourceHelper {

    public static final String TAG = ResourceHelper.class.getSimpleName();

    /**
     * Get Resource from a String name
     * Example: getResDrawableId("icon");
     * @param variableName the string name for the id R.drawable.EXAMPLE
     * @return
     */
    public static int getResDrawableId(String variableName) {

        try{
            Class res = R.drawable.class;
            Field field = res.getField(variableName);
            int drawableId = field.getInt(null);
            return drawableId;
        }catch (Exception e){
            Log.e(TAG, "Failure to get drawable id.", e);
        }

        return 0;
    }
}
