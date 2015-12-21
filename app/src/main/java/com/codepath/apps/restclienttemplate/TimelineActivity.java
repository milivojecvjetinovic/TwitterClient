package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.adapter.SmartFragmentStatePagerAdapter;
import com.codepath.apps.restclienttemplate.adapter.TimelineListAdapter;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.TimelineModel;
import com.codepath.apps.restclienttemplate.models.TwitterTimelineComparator;
import com.codepath.apps.restclienttemplate.models.UserModel;
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
    private ViewPager vpPager;
    private TweetsPageAdapter tweetsPageAdapter;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            setContentView(R.layout.activity_timeline);
            //get the viewpager
            vpPager = (ViewPager) findViewById(R.id.viewpager);

            //set the view pager adapter for the pager
            tweetsPageAdapter = new TweetsPageAdapter(getSupportFragmentManager());
            vpPager.setAdapter(tweetsPageAdapter);
            //find the pager sliding tabs
            PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            tabStrip.setViewPager(vpPager);

            //fetching user info
            client = TwitterApp.getRestClient();
            fetchUserInfo();
        }
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
            case R.id.mnuProfile:
                viewUserProfile();
                return true;
            default:
                //handle other cases here
               return super.onOptionsItemSelected(item);
        }
    }

    //show profile view
    private void viewUserProfile() {
        //Launch profile view
        Intent i = new Intent(this , UserProfileActivity.class);
        startActivity(i);
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

                TweetsListFragment fragment = (TweetsListFragment) tweetsPageAdapter.getRegisteredFragment(vpPager.getCurrentItem());
                fragment.add(tweet);

//                fragmentTweetsList.add(tweet);

//                sortTimeline();
//                setSinceMaxId();
//                timelineAdapter.notifyDataSetChanged();
//                //save to db
//                tweet.save();
            }
        }
    }

    //return the order of the fragments in the view pager
    public class TweetsPageAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = { "Home", "Mentions"};

        //adapter gets the manager insert or remove fragment from activity
        public TweetsPageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        //the order of the creation fragments within the page
        @Override
        public Fragment getItem(int position) {

            if(position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //how many tabs
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }


    private void fetchUserInfo() {
        client.getUserInfo(null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                UserModel user = UserModel.createModel(response);
                getSupportActionBar().setTitle("@"+user.getScreenName());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(TimelineActivity.this , "Cannot fetch user info!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
