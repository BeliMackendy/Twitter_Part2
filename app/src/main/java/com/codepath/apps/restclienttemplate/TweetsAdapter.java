package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Tweet> tweets;
    private final int VIDEO = 1;
    private final int IMAGE = 2;

    public TweetsAdapter(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIDEO) {// Inflate the custom layout
            View view2 = inflater.inflate(R.layout.item_tweet_video, parent, false);

            // Return a new holder instance
            return new ViewHolder2(view2);
        }
        if (viewType == IMAGE) {// Inflate the custom layout
            View view3 = inflater.inflate(R.layout.item_tweet_image, parent, false);

            // Return a new holder instance
            return new ViewHolder3(view3);
        }
        else {
            View view1 = inflater.inflate(R.layout.item_tweet, parent, false);

            // Return a new holder instance
            return new ViewHolder1(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Tweet tweet = tweets.get(position);

        if (holder.getItemViewType() == VIDEO) {
            ViewHolder2 holder2 = (ViewHolder2) holder;

            holder2.tvName.setText(tweet.user.name);
            holder2.tvScreenName.setText("@" + tweet.user.screenName);
            Glide
                    .with(holder.itemView.getContext())
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCorners(100))
                    .into(holder2.ivProfileImage);
            holder2.tvBody.setText("" + tweet.body);
            holder2.tvTimestamp.setText("" + tweet.getFormattedTimestamp());
            holder2.tvRetweet.setText("" + tweet.retweetCount);
            holder2.tvLike.setText("" + tweet.favoriteCount);
            Uri uri = Uri.parse(tweet.media_url);
            holder2.vViewTweet.setVideoURI(uri);
            holder2.vViewTweet.start();
        }
        if (holder.getItemViewType() == IMAGE) {
            ViewHolder3 holder3 = (ViewHolder3) holder;

            holder3.tvName.setText(tweet.user.name);
            holder3.tvScreenName.setText("@" + tweet.user.screenName);
            Glide
                    .with(holder.itemView.getContext())
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCorners(100))
                    .into(holder3.ivProfileImage);
            holder3.tvBody.setText("" + tweet.body);
            holder3.tvTimestamp.setText(tweet.getFormattedTimestamp());
            holder3.tvRetweet.setText("" + tweet.retweetCount);
            holder3.tvLike.setText("" + tweet.favoriteCount);
            Glide
                    .with(holder.itemView.getContext())
                    .load(tweet.media_url)
                    .transform(new RoundedCorners(100))
                    .into(holder3.ivImage);
        }
        else {

            ViewHolder1 holder1 = (ViewHolder1) holder;

            holder1.tvName.setText(tweet.user.name);
            holder1.tvScreenName.setText("@" + tweet.user.screenName);
            Glide
                    .with(holder.itemView.getContext())
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCorners(100))
                    .into(holder1.ivProfileImage);
            holder1.tvBody.setText("" + tweet.body);
            holder1.tvTimestamp.setText("" + tweet.getFormattedTimestamp());
            holder1.tvRetweet.setText("" + tweet.retweetCount);
            holder1.tvLike.setText("" + tweet.favoriteCount);
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addTweets(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (tweets.get(position).type != null) {
            if (tweets.get(position).type.contentEquals("video")) {
                return VIDEO;
            }
            if (tweets.get(position).type.contentEquals("photo")) {
                return IMAGE;
            }
        }
        return 0;
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvScreenName;
        TextView tvBody;
        ImageView ivProfileImage;
        TextView tvTimestamp;
        TextView tvRetweet;
        TextView tvLike;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvLike = itemView.findViewById(R.id.tvLike);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                Tweet tweet = tweets.get(position);

                Intent i = new Intent(v.getContext(), TweetDetailActivity.class);
                i.putExtra("tweet", Parcels.wrap(tweet));
                v.getContext().startActivity(i);
            }
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvTimestamp;
        ImageView ivProfileImage;
        TextView tvReply;
        TextView tvRetweet;
        TextView tvLike;
        VideoView vViewTweet;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvReply = itemView.findViewById(R.id.tvReply);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvLike = itemView.findViewById(R.id.tvLike);
            vViewTweet = itemView.findViewById(R.id.vViewTweet);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvTimestamp;
        ImageView ivProfileImage;
        TextView tvReply;
        TextView tvRetweet;
        TextView tvLike;
        ImageView ivImage;

        public ViewHolder3(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvReply = itemView.findViewById(R.id.tvReply);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvLike = itemView.findViewById(R.id.tvLike);
            ivImage = itemView.findViewById(R.id.ivImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                Tweet tweet = tweets.get(position);

                Intent i = new Intent(v.getContext(), TweetDetailActivity.class);
                i.putExtra("tweet", Parcels.wrap(tweet));
                v.getContext().startActivity(i);
            }
        }
    }
}
