package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
public class Tweet {
    @PrimaryKey
    @ColumnInfo
    public long id;
    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public int retweetCount;
    @ColumnInfo
    public int favoriteCount;
    @ColumnInfo
    public String type;
    @ColumnInfo
    public String media_url;

    @Ignore
    public User user;
    @ColumnInfo
    public long userId;



    public Tweet() {
    }

    public static Tweet fromJson(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.id = json.getLong("id");
        tweet.body = json.getString("text");
        tweet.createdAt = json.getString("created_at");
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

        User user = User.fromJson(json.getJSONObject("user"));
        tweet.user = user;
        tweet.userId = user.id;

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
        return TimeFormatter.getTimeStamp(createdAt);
    }

}
