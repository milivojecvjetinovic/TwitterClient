package com.codepath.apps.restclienttemplate.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserHeaderFragment extends Fragment {


    private static final String USER_HEADER = "USER_HEADER";
    public static final String SCREEN_NAME = "screen_name";
    private TextView tvName;
    private TextView tvTagLine;
    private TextView tvFollowers;
    private TextView tvFollwing;
    private ImageView ivProfileImage;
    private TwitterClient client;
    private TextView tvTweetCount;
    private UserModel user;

    public static UserHeaderFragment newInstance(String screenName) {
        UserHeaderFragment userHeaderFragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putSerializable(SCREEN_NAME, screenName);
        userHeaderFragment.setArguments(args);
        return userHeaderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_user_header, container, false);
        client = TwitterApp.getRestClient();

        tvName = (TextView) v.findViewById(R.id.tvFullName);
        tvTagLine = (TextView) v.findViewById(R.id.tvTagLine);
        tvFollowers = (TextView) v.findViewById(R.id.tvFollowers);
        tvFollwing = (TextView) v.findViewById(R.id.tvUserFollowing);
        ivProfileImage = (ImageView) v.findViewById(R.id.ivUserProfileImage);
        tvTweetCount = (TextView) v.findViewById(R.id.tvTweetCount);
        fetchData(getArguments().getString(UserHeaderFragment.SCREEN_NAME));
        return v;
    }


    public void fetchData(String screenName) {
        client.getUserInfo(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = UserModel.createModel(response);
                populateUserData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(USER_HEADER, "error getting user info:" , throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(USER_HEADER,"Error fetching user from twitter.", throwable);
                Log.e(USER_HEADER,"Error fetching user from twitter:" + errorResponse);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void populateUserData() {
        if(user == null) return;
        Log.d(USER_HEADER, "User:" + user);
        tvName.setText(user.getFullName());
        tvTagLine.setText(user.getTagLine());
        tvFollowers.setText(String.valueOf(user.getFollowersCount()));
        tvFollwing.setText(String.valueOf(user.getFollowingCount()));
        tvTweetCount.setText(String.valueOf(user.getTweetsCount()));
        if(user.getProfileImage() != null) {
            Picasso.with(getContext()).load(user.getProfileImage()).into(ivProfileImage);
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("@"+user.getScreenName());
    }


}
