package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDAO {
    @Query("SELECT Tweet.body as tweet_body,Tweet.id as tweet_id,Tweet.createdAt as tweet_cretedAt,User.*"+
            "FROM Tweet INNER JOIN User ON Tweet.userId = User.id ORDER BY createdAt DESC LIMIT 5 ")
    List<TweetWithUser> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet...tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User...users);
}
