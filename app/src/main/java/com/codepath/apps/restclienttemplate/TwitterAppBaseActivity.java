package com.codepath.apps.restclienttemplate;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

/**
 * Created by mcvjetinovic on 12/13/15.
 */
public abstract class TwitterAppBaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twitter_log_menu, menu);

        return true;
    }
}
