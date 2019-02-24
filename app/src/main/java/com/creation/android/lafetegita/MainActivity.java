package com.creation.android.lafetegita;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private Context mContext = MainActivity.this;

    SliderLayout sliderLayout;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    ImageButton play_btn;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    Button know_more_btn;
    Button explore_btn;
    ImageView lafetegita_iv;


    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    //firebase auth

    //widgets
    TextView tv_share;
    //widgets


    //private TextView mTextMessage;
    //Button btn;

    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_camera:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.nav_gallery:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.nav_share:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //firebase auth
        setupFirebaseAuth();

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        //mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        lafetegita_iv = findViewById(R.id.lafetegita_iv_id);
        final YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.Youtube_play_id);
        play_btn = findViewById(R.id.play_btn_id);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("EvXg3HiMbus");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag.initialize(YouTubeConfig.getApiKey(), mOnInitializedListener);
                play_btn.setVisibility(View.GONE);
                lafetegita_iv.setVisibility(View.GONE);
            }
        });

        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :
        setSliderViews();

        know_more_btn = findViewById(R.id.know_btn_id);
        know_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutUS.class);
                startActivity(intent);
            }
        });

        explore_btn = findViewById(R.id.details_invite_online_id);
        explore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventCategories.class);
                startActivity(intent);
            }
        });

        tv_share= findViewById(R.id.action_button_2);
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            }
        });
    }

    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.srila);
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.event4);
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.prizes_win);
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.lafetegita_image);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //sliderView.setDescription("ISCKON presents!");
            // final int finalI = i;

            /*sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(MainActivity.this, "This is slider " + (finalI+1), Toast.LENGTH_SHORT).show();

                }
            });*/

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }


    /*
    .....................................firebase................................................
     */


    /**
     * checks to see if User is singned in.
     * @param user
     */
    //self created from video
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: is logged in");

        if(user==null)
            startActivity(new Intent(mContext, GLoginActivity.class));

    }


    /**
     * setup firebase auth object.
     */

    // this I set up from coding with mitch video , not with firebase Assistant.
    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                checkCurrentUser(user);

                if (user!= null){
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
                } else{
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };

    }
    //.... manual setup ends

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);

        //always checks the User on starting the activity.
        checkCurrentUser(mAuth.getCurrentUser());

//        // Check if User is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListner!=null)
            mAuth.removeAuthStateListener(mAuthListner);
    }



}
