package com.creation.android.lafetegita;

import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class OnlineEvents extends BaseActivity {
    ImageButton expandButton;
    TextView supporting_text_tv;
    ImageButton essay_expand_ibtn;
    TextView essay_supporting_text_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_online_events, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        supporting_text_tv = findViewById(R.id.supporting_text_online_tv_id);
        expandButton = findViewById(R.id.expand_button);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (supporting_text_tv.getVisibility() == View.VISIBLE) {
                    expandButton.setImageResource(R.drawable.ic_expand_more_black_36dp);
                    supporting_text_tv.setVisibility(View.GONE);
                } else {
                    expandButton.setImageResource(R.drawable.ic_expand_less_black_36dp);
                    supporting_text_tv.setVisibility(View.VISIBLE);

                    //expandButton.setVisibility(View.GONE);

                }
            }
        });
        essay_supporting_text_tv = findViewById(R.id.essay_supporting_text_tv_id);
        essay_expand_ibtn = findViewById(R.id.essay_expand_ibtn_id);
        essay_expand_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (essay_supporting_text_tv.getVisibility() == View.VISIBLE) {
                    essay_expand_ibtn.setImageResource(R.drawable.ic_expand_more_black_36dp);
                    essay_supporting_text_tv.setVisibility(View.GONE);
                } else {
                    essay_expand_ibtn.setImageResource(R.drawable.ic_expand_less_black_36dp);
                    essay_supporting_text_tv.setVisibility(View.VISIBLE);

                    //expandButton.setVisibility(View.GONE);

                }
            }
        });

    }
}

