package com.creation.android.lafetegita;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;

public class EventCategories extends BaseActivity {

    CardView online_event_card;
    CardView hackathon_card;
    CardView literary_card;
    CardView digital_card;
    CardView arts_card;
    CardView cultural_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.event_categories, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        online_event_card = findViewById(R.id.online_card_id);
        online_event_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCategories.this, OnlineEvents.class);
                startActivity(intent);
            }
        });

        hackathon_card = findViewById(R.id.hackathon_card_id);
        hackathon_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCategories.this, Hackathon.class);
                startActivity(intent);
            }
        });

        literary_card = findViewById(R.id.literary_card_id);
        literary_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCategories.this, Literary.class);
                startActivity(intent);
            }
        });

        digital_card = findViewById(R.id.digital_card_id);
        digital_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCategories.this, DigitalArts.class);
                startActivity(intent);
            }
        });

        arts_card = findViewById(R.id.arts_card_id);
        arts_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCategories.this, Arts.class);
                startActivity(intent);
            }
        });

        cultural_card = findViewById(R.id.cultural_card_id);
        cultural_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCategories.this, Cultural.class);
                startActivity(intent);
            }
        });
    }
}
