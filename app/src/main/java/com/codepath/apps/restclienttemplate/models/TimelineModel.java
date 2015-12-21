package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcvjetinovic on 12/12/15.
 */
@Table(name = "Tweets")
public class TimelineModel extends Model implements Serializable {
    private static final String TIMELINE_MODEL = "TIMELINE_MODEL";

    @Column(name="tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long tweetId;

    //    private String userName;
//    private String name;
    @Column(name = "tweet")
    private String tweet;
    @Column(name="tweetTime")
    private String tweetTime;
//    private String profileImage;

    @Column(name = "user")
    private UserModel user;

    @Column(name="relativeTweetTime")
    private String relativeTweetTime;

    public TimelineModel() {
        super();
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

    public static List<TimelineModel> get(int count) {
        ArrayList<TimelineModel> result = new ArrayList<>();
        return new Select().from(TimelineModel.class).limit(count).orderBy("tweetId DESC").execute();
    }

    public static void save(ArrayList<TimelineModel> modelList) {

        ActiveAndroid.beginTransaction();
        try{
            new Delete().from(UserModel.class).execute();
            new Delete().from(TimelineModel.class).execute();

            for (TimelineModel m: modelList) {
                UserModel user = UserModel.findUser(m.user.getUserId());
                Log.d(TIMELINE_MODEL, "Find user by id:" + m.user.getUserId() +" found:" + user);
                if(user != null){
                    m.user.save();
                }
                m.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TIMELINE_MODEL, "Error saving model to database:" + e.getMessage(), e);
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
