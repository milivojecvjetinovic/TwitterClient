package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.codepath.apps.restclienttemplate.models.StatusModel;
import com.codepath.apps.restclienttemplate.models.TimelineModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ComposeTweetActivity extends AppCompatActivity {


    private static final String COMPOSE_TWEET = "COMPOSE_TWEET";
    public static final int COMPOSE_TWEET_ACTIVITY = 0;
    public static final String NEW_TWEET = "newTweet";
    private static final int MAX_LENGTH = 140;
    private TwitterClient client;
    private EditText etTweet;
    private TextView tvCharLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        client = TwitterApp.getRestClient();
        tvCharLeft = (TextView) findViewById(R.id.tvCharLeft);

        etTweet = (EditText) findViewById(R.id.etTweet);

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int charLeft = MAX_LENGTH - s.toString().length();
                tvCharLeft.setText(charLeft + " characters left.");
            }
        });

        Button btnTweetSubmit = (Button) findViewById(R.id.btnTweetSubmit);
        btnTweetSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ComposeTweetActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                StatusModel model = new StatusModel();
                model.setStatus(etTweet.getText().toString());
                client.postStatus(model, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(COMPOSE_TWEET, "New status:" + response.toString());
                        TimelineModel model = TimelineModel.createModel(response);
                        //send back intent
                        Intent i = new Intent();
                        i.putExtra(ComposeTweetActivity.NEW_TWEET, model);
                        setResult(RESULT_OK, i);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e(COMPOSE_TWEET,"Error creating new tweet.", throwable);
                        Toast.makeText(ComposeTweetActivity.this, "Error creating new tweet!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tweeter_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuHome:
                setResult(RESULT_CANCELED, null);
                finish();
            default:
                //handle other cases here
                return super.onOptionsItemSelected(item);
        }
    }
}
