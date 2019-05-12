package com.example.karu.Screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.karu.R;
import com.example.karu.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BrowseActivity extends AppCompatActivity {
    private static final String TAG = "BrowseActivity";
    private TextView screenNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();
        screenNameTextView = (TextView) findViewById(R.id.screen_name);
        screenNameTextView.setText("Browse");
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView:  setting up BottomNavigationView");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);


    }
}
