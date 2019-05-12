package com.example.karu.Screens.Favourite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.karu.R;
import com.example.karu.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class FavouriteActivity extends AppCompatActivity {
    private static final String TAG = "FavouriteActivity";
    private static final int ACTIVITY_NUMBER = 1;
    private TextView screenNameTextView;
    private Context mContext = FavouriteActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();
        screenNameTextView = (TextView) findViewById(R.id.screen_name);
        screenNameTextView.setText("Favorite");
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView:  setting up BottomNavigationView");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
        // change icon with screen
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);


    }


}
