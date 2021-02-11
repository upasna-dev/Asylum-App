package com.traidev.asylum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AboutDonate extends AppCompatActivity {

    LinearLayout about,donate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_donate);


        about = findViewById(R.id.about);
        donate = findViewById(R.id.donate);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString("pType");

            if(type.equals("about"))
            {
                about.setVisibility(View.VISIBLE);
                donate.setVisibility(View.GONE);
            }
            else
            {
                about.setVisibility(View.GONE);
                donate.setVisibility(View.VISIBLE);
            }
        }




        ImageView BackBtn = findViewById(R.id.backClick);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });

    }
}
