package com.jmw.lds.articlesoffaith.toolbox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

/**
 * Created by justin on 11/8/14.
 */
public class GsonHelper {

    /**
     * Return a Gson Object
     * If running proguard, exclude any fields without expose.
     * @return
     */
    public Gson getGson(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").excludeFieldsWithoutExposeAnnotation().create();
        return gson;
    }

    /**
     * Return a JsonReader with lenient set to true
     * @param response the string to read from
     * @return
     */
    public JsonReader getJsonReader(String response){
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.setLenient(true);
        return reader;
    }

}
