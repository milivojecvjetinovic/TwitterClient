package com.codepath.apps.restclienttemplate.models;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mcvjetinovic on 12/14/15.
 */
public class UserModelTest {

    @Test
    public void testSaveUserModel() throws JSONException {

        String userString = "{\"created_at\":\"Sat Dec 12 08:39:51 +0000 2015\",\"id\":675595910135463936,\"id_str\":\"675595910135463936\",\"text\":\"getting ready for the futsal game\",\"source\":\"<a href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\">Twitter Web Client<\\/a>\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":92453746,\"id_str\":\"92453746\",\"name\":\"milivoje cvjetinovic\",\"screen_name\":\"fkdrina\",\"location\":\"Santa Clara, CA\",\"description\":\"\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":2,\"friends_count\":4,\"listed_count\":0,\"created_at\":\"Wed Nov 25 05:21:33 +0000 2009\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"verified\":false,\"statuses_count\":1,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_0_normal.png\",\"profile_image_url_https\":\"https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_0_normal.png\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":true,\"following\":false,\"follow_request_sent\":false,\"notifications\":false},\"geo\":null,\"coordinates\":null,\"place\":{\"id\":\"4b58830723ec6371\",\"url\":\"https:\\/\\/api.twitter.com\\/1.1\\/geo\\/id\\/4b58830723ec6371.json\",\"place_type\":\"city\",\"name\":\"Santa Clara\",\"full_name\":\"Santa Clara, CA\",\"country_code\":\"US\",\"country\":\"United States\",\"contained_within\":[],\"bounding_box\":{\"type\":\"Polygon\",\"coordinates\":[[[-122.005597,37.322943],[-121.930045,37.322943],[-121.930045,37.419037],[-122.005597,37.419037]]]},\"attributes\":{}},\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[]},\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"}";

        JSONObject json = new JSONObject(userString) ;
        JSONObject user = json.getJSONObject("user");

        UserModel model = UserModel.createModel(user);
        Assert.assertNotNull(user);
    }
}