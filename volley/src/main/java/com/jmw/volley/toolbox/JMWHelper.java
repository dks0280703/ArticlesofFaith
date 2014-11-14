package com.jmw.volley.toolbox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by justin on 11/14/14.
 */
public enum JMWHelper {
    INSTANCE;

    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    private Pattern p = Pattern.compile(URL_REGEX);
    private Matcher mMatcher = p.matcher("");

    public boolean isURL(String value) {

        mMatcher.reset(value);
        return mMatcher.find()? true:false;
    }
}
