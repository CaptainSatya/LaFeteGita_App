package com.creation.android.lafetegita;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creation.android.lafetegita.GLoginActivity;
import com.creation.android.lafetegita.Model.User;
import com.creation.android.lafetegita.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GLoginActivity extends AppCompatActivity {


    private static final String TAG = "GLoginActivity";
    //Google sing in
    private GoogleSignInClient mGoogleSignInClient;
    SignInButton GsignInButton;

    // RC means request code. can give any number.
    private int RC_SIGN_IN = 1;


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private String user_id;

    //Firebase database
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myDbRef = firebaseDatabase.getReference(); // initilize database in 'setupFirebaseAuth' method.


    private MyFirebaseMethods myFirebaseMethods = new MyFirebaseMethods(GLoginActivity.this);



    //widgets
    ConstraintLayout layout_userInfo;
    TextView tv_username, tv_email;
    ImageView iv_userPhoto;
    Button btn_singOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_login);

        initWidgets();

        setupFirebaseAuth();

        //initilizing mAuth
        mAuth = FirebaseAuth.getInstance();

        // (optional) Set the dimensions of the sign-in button.
        GsignInButton = findViewById(R.id.sign_in_button);
        GsignInButton.setSize(SignInButton.SIZE_STANDARD);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GsignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btn_singOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void signOut() {
        mGoogleSignInClient.signOut();
        mAuth.signOut(); // get signed out
            layout_userInfo.setVisibility(View.GONE);

    }

    private void initWidgets() {
        layout_userInfo = findViewById(R.id.layout_userInfo);
        layout_userInfo.setVisibility(View.GONE);

        tv_username = findViewById(R.id.tv_username);
        tv_email = findViewById(R.id.tv_email);
        iv_userPhoto = findViewById(R.id.iv_userPhoto);
        btn_singOut = findViewById(R.id.btn_logout);

    }

//    private void updateUI(GoogleSignInAccount account) {
//        //If GoogleSignIn.getLastSignedInAccount returns a GoogleSignInAccount object (rather than null), the User has already signed in to your app with Google. Update your UI accordingly—that is, hide the sign-in button, launch your main activity, or whatever is appropriate for your app.
//        //If GoogleSignIn.getLastSignedInAccount returns null, the User has not yet signed in to your app with Google. Update your UI to display the Google Sign-in button.
//
//    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in User's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            startActivity(new Intent(GLoginActivity.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the User.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                            Toast.makeText(GLoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        layout_userInfo.setVisibility(View.GONE);

//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());

        //note arg 'getActivity()' changed manually to 'getApplicationContext()'.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            layout_userInfo.setVisibility(View.GONE);
            tv_username.setText(personName + '\n' + personId);
            tv_email.setText(personEmail);
        }

    }


//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }



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

                    myDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange: dataSnapshot " + dataSnapshot);

//                            myFirebaseMethods.addNewUser(email, mUsername);


                             //GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
                            //note arg 'getActivity()' changed manually to 'getApplicationContext()'.
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                            if (acct != null) {
                                String personName = acct.getDisplayName();
                                String personGivenName = acct.getGivenName();
                                String personFamilyName = acct.getFamilyName();
                                String personEmail = acct.getEmail();
                                String personId = acct.getId();
                                Uri personPhoto = acct.getPhotoUrl();

////            layout_userInfo.setVisibility(View.VISIBLE);
//                                tv_username.setText(personName + '\n' + personId);
//                                tv_email.setText(personEmail);

//                                User user = new User(user_id,personName,personEmail );
                                User user = new User(personId,personName,personEmail );

                                myDbRef.child("users")
                                        .child(personId)
                                        .setValue(user);

                            }

//                            User user = new User(user_id,, tv_email.getText().toString() );
//
//                            myDbRef.child("users")
//                                    .child(user_id)
//                                    .setValue(user);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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
////
////                User user = myFirebaseMethods.getUserInfo(dataSnapshot); // dataSnapshot = user data from database
////                feedDataToNavigationWidgetsFromDatabase(user);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: Error" + databaseError.getMessage());
//            }
//        });
//

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


