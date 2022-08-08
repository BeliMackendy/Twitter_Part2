package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    Tweet tweet;

    TextView tvName;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvTimestamp;
    ImageView ivProfileImage;
    TextView tvRetweet;
    TextView tvLike;
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivImage = findViewById(R.id.ivImage);

        // unwrap the Tweet passed in via intent, using its simple name as a key
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvName.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(100))
                .into(ivProfileImage);
        tvBody.setText("" + tweet.body);
        tvTimestamp.setText(tweet.getFormattedTimestamp());
        Glide
                .with(this)
                .load(tweet.media_url)
                .transform(new RoundedCorners(100))
                .into(ivImage);




    }
}