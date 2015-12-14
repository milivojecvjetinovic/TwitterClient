package com.codepath.apps.restclienttemplate;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.StatusModel;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "sOGzCL5cENzeWoRllDZdimuB1";       // Change this
	public static final String REST_CONSUMER_SECRET = "4LWdlU6X57cL2BkXsbtDGyOql0vPYTTkjt2Xu9UIVYDxnPhWpd"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpresttweet"; // Change this (here and in manifest)
    private static final String TWITTER_CLIENT = "TWITTER_CLIENT";

    public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    /**
     * TWITER API
     * BASE URL:
     *
     * https://api.twitter.com/1.1/
     *
     *Get statuses for the user:
     *GET  statuses/user_timeline.json
     *      count = 25
     *      since = 1
     *
     */

    public void getHomeTimeline(long sinceId,long maxId, AsyncHttpResponseHandler handler, Resources resources) {
//        if(false) {
//            handler.onSuccess(200, null, readResource(resources.openRawResource(R.raw.tweets)) );
//        } else {
            String apiUrl = getApiUrl("statuses/user_timeline.json");
            RequestParams params = new RequestParams();
            params.put("count", 15);
            if(maxId > 0) {
                params.put("max_id", maxId);
            } else {
                params.put("since_id", sinceId);
            }
        Log.d(TWITTER_CLIENT, "Parameters passed in:" + params.toString());

            client.get(apiUrl, params, handler);
//        }
    }


    public void postStatus(StatusModel status, AsyncHttpResponseHandler handler){
        if(status != null && status.getStatus() != null) {
            RequestParams params = new RequestParams();
            params.put("status", status.getStatus());
            String apiUrl = getApiUrl("statuses/update.json");
            client.post(apiUrl, params, handler);
        }
    }

    private static byte[] readResource(InputStream inputStream) {
        Log.d(TWITTER_CLIENT,"READING RESOURCE");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}