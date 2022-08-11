package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class User {
    @PrimaryKey
    @ColumnInfo
    public long id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String screenName;
    @ColumnInfo
    public String profileImageUrl;


    public User() {
    }

    public static User fromJson(JSONObject json) throws JSONException {
        User user = new User();
        user.name = json.getString("name");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url_https");
        user.id = json.getLong("id");
        return user;
    }

    public static List<User> fromJsonArrary(List<Tweet> tweetsFromNetwork) {
        List<User> users = new ArrayList<>();
        for(int i=0; i<tweetsFromNetwork.size();i++){
            users.add(tweetsFromNetwork.get(i).user);
        }
        return users;
    }
}
