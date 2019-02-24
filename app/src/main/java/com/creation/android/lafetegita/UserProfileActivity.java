package com.creation.android.lafetegita;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creation.android.lafetegita.Model.User;
import com.creation.android.lafetegita.Model.UserEventDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private String user_id;

    //Firebase database
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myDbRef = firebaseDatabase.getReference(); // initilize database in 'setupFirebaseAuth' method.


    private MyFirebaseMethods myFirebaseMethods = new MyFirebaseMethods(UserProfileActivity.this);
//    private MyFirebaseMethods myFirebaseMethods;

    //Widgets
    TextView tv_email, tv_username;
    CardView cv_megaRegis;
    TextView tv_mega_regis_status;
    //Widgets

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);
        Log.d(TAG, "onCreate: started");

        tv_mega_regis_status = findViewById(R.id.tv_mega_regis_status);


       // loadMegaRegistrationStatus(new UserEventDetails("Mega Registration: Not Done", ""));

        setupFirebaseAuth();




        cv_megaRegis = findViewById(R.id.cv_mega_regis);
        cv_megaRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: dataSnapshot" + dataSnapshot);
                        //retreive user info from the database
//
//                User user = myFirebaseMethods.getUserInfo(dataSnapshot); // dataSnapshot = user data from database
//                feedDataToNavigationWidgetsFromDatabase(user);

                        String event_name = "registered for all events";
                        String event_date = "24 feb 2019";
                        UserEventDetails userEventDetails = new UserEventDetails(event_name, event_date);

                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        if (acct != null) {
                            String personId = acct.getId();
                            Uri personPhoto = acct.getPhotoUrl();

                            myDbRef.child("users")
                                    .child(personId)
                                    .child("registered_events")
                                    .setValue(userEventDetails);

                            Toast.makeText(UserProfileActivity.this, "Registered for all events!", Toast.LENGTH_SHORT).show();

                            UserEventDetails userEventDetails1 = myFirebaseMethods.getUserEventDetails(dataSnapshot);

                            loadMegaRegistrationStatus(userEventDetails1);
//                            TextView tv_mega_regis_status = findViewById(R.id.tv_mega_regis_status);
//                            tv_mega_regis_status.setText(userEventDetails1.getEvent_name());

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: Error" + databaseError.getMessage());
                    }
                });
            }
        });

    }

    public void loadMegaRegistrationStatus(UserEventDetails userEventDetails){
        //tv_mega_regis_status.setText(userEventDetails.getEvent_name());

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

    private void feedDataToNavigationWidgetsFromDatabase(User userInfo) {
        TextView tv_username = findViewById(R.id.tv_username);
        TextView tv_email = findViewById(R.id.tv_email);

//        if(userInfo !=null) {

//            if(tv_username.getText()==null || tv_email.getText() == null){
//                tv_username.setText("your name");
//                tv_email.setText("your email");
//
//            }
//            else {
        tv_username.setText(userInfo.getUsername());
        tv_email.setText(userInfo.getEmail());
//            }
//        }
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
