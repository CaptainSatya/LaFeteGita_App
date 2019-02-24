package com.creation.android.lafetegita;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.creation.android.lafetegita.Model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String user_id;
   //firease auth

    //Google sing in
    private GoogleSignInClient mGoogleSignInClient;
    private String GuserId;



    //firebase database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    //firebase database


    //variables
    private Context mContext;
    private double mPhotoUploadProgress = 0;
    //variables


    public MyFirebaseMethods(Context context) {
        //initilize authentication
        mAuth = FirebaseAuth.getInstance();

        //initilize firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mContext = context;

//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(mContext.getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
//
//
////        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
//
//        //note arg 'getActivity()' changed manually to 'getApplicationContext()'.
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(mContext);
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            GuserId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//        }



        if (mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
        }
    }

    public User getUserInfo(DataSnapshot dataSnapshot) {

        User user = new User();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);


//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());

        //note arg 'getActivity()' changed manually to 'getApplicationContext()'.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(mContext);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            GuserId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }



        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            if (ds.getKey().equals("users")) {
                Log.d(TAG, "getUserFullInfo: getting user private details from " + ds);

                try {
                    user.setEmail(
                            ds.child(GuserId)
                                    .getValue(User.class)
                                    .getEmail()
                    );

                    user.setUsername(
                            ds.child(GuserId)
                                    .getValue(User.class)
                                    .getUsername()
                    );
                    user.setUser_id(
                            ds.child(GuserId)
                                    .getValue(User.class)
                                    .getUser_id()
                    );


                } catch (NullPointerException e) {
                    Log.d(TAG, "getUserFullInfo: NullPointerException" + e.getMessage());
                }

                Log.d(TAG, "getUserFullInfo: retrieving user private info " + user.toString());
            }

        }

        return user;
    }

}
