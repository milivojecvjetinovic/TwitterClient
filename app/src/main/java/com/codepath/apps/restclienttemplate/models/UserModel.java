package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mcvjetinovic on 12/12/15.
 */
@Table(name = "Users")
public class UserModel extends Model implements Serializable {
    private static final String USER_MODEL = "USER_MODEL";

    @Column(name="userId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long userId;

    @Column(name="name")
    private String name;

    @Column(name="screenName")
    private String screenName;

    @Column(name="profileImage")
    private String profileImage;

    @Column(name="tagLine")
    private String tagLine;

    @Column(name="followersCount")
    private int followersCount;

    @Column(name="followingCount")
    private int followingCount;

    @Column(name="fullName")
    private String fullName;

    @Column(name="tweetsCount")
    private int tweetsCount;

    public UserModel() {
        super();
    }

    public static UserModel createModel(JSONObject json) {

        try {
            UserModel user = new UserModel();
            user.name = json.getString("name");
            user.profileImage = json.getString("profile_image_url");
            user.screenName = json.getString("screen_name");
            user.userId = json.getLong("id");
            if(json.has("description")) user.tagLine = json.getString("description");
            if(json.has("followers_count")) user.followersCount = json.getInt("followers_count");
            if(json.has("friends_count")) user.followingCount = json.getInt("friends_count");
            if(json.has("name")) user.fullName = json.getString("name");
            if(json.has("statuses_count")) user.tweetsCount = json.getInt("statuses_count");
            return user;
        } catch (JSONException e) {
            Log.e(USER_MODEL, "Error parsing user from twitter:" + e.getMessage(),e);
        }
        return null;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public static UserModel findUser(long userId){
        return new Select().from(UserModel.class).where("userId = ?", userId).executeSingle();
    }

    public String getTagLine() {
        return tagLine;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getFullName() {
        return fullName;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }
}
