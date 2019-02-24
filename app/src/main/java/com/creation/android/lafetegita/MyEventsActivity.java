package com.creation.android.lafetegita;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.creation.android.lafetegita.Model.User;
import com.creation.android.lafetegita.Model.UserEventDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyEventsActivity extends BaseActivity {

    private static final String TAG = "MyEventsActivity.this";
    //variables
    String event_name, event_date;

    //Widgets
    TextView tv_event_name, tv_event_date;
    //Widgets


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private String user_id;

    //Firebase database
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myDbRef = firebaseDatabase.getReference(); // initilize database in 'setupFirebaseAuth' method.


    private MyFirebaseMethods myFirebaseMethods = new MyFirebaseMethods(MyEventsActivity.this);
//    private MyFirebaseMethods myFirebaseMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_events);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_my_events, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

//        receiveIntent();

        intWidgets();

        setupFirebaseAuth();

        tv_event_name.setText(event_name);
        tv_event_date.setText(event_date);


    }

    private void intWidgets(){
        tv_event_name= findViewById(R.id.tv_event_name);
        tv_event_date = findViewById(R.id.tv_event_date);
    }


    private void receiveIntent(){
        Intent intent= getIntent();
        event_name= intent.getStringExtra("event_name");
        event_date = intent.getStringExtra("event_date");
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

//                User user = myFirebaseMethods.getUserInfo(dataSnapshot); // dataSnapshot = user data from database
//                feedDataToNavigationWidgetsFromDatabase(user);

                UserEventDetails userEventDetails = myFirebaseMethods.getUserEventDetails(dataSnapshot);
                loadEventsDataFromDatabase(userEventDetails);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Error" + databaseError.getMessage());
            }
        });


        //.... manual setup ends
    }

    private void loadEventsDataFromDatabase(UserEventDetails userEventDetails) {
//        TextView tv_username = findViewById(R.id.tv_username);
//        TextView tv_email = findViewById(R.id.tv_email);

        tv_event_name.setText(userEventDetails.getEvent_name());
        tv_event_date.setText(userEventDetails.getEvent_date());

    }




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
