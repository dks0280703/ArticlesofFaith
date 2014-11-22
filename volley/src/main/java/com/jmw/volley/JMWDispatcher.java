package com.jmw.volley;

import android.os.Process;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkDispatcher;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.jmw.volley.toolbox.JMWImageRequest;

import java.util.concurrent.BlockingQueue;

/**
 * Created by dks0280703 on 10/30/14.
 */
public class JMWDispatcher extends NetworkDispatcher {

    private static final String TAG = JMWDispatcher.class.getSimpleName();


    /**
     * Creates a new network dispatcher thread.  You must call {@link #start()}
     * in order to begin processing.
     *
     * @param queue    Queue of incoming requests for triage
     * @param network  Network interface to use for performing requests
     * @param cache    Cache interface to use for writing responses to cache
     * @param delivery Delivery interface to use for posting responses
     */
    public JMWDispatcher(BlockingQueue<Request<?>> queue, Network network, Cache cache, ResponseDelivery delivery) {
        super(queue, network, cache, delivery);
    }


    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (true) {
            Request<?> request;
            try {
                // Take a request from the queue.
                request = mQueue.take();
            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (mQuit) {
                    return;
                }
                continue;
            }

            try {
                request.addMarker("network-queue-take");

                // If the request was cancelled already, do not perform the
                // network request.
                if (request.isCanceled()) {
                    request.finish("network-discard-cancelled");
                    continue;
                }

                addTrafficStatsTag(request);

                // FIND OUT FROM THE REQUEST IF IT IS LOCAL OR NOT
                // Easy way to do this is find out if it's a instance of the Local Request
                Response<?> response;

                if (request instanceof JMWImageRequest) {
                    //Log.d(TAG, "Request is a LocalImageRequest");
                    request.addMarker("local-complete");

                    response = ((JMWImageRequest) request).parseFileToBitmap();


                } else {

                    // Perform the network request.
                    NetworkResponse networkResponse = mNetwork.performRequest(request);
                    request.addMarker("network-http-complete");

                    // If the server returned 304 AND we delivered a response already,
                    // we're done -- don't deliver a second identical response.
                    if (networkResponse.notModified && request.hasHadResponseDelivered()) {
                        request.finish("not-modified");
                        continue;
                    }

                    // Parse the response here on the worker thread.
                    response = request.parseNetworkResponse(networkResponse);
                    request.addMarker("network-parse-complete");
                }


                // TODO Uncomment this when we are done
                // Write to cache if applicable.
//                if (request.shouldCache() && response.cacheEntry != null) {
//                    //Log.i(TAG, "SHould Cache = "+request.shouldCache());
//                    //Log.i(TAG, "Key = "+request.getCacheKey());
//
//
//                    mCache.put(request.getCacheKey(), response.cacheEntry);
//                    request.addMarker("network-cache-written");
//                }

                // Post the response back.
                request.markDelivered();
                mDelivery.postResponse(request, response);
            } catch (VolleyError volleyError) {
                parseAndDeliverNetworkError(request, volleyError);
            } catch (Exception e) {
                VolleyLog.e(e, "Unhandled exception %s", e.toString());
                mDelivery.postError(request, new VolleyError(e));
            }
        }
    }

}
