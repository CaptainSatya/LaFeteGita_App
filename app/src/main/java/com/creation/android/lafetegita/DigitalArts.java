package com.creation.android.lafetegita;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class DigitalArts extends BaseActivity {
    ImageButton ui_expandButton;
    TextView ui_supporting_text_tv;
    ImageButton _3d_expand_ibtn;
    TextView _3d_supporting_text_tv;

    Button details_ui_hack_btn;
    Button details_3d_hack_btn;

    //Widgets
    TextView tv_event_name, tv_event_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_digital_arts, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        ui_supporting_text_tv = findViewById(R.id.supporting_text_online_tv_id);
        ui_expandButton = findViewById(R.id.expand_button);
        ui_expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ui_supporting_text_tv.getVisibility() == View.VISIBLE) {
                    ui_expandButton.setImageResource(R.drawable.ic_expand_more_black_36dp);
                    ui_supporting_text_tv.setVisibility(View.GONE);
                } else {
                    ui_expandButton.setImageResource(R.drawable.ic_expand_less_black_36dp);
                    ui_supporting_text_tv.setVisibility(View.VISIBLE);

                    //ui_expandButton.setVisibility(View.GONE);

                }
            }
        });
        _3d_supporting_text_tv = findViewById(R.id._3d_supporting_text_tv_id);
        _3d_expand_ibtn = findViewById(R.id._3d_expand_ibtn_id);
        _3d_expand_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_3d_supporting_text_tv.getVisibility() == View.VISIBLE) {
                    _3d_expand_ibtn.setImageResource(R.drawable.ic_expand_more_black_36dp);
                    _3d_supporting_text_tv.setVisibility(View.GONE);
                } else {
                    _3d_expand_ibtn.setImageResource(R.drawable.ic_expand_less_black_36dp);
                    _3d_supporting_text_tv.setVisibility(View.VISIBLE);

                    //ui_expandButton.setVisibility(View.GONE);

                }
            }
        });
        details_ui_hack_btn = findViewById(R.id.details_literary_quiz_id);
        details_ui_hack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DigitalArts.this, DetailsUI_Hack.class);

                tv_event_name = findViewById(R.id.tv_event_name);
                tv_event_date = findViewById(R.id.tv_event_date);

                String event_name = tv_event_name.getText().toString();
                String event_date = tv_event_date.getText().toString();

                intent.putExtra("event_name", event_name);
                intent.putExtra("event_date", event_date);

                startActivity(intent);
            }
        });

        details_3d_hack_btn = findViewById(R.id.details_essay_online_btn_id);
        details_3d_hack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DigitalArts.this, Details3D_Hack.class);
                startActivity(intent);
            }
        });

    }
}

