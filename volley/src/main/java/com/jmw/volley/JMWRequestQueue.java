package com.jmw.volley;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.Cache;
import com.android.volley.CacheDispatcher;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;


/**
 * Created by dks0280703 on 10/30/14.
 */
public class JMWRequestQueue extends RequestQueue {


    /** the hybrid dispatchers that are smart enough to check for url or local file path*/
    protected JMWDispatcher[] mDSGDispatchers;


    /**
     * Basically the call to the super worker pool for network requests
     * Creates the worker pool. Processing will not begin until {@link #start()} is called.
     *
     * @param cache A Cache to use for persisting responses to disk
     * @param network A Network interface for performing HTTP requests
     * @param threadPoolSize Number of network dispatcher threads to create
     * @param delivery A ResponseDelivery interface for posting responses and errors
     */
    public JMWRequestQueue(Cache cache, Network network, int threadPoolSize,
                           ResponseDelivery delivery) {

        super(cache, network, threadPoolSize, delivery);
        // Add a new dispatcher that will override NetworkDispatcher
        mDSGDispatchers = new JMWDispatcher[threadPoolSize];
    }

    /**
     * Basically the call to the super worker pool for network requests
     * Creates the worker pool. Processing will not begin until {@link #start()} is called.
     *
     * @param cache A Cache to use for persisting responses to disk
     * @param network A Network interface for performing HTTP requests
     * @param threadPoolSize Number of network dispatcher threads to create
     */
    public JMWRequestQueue(Cache cache, Network network, int threadPoolSize) {
        this(cache, network, threadPoolSize,
                new ExecutorDelivery(new Handler(Looper.getMainLooper())));
    }

    /**
     * Basically the call to the super worker pool for network requests
     * Creates the worker pool. Processing will not begin until {@link #start()} is called.
     *
     * @param cache A Cache to use for persisting responses to disk
     * @param network A Network interface for performing HTTP requests
     */
    public JMWRequestQueue(Cache cache, Network network) {
        this(cache, network, DEFAULT_NETWORK_THREAD_POOL_SIZE);
    }


    /**
     * Starts the dispatchers in this queue.
     */
    @Override
    public void start() {
        stop();  // Make sure any currently running dispatchers are stopped.
        // Create the cache dispatcher and start it.
        mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue, mCache, mDelivery);
        mCacheDispatcher.start();

        // Create network dispatchers (and corresponding threads) up to the pool size.
        for (int i = 0; i < mDSGDispatchers.length; i++) {
            JMWDispatcher JMWDispatcher = new JMWDispatcher(mNetworkQueue, mNetwork,
                    mCache, mDelivery);
            mDSGDispatchers[i] = JMWDispatcher;
            JMWDispatcher.start();
        }
    }

    /**
     * Stops the cache and network dispatchers.
     */
    @Override
    public void stop() {

        super.stop();

        for (int i = 0; i < mDSGDispatchers.length; i++) {
            if (mDSGDispatchers[i] != null) {
                mDSGDispatchers[i].quit();
            }
        }
    }







}
