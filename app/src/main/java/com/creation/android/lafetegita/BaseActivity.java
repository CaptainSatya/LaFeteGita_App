package com.creation.android.lafetegita;

/**
 * Created by ravi on 3/8/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class BaseActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String appPackageName = getPackageName();
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_home_id) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    // Handle the camera action
                } else if (id == R.id.nav_about_us_id) {
                    Intent intent = new Intent(getApplicationContext(), AboutUS.class);
                    startActivity(intent);

                } else if (id == R.id.nav_events_id) {
                    Intent intent = new Intent(getApplicationContext(), EventCategories.class);
                    startActivity(intent);

                } else if (id == R.id.nav_mega_reg_id) {
                    /*Intent intent=new Intent(getApplicationContext(),EventCategories.class);
                    startActivity(intent);*/

                } else if (id == R.id.nav_share) {
                    /*Intent intent=new Intent(getApplicationContext(),EventCategories.class);
                    startActivity(intent);*/

                } else if (id == R.id.nav_send) {
                    /*Intent intent=new Intent(getApplicationContext(),EventCategories.class);
                    startActivity(intent);*/

                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;

               /* switch (item.getItemId()) {

                    case R.id.nav_dashboard:
                        Intent dash = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(dash);
//                        finish();
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.nav_about_us:
                        Intent anIntent = new Intent(getApplicationContext(), com.example.ravi.myapplication.AboutUS.class);
                        startActivity(anIntent);
//                        finish();
                        drawerLayout.closeDrawers();
                        break;

                }*/

            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}