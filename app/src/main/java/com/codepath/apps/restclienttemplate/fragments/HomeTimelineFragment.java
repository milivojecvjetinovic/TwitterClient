package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.UserProfileActivity;
import com.codepath.apps.restclienttemplate.models.TimelineModel;
import com.codepath.apps.restclienttemplate.models.TwitterTimelineComparator;
import com.codepath.apps.restclienttemplate.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mcvjetinovic on 12/19/15.
 */
public class HomeTimelineFragment extends TweetsListFragment {
    private static final String TIMELINE_HOME = "TIMELINE_HOME";
    private TwitterClient client;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeLineList.clear();
        client = TwitterApp.getRestClient();
        fetchTimeline(1, 0, false);
//        fetchUserInfo();
    }

//    private void fetchUserInfo() {
//        client.getUserInfo(null, new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                UserModel user = UserModel.createModel(response);
//                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("@"+user.getScreenName());
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                Toast.makeText(HomeTimelineFragment.this.getActivity(), "Cannot fetch user info!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    protected void itemClicked(TimelineModel timelineModel) {
        Intent i = new Intent(getActivity() , UserProfileActivity.class);
        i.putExtra(UserProfileActivity.SCREEN_NAME, timelineModel.getUser().getScreenName());
        startActivity(i);
    }

    //    private void fetchTimeline(int page,int maxId, final boolean endless) {
    public void fetchTimeline(final long sinceId, final long maxId, final boolean sort) {
        client.getHomeTimeline(sinceId, maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Log.d(TIMELINE_HOME, response.toString());
                //append tweet to the top of the array
//               timeLineList = new ArrayList<TimelineModel>();

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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TIMELINE_HOME,"Error fetching data from twitter.", throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TIMELINE_HOME,"Error fetching data from twitter.", throwable);
                Log.e(TIMELINE_HOME,"Error fetching data from twitter:" + errorResponse);
            }
        }, getResources());
    }



}
