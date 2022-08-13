package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    EditText etCompose;
    Button btnTweet;
    TextView tvDisplay;
    TextView tvCharacter;

    TwitterClient client;

    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        tvDisplay = findViewById(R.id.tvDisplay);
        tvCharacter = findViewById(R.id.tvCharacter);
        btnTweet = findViewById(R.id.btTweet);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()<=280) {
                    tvDisplay.setText("" + s.length());
                    tvDisplay.setTextColor(Color.parseColor("#c6d0c2"));
                    tvCharacter.setTextColor(Color.parseColor("#c6d0c2"));
                    btnTweet.setEnabled(true);
                }else
                {
                    tvDisplay.setTextColor(Color.parseColor("#DE0F17"));
                    tvCharacter.setTextColor(Color.parseColor("#DE0F17"));
                    btnTweet.setEnabled(false);
                }
            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry,your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry,your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet\"");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Tweet Says: "+tweet.body);

                            // Prepare data intent
                            Intent data = new Intent();
                            // Pass relevant data back as a result
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            // Activity finished ok, return the data
                            setResult(RESULT_OK, data); // set result code and bundle data for response
                            finish(); // closes the activity, pass data to parent
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet",throwable );
                    }
                });
            }
        });
    }
}