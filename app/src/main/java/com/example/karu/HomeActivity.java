package com.example.karu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.karu.Fragments.BrowseFragment;
import com.example.karu.Fragments.CameraFragment;
import com.example.karu.Fragments.LikeFragment;
import com.example.karu.Fragments.MessageFragment;
import com.example.karu.Fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private static final int ACTIVITY_NUMBER = 0;

    private Button LogoutBtn;
    private TextView screenNameTextView;

    public static Context contextOfApplication;
    private Context mContex = HomeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // replace action bar with toolbar
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        Log.d(TAG, "onCreate: starting");

        /*setupBottomNavigationView();*/
        // to use getContentResolver() in fragments
        // dont delete this line of code
         contextOfApplication = getApplicationContext();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new BrowseFragment()).commit();

        /*LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Paper.book().destroy();
                AuthUI.getInstance()
                        .signOut(HomeActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                LogoutBtn.setEnabled(false);
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/



    }// onCreate()

    /**
     * BottomNaveView Setup
     */
/*
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView:  setting up BottomNavigationView");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContex,bottomNavigationViewEx);
        // change icon with screen
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);


    }*/

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch (item.getItemId())
                    {
                        case R.id.nav_browse:
                            selectedFragment = new BrowseFragment();
                            break;
                        case R.id.nav_like:
                            selectedFragment = new LikeFragment();
                            break;
                        case R.id.nav_add_item:
                            selectedFragment = new CameraFragment();
                            break;
                        case R.id.nav_message:
                            selectedFragment = new MessageFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return  true;
                }
    };

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

}
