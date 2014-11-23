package com.jmw.lds.articlesoffaith.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jmw.lds.articlesoffaith.model.Article;
import com.jmw.lds.articlesoffaith.toolbox.GsonHelper;
import com.jmw.lds.articlesoffaith.toolbox.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by justin on 11/8/14.
 */
abstract public class AbsParseDataActivity extends AbsToolbarActivity {

    /**
     * Constants
     */
    private static final String DATA_ARTICLES_ARRAY_NAME = "articles";

    /**
     * Tag
     */
    private static final String TAG = AbsParseDataActivity.class.getSimpleName();

    /**
     * Lock for making sure this only runs once at a time
     */
    private ReentrantLock mlock = new ReentrantLock();

    /**
     * Thread for parsing JSON file to objects
     */
    private Thread mThread;

    /**
     * Message Object used with the Handler to pass the list back to the main thread
     */
    private class MessageObject {
        private List<Article> list = new ArrayList<>();

        public List<Article> getList() {
            return list;
        }
        public MessageObject(List<Article> list){
            this.list = list;
        }
    }

    /**
     * Handler for talking back on the main thread once ParseDataRunnable is done
     */
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message message){
            MessageObject messageObject = (MessageObject) message.obj;
            List<Article> list = messageObject.getList();
            if(!list.isEmpty()){
                setUpListView(list);
            }else{
                Log.w(TAG, "We have an empty list. why?!?");
            }
        }
    };

    /**
     * This method starts the thread to parse data
     */
    protected void parseData() {
        mThread = new Thread(new ParseDataRunnable());
        mThread.setName("ParseDataThread");
        mThread.start();
    }


    /**
     * This will attempt to interrupt the thread if it's running
     */
    protected synchronized void stopThread() {
        if (mThread != null) {
            Thread dummy = mThread;
            mThread = null;
            dummy.interrupt();
        }
    }

    /**
     * This runnable is responsible for creating a list of articles from json
     */
    private class ParseDataRunnable implements Runnable {

        @Override
        public void run() {
            // About to do something important so lock
            mlock.lock();

            // Do something
            try {
                List<Article> articleList = getArticles();
                // Send message now
               sendMessage(articleList);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Now done so unlock
            mlock.unlock();
        }

        private List<Article> getArticles() throws JSONException, IOException {

            List<Article> list = new ArrayList<>();

            GsonHelper gsonHelper = new GsonHelper();
            Gson gson = gsonHelper.getGson();
            String data = loadData(LocaleHelper.getData());

            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray(DATA_ARTICLES_ARRAY_NAME);
            for(int i=0; i< array.length(); i++){
                JSONObject jo = array.getJSONObject(i);
                JsonReader reader = gsonHelper.getJsonReader(jo.toString());
                Article article = gson.fromJson(reader, Article.class);
                list.add(article);
            }

            return list;
        }

        private String loadData(String fileName) throws IOException {
            String data = null;

            InputStream is = getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            data = sb.toString();
            return data;
        }

        private void sendMessage(List<Article> list){
            Message message = mHandler.obtainMessage();
            message.obj = new MessageObject(list);
            mHandler.sendMessage(message);
        }
    }


    /**
     * This is the method that is called from the handler to go ahead and populate the list view
     * @param list
     */
    abstract protected void setUpListView(List<Article> list);


    /* Methods for Activity */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Resources res = this.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale("ja");
        res.updateConfiguration(conf, dm);

        LocaleHelper.saveLanguagePref("ja");

        parseData();
    }

    @Override
    protected void onResume(){
        super.onResume();
        LocaleHelper.getData();
    }

    @Override
    protected void onPause(){
        super.onPause();
        stopThread();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


}
