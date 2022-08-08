package com.codepath.apps.restclienttemplate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    List<Tweet> tweets;

    public TweetsAdapter(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Tweet tweet = tweets.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(100))
                .into(holder.ivProfileImage);
        holder.tvName.setText(tweet.user.name);
        holder.tvScreenName.setText("@" + tweet.user.screenName);
        holder.tvTimestamp.setText(tweet.getFormattedTimestamp());
        holder.tvBody.setText("" + tweet.body);
        holder.tvRetweet.setText("" + tweet.retweetCount);
        holder.tvLike.setText("" + tweet.favoriteCount);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear()
    {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addTweets(List<Tweet> tweetList)
    {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvScreenName;
        TextView tvBody;
        ImageView ivProfileImage;
        TextView tvTimestamp;
        TextView tvRetweet;
        TextView tvLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvLike = itemView.findViewById(R.id.tvLike);
        }
    }
}
