package com.creation.android.lafetegita;

/**
 * Created by ravi on 3/8/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.creation.android.lafetegita.Model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    //for google singIn and Signout
    private GoogleSignInClient mGoogleSignInClient;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private String user_id;

    //Firebase database
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myDbRef = firebaseDatabase.getReference(); // initilize database in 'setupFirebaseAuth' method.


    private MyFirebaseMethods myFirebaseMethods = new MyFirebaseMethods(BaseActivity.this);
//    private MyFirebaseMethods myFirebaseMethods;

    //Widgets
    TextView tv_email, tv_username;
    //Widgets


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


        //init widgets
        initWidgets();
        //init widgets

        //initilize Firebase auth
        setupFirebaseAuth();



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Configure Google Sign In, ends

        //feeding data
//        feedData();



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
                    Intent intent=new Intent(getApplicationContext(),UserProfileActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_MyEvents) {
//                    Intent intent=new Intent(getApplicationContext(),MyEventsActivity.class);
                    Intent intent=new Intent(getApplicationContext(),UserProfileActivity.class);
                    startActivity(intent);

                } else if (id == R.id.logout) {
                    /*Intent intent=new Intent(getApplicationContext(),EventCategories.class);
                    startActivity(intent);*/
                    Toast.makeText(getApplicationContext(), "logged out", Toast.LENGTH_SHORT).show();
                    mGoogleSignInClient.signOut();
                    mAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), GLoginActivity.class));

                }

                else if (id == R.id.profile) {
                    Intent intent=new Intent(getApplicationContext(),UserProfileActivity.class);
                    startActivity(intent);


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

    private  void initWidgets(){

//         myFirebaseMethods = new MyFirebaseMethods(BaseActivity.this);


         tv_username = findViewById(R.id.tv_username);
         tv_email= findViewById(R.id.tv_email);

    }

    private void feedDataToNavigationWidgetsFromDatabase(User userInfo) {
        TextView tv_username = findViewById(R.id.tv_username);
        TextView tv_email = findViewById(R.id.tv_email);

//        if(userInfo !=null) {

//            if(tv_username.getText()==null || tv_email.getText() == null){
//                tv_username.setText("your name");
//                tv_email.setText("your email");
//
//            }
////            else {
//        tv_username.setText(userInfo.getUsername());
//        tv_email.setText(userInfo.getEmail());
//            }
//        }
    }



    /*
    .....................................firebase................................................
     */

    /**
     * setup firebase auth object.
     */

    // this I set up from coding with mitch video , not with firebase Assistant.
    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
        }

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };

        myDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: dataSnapshot" + dataSnapshot);
                //retreive user info from the database

                User user = myFirebaseMethods.getUserInfo(dataSnapshot); // dataSnapshot = user data from database
                feedDataToNavigationWidgetsFromDatabase(user);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Error" + databaseError.getMessage());
            }
        });


        //.... manual setup ends
    }
//
//    private void feedData() {
//        myDbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: dataSnapshot" + dataSnapshot);
//                //retreive user info from the database
//
//                User user = myFirebaseMethods.getUserInfo(dataSnapshot); // dataSnapshot = user data from database
//                feedDataToNavigationWidgetsFromDatabase(user);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: Error" + databaseError.getMessage());
//            }
//        });
//    }
//
//


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListner != null)
            mAuth.removeAuthStateListener(mAuthListner);
    }


}