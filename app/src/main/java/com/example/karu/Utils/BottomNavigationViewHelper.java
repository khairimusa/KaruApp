package com.example.karu.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.karu.HomeActivity;
import com.example.karu.R;
import com.example.karu.Screens.Favourite.FavouriteActivity;
import com.example.karu.Screens.Message.MessageActivity;
import com.example.karu.Screens.Photo.PhotoActivity;
import com.example.karu.Screens.Profile.ProfileActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHelper";

    public static void  setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(true);

    }

    // navigation between activity
    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_browse:
                        Intent intent1 = new Intent(context, HomeActivity.class);// ACTIVITY_NUMBER = 0
                        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent1);
                        break;

                    case R.id.nav_like:
                        Intent intent2 = new Intent(context, FavouriteActivity.class);// ACTIVITY_NUMBER = 1
                        intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent2);
                        break;

                    case R.id.nav_add_item:
                        Intent intent3 = new Intent(context, PhotoActivity.class);// ACTIVITY_NUMBER = 2
                        intent3.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent3);
                        break;

                    case R.id.nav_message:
                        Intent intent4 = new Intent(context, MessageActivity.class);// ACTIVITY_NUMBER = 3
                        intent4.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent4);
                        break;

                    case R.id.nav_profile:
                        Intent intent5 = new Intent(context, ProfileActivity.class);// ACTIVITY_NUMBER = 4
                        intent5.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent5);
                        break;

                }

                return false;
            }
        });

    }
}
