package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public long id;
    public int retweetCount;
    public int favoriteCount;
    public String type;
    public String media_url;

    public Tweet() {
    }

    public static Tweet fromJson(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = json.getString("text");
        tweet.createdAt = json.getString("created_at");
        tweet.user = User.fromJson(json.getJSONObject("user"));
        tweet.id = json.getLong("id");
        tweet.retweetCount = json.getInt("retweet_count");
        tweet.favoriteCount = json.getInt("favorite_count");

        JSONObject entities = json.getJSONObject("entities");
        if(entities.has("media")) {

            tweet.type = entities.getJSONArray("media").getJSONObject(0).getString("type");
            tweet.media_url = entities.getJSONArray("media").getJSONObject(0).getString("media_url");
        }
        else {
            tweet.type = null;
            tweet.media_url = null;
        }

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i=0; i<jsonArray.length();i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getFormattedTimestamp(){
        return TimeFormatter.getTimeDifference(createdAt);
    }

}
