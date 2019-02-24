package com.creation.android.lafetegita;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

public class DetailsUI_Hack extends BaseActivity {

    private static final String TAG = "DetailsUi_Hack";
    Button reg_btn;

    //variables
    String event_name, event_date;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private String user_id;

    //Firebase database
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myDbRef = firebaseDatabase.getReference(); // initilize database in 'setupFirebaseAuth' method.


    private MyFirebaseMethods myFirebaseMethods = new MyFirebaseMethods(DetailsUI_Hack.this);
//    private MyFirebaseMethods myFirebaseMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_details_ui__hack, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        receiveIntent();

        setupFirebaseAuth();

        reg_btn = findViewById(R.id.reg_btn_id);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(DetailsUI_Hack.this, GLoginActivity.class));
//                Toast.makeText(DetailsUI_Hack.this, "Registered", Toast.LENGTH_SHORT).show();
                receiveIntent();
                Intent intent = new Intent(DetailsUI_Hack.this, MyEventsActivity.class);
                intent.putExtra("event_name", event_name);
                intent.putExtra("event_name", event_name);

                //saveUserEventsToDatabase();

//                startActivity(intent);

            }
        });


    }

    private void saveUserEventsToDatabase() {
        myDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: dataSnapshot" + dataSnapshot);
                //retreive user info from the database
//
//                User user = myFirebaseMethods.getUserInfo(dataSnapshot); // dataSnapshot = user data from database
//                feedDataToNavigationWidgetsFromDatabase(user);

                UserEventDetails userEventDetails = new UserEventDetails(event_name, event_date);

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if (acct != null) {
                    String personName = acct.getDisplayName();
                    String personGivenName = acct.getGivenName();
                    String personFamilyName = acct.getFamilyName();
                    String personEmail = acct.getEmail();
                    String personId = acct.getId();
                    Uri personPhoto = acct.getPhotoUrl();

                    myDbRef.child("users")
                            .child(personId)
                            .child("registered_events")
                            .setValue(userEventDetails);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Error" + databaseError.getMessage());
            }
        });

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


        //.... manual setup ends
    }

    private void feedDataToNavigationWidgetsFromDatabase(User userInfo) {
        TextView tv_username = findViewById(R.id.tv_username);
        TextView tv_email = findViewById(R.id.tv_email);

        tv_username.setText(userInfo.getUsername());
        tv_email.setText(userInfo.getEmail());


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
