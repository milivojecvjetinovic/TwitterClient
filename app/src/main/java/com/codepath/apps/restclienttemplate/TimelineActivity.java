package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.adapter.TimelineListAdapter;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.TimelineModel;
import com.codepath.apps.restclienttemplate.models.TwitterTimelineComparator;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    private static final String TIMELINE = "TIMELINE_ACTIVITY";
    private static final String TIMELINE_ACTIVITY = "TIMELINE_ACTIVITY";
    private TwitterClient client;
    private ArrayList<TimelineModel> timeLineList;
    private TimelineListAdapter timelineAdapter;
    private ListView lvTweets;
    public long sinceId;
    public long maxId;

    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //get swipe container
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TIMELINE_ACTIVITY, "Do SWIPE_REFRESH with params: sinceId:" + sinceId);
                fetchTimeline(sinceId, 0, true);
            }
        });


        timeLineList = new ArrayList<>();
        timelineAdapter = new TimelineListAdapter(this, timeLineList);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setAdapter(timelineAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d(TIMELINE_ACTIVITY,"activate endless scoreller: sinceId:" + sinceId + " maxId:" + maxId );
                fetchTimeline(sinceId, maxId, false);
//                return true;
                return true;
            }
        });

        client = TwitterApp.getRestClient();
//        fetchTimeline(0, 0, false);

        timelineAdapter.clear();
        timeLineList.clear();
        fetchTimeline(1, 0, false);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


//    private void fetchTimeline(int page,int maxId, final boolean endless) {
    private void fetchTimeline(final long sinceId, final long maxId, final boolean sort) {
        client.getHomeTimeline(sinceId, maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                if(!endless) {
//                    timelineAdapter.clear();
//                    timeLineList.clear();
//                }
                Log.d(TIMELINE, response.toString());
                //append tweet to the top of the array
                timeLineList.addAll(TimelineModel.parseResponse(response));
                if(sort){
                    sortTimeline();
                }

                setSinceMaxId();
                Log.d(TIMELINE,"list from model:" + timeLineList.size());
                swipeContainer.setRefreshing(false);
                timelineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TIMELINE,"Error fetching data from twitter.", throwable);
            }
        }, getResources());
    }

    private void setSinceMaxId() {
        this.maxId = timeLineList.get(timeLineList.size() -1).getTweetId() - 1;
        this.sinceId = timeLineList.get(0).getTweetId();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twitter_log_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuCompose:
                openComposeAtivity();
                return true;
            default:
                //handle other cases here
               return super.onOptionsItemSelected(item);
        }
    }

    private void openComposeAtivity() {
        Intent i = new Intent(this, ComposeTweetActivity.class);
        startActivityForResult(i,ComposeTweetActivity.COMPOSE_TWEET_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ComposeTweetActivity.COMPOSE_TWEET_ACTIVITY) {
            TimelineModel tweet = (TimelineModel) data.getSerializableExtra(ComposeTweetActivity.NEW_TWEET);
            if(tweet != null) {
                timeLineList.add(tweet);
                sortTimeline();
                setSinceMaxId();
                timelineAdapter.notifyDataSetChanged();
            }
        }
    }

    private void sortTimeline() {
        Comparator<? super TimelineModel> tweeterComparator = new TwitterTimelineComparator();
        Collections.sort(timeLineList, tweeterComparator);
    }
}
