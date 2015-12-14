package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mcvjetinovic on 12/12/15.
 */
public class UserModel implements Serializable {
    private static final String USER_MODEL = "USER_MODEL";
    private long userId;
    private String name;
    private String screenName;
    private String profileImage;

    public UserModel() {

    }

    public static UserModel createModel(JSONObject json) {

        try {
            UserModel user = new UserModel();
            user.name = json.getString("name");
            user.profileImage = json.getString("profile_image_url");
            user.screenName = json.getString("screen_name");
            user.userId = json.getLong("id");
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
}
