package com.codepath.apps.restclienttemplate.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.UserProfileActivity;
import com.codepath.apps.restclienttemplate.models.SampleModel;
import com.codepath.apps.restclienttemplate.models.TimelineModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mcvjetinovic on 12/11/15.
 */
public class TimelineListAdapter extends ArrayAdapter<TimelineModel> {

    private static final String TIMELINE_ADAPTER = "TIMELINE_ADAPTER";
    private static final String AT="@";

    private static final class ViewHolder {
        public TextView tvUsername;
        public TextView tvTweet;
        public ImageView ivUserProfile;
        public TextView tvtvTweetTime;
        public TextView tvScreenName;
    }

    public TimelineListAdapter(Context context, ArrayList<TimelineModel> timelineList ) {
        super(context,0, timelineList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d( TIMELINE_ADAPTER,"GETTING VIEW FROM ADAPTER:" + position);
        TimelineModel item = getItem(position);
        ViewHolder holder;

        if(convertView == null) {

            //inflate view from xml file
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_details, parent, false);
            holder = new ViewHolder();

            TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            TextView tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);
            TextView tvTweetTime = (TextView) convertView.findViewById(R.id.tvTweetTime);
            ImageView ivUserProfile = (ImageView) convertView.findViewById(R.id.ivUserProfile);

            ivUserProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String screenName = (String)v.getTag(R.id.screen_name_value);
                    Intent i = new Intent(TimelineListAdapter.this.getContext(), UserProfileActivity.class);
                    Log.d(TIMELINE_ADAPTER, "Screen name:" + screenName);
                    i.putExtra(UserProfileActivity.SCREEN_NAME, screenName);
                    TimelineListAdapter.this.getContext().startActivity(i);
                }
            });

            TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);

            holder.tvUsername = tvUsername;
            holder.tvTweet = tvTweet;
            holder.ivUserProfile = ivUserProfile;
            holder.tvtvTweetTime = tvTweetTime;
            holder.tvScreenName = tvScreenName;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTweet.setText(item.getTweet());
        holder.tvUsername.setText(item.getUser().getName());
        holder.tvScreenName.setText(TimelineListAdapter.AT + item.getUser().getScreenName());
        holder.tvtvTweetTime.setText(item.getRelativeTweetTime());
        Log.d(TIMELINE_ADAPTER, "Relative tweet time:" + item.getRelativeTweetTime());
        Picasso.with(getContext()).load(item.getUser().getProfileImage()).into(holder.ivUserProfile);
        holder.ivUserProfile.setTag(R.id.screen_name_value ,item.getUser().getScreenName());
        return convertView;
    }
}
