package com.codepath.apps.restclienttemplate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDAO;
import com.codepath.apps.restclienttemplate.models.TweetWithUser;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    public static final String TAG = "TimelineActivity";

    TwitterClient client;
    List<Tweet> tweets;
    RecyclerView rvTweet;
    TweetsAdapter adapter;

    private SwipeRefreshLayout swipeContainer;
    private TweetDAO tweetDAO;

    String username;
    String user_profile_image_url;
    String user_screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        // Lookup the swipe container view

        swipeContainer = findViewById(R.id.swipeContainer);

        tweets = new ArrayList<>();
        client = TwitterApp.getRestClient(this);
        tweetDAO = ((TwitterApp) getApplicationContext()).getMyDatabase().TweetDAO();


        rvTweet = findViewById(R.id.rvTweet);
        adapter = new TweetsAdapter(tweets);
        rvTweet.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweet.setLayoutManager(linearLayoutManager);

        swipeContainer = findViewById(R.id.swipeContainer);

        AsyncTask.execute(() -> {
            Log.i(TAG, "Showing data from database");
            List<TweetWithUser> tweetWithUsers = tweetDAO.recentItems();
            List<Tweet> tweetFromDB = TweetWithUser.getTweetList(tweetWithUsers);
            adapter.clear();
            adapter.addTweets(tweetFromDB);

        });


        swipeContainer.setOnRefreshListener(this::populateHomeTimeline);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Store a member variable for the listener
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweet.addOnScrollListener(scrollListener);

        populateHomeTimeline();
        populateUserTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    ActivityResultLauncher<Intent> composeActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // If the user comes back to this activity from EditActivity
                    // with no error or cancellation
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // Get the data passed from EditActivity
//                        String editedString = data.getExtras().getString("newString");
                        Tweet tweet = Parcels.unwrap(Objects.requireNonNull(data).getParcelableExtra("tweet"));

                        tweets.add(0,tweet);

                        adapter.notifyItemInserted(0);
                        rvTweet.smoothScrollToPosition(0);
                    }
                }
            });

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Compose) {
            Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show();
            Intent i  = new Intent(this,ComposeActivity.class);
            i.putExtra("username",username);
            i.putExtra("user_profile_image_url",user_profile_image_url);
            i.putExtra("user_screenName",user_screenName);
//            startActivity(i);
            composeActivityResultLauncher.launch(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweetsFromNetwork = Tweet.fromJsonArray(jsonArray);
                    adapter.clear();
                    adapter.addTweets(tweetsFromNetwork);

                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);

                    AsyncTask.execute(() -> {
                        Log.i(TAG, "Saving data from database");
                        List<User> usersFromNetwork = User.fromJsonArrary(tweetsFromNetwork);
                        tweetDAO.insertModel(usersFromNetwork.toArray(new User[0]));
                        tweetDAO.insertModel(tweetsFromNetwork.toArray(new Tweet[0]));


                    });
                } catch (JSONException e) {
                    Log.e(TAG, "Json Exception: ", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure ", throwable);
            }
        });
    }

    private void populateUserTimeline() {
        client.getUser_timeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    JSONObject user = jsonArray.getJSONObject(0).getJSONObject("user");

                    username = user.getString("name");
                    user_profile_image_url = user.getString("profile_image_url");
                    user_screenName = user.getString("screen_name");

                } catch (JSONException e) {
                    Log.e(TAG, "Json Exception: ", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure ", throwable);
            }
        });
    }

    private void loadMoreData() {
        // 1. Send an API request to retrieve appropriate paginated data
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // 2. Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                    // 3. Append the new data objects to the existing set of items inside the array of items
                    // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`
                    adapter.addTweets(tweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        }, tweets.get(tweets.size() - 1).id);

    }
}