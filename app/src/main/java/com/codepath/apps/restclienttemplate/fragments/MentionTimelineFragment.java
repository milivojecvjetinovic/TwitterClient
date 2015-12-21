package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.UserProfileActivity;
import com.codepath.apps.restclienttemplate.models.TimelineModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mcvjetinovic on 12/19/15.
 */
public class MentionTimelineFragment extends TweetsListFragment {
    private static final String TIMELINE_HOME = "TIMELINE_MENTIONS";
    private TwitterClient client;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeLineList.clear();
        client = TwitterApp.getRestClient();
        fetchTimeline(1, 0, false);
    }

    @Override
    protected void itemClicked(TimelineModel timelineModel) {
        Intent i = new Intent(getActivity() , UserProfileActivity.class);
        i.putExtra(UserProfileActivity.SCREEN_NAME, timelineModel.getUser().getScreenName());
        startActivity(i);
    }

    //    private void fetchTimeline(int page,int maxId, final boolean endless) {
    public void fetchTimeline(final long sinceId, final long maxId, final boolean sort) {
        client.getMentionsTimeLine(sinceId, maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TIMELINE_HOME, "responseReceived:" + response.toString());
                timeLineList.addAll(TimelineModel.parseResponse(response));
                Log.d(TIMELINE_HOME,"list from model:" + timeLineList.size());

                if(sort){
                    sortTimeline(timeLineList);
                }
                timelineAdapter.notifyDataSetChanged();
                setSinceMaxId(timeLineList);
                saveToDb(timeLineList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TIMELINE_HOME,"Error fetching data from twitter.", throwable);
                Log.e(TIMELINE_HOME,"Error fetching data from twitter:" + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TIMELINE_HOME,"Error fetching data from twitter.", throwable);
            }
        }, getResources());
    }

}