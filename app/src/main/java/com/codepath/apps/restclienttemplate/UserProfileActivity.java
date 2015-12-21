package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.UserHeaderFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {

    private static final String USER_PROFILE = "USER_PROFILE_ACTIVITY";
    private TwitterClient client;
    private UserModel user;
    public static final String SCREEN_NAME ="screen_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        client = TwitterApp.getRestClient();

        String screenName = getIntent().getStringExtra(UserProfileActivity.SCREEN_NAME);
        if(screenName != null) {
            getSupportActionBar().setTitle("@"+screenName);
        }

        //get screen name passed in from calling activity (Timeline activity in this case)
        if(savedInstanceState == null) {
            //create user timeline fragment and pass data into the fragment new instance method
            //display fragment within an acitivty dynamically - need container for it
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            UserHeaderFragment fragmentUserHeader = UserHeaderFragment.newInstance(screenName);
            ft.replace(R.id.frUserHeader, fragmentUserHeader);

            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            ft.replace(R.id.frmContainer, fragmentUserTimeline);

            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twitter_log_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.mnuProfile) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
