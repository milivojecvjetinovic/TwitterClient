package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mcvjetinovic on 12/12/15.
 */
public class TimelineModel implements Serializable {
    private static final String TIMELINE_MODEL = "TIMELINE_MODEL";
//    private String userName;
//    private String name;
    private String tweet;
    private String tweetTime;
//    private String profileImage;
    private long tweetId;
    private UserModel user;
    private String relativeTweetTime;

    public TimelineModel() {

    }

//    public TimelineModel(JSONObject response) {
//        try {
////            SimpleDateFormat f = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH);
////            f.setLenient(false);
////            Date created_at = f.parse(response.getString("created_at"));
////
//
//            this.tweetId = response.getLong("id");
//            this.tweetTime = response.getString("created_at");
//            this.tweet = response.getString("text");
//            this.relativeTweetTime = TwitterUtil.getRelativeTimeAgo(response.getString("created_at"));
//
//            JSONObject userJson = response.getJSONObject("user");
//            UserModel user = UserModel.parseJson(userJson);
//            this.user = user;
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(TIMELINE_MODEL, "Error from model:" + e.getMessage(), e);
//        }
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//    }

    public String getRelativeTweetTime() {
        return relativeTweetTime;
    }

    public UserModel getUser() {
        return user;
    }

    public String getTweet() {
        return tweet;
    }

    public String getTweetTime() {
        return tweetTime;
    }

    public void setTweetTime(String tweetTime) {
        this.tweetTime = tweetTime;
    }


    public long getTweetId() {
        return tweetId;
    }


    public static TimelineModel createModel(JSONObject response) {
        try {
            TimelineModel model = new TimelineModel();
            model.tweetId = response.getLong("id");
            model.tweetTime = response.getString("created_at");
            model.tweet = response.getString("text");
            model.relativeTweetTime = TwitterUtil.getRelativeTimeAgo(response.getString("created_at"));

            JSONObject userJson = response.getJSONObject("user");
            UserModel user = UserModel.createModel(userJson);
            model.user = user;
            return model;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TIMELINE_MODEL, "Error from model:" + e.getMessage(), e);
        }
        return null;
    }

    public static ArrayList<TimelineModel> parseResponse(JSONArray response) {
        ArrayList<TimelineModel> timeList = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject json = (JSONObject) response.get(i);
                if(json != null ) {
                    TimelineModel item = TimelineModel.createModel(json);
                    if(item != null) {
                        timeList.add(item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return timeList;
    }
}
