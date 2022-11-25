package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ProfileDetailsActivity extends AppCompatActivity {
    LinearLayout MYORDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);


        MYORDER= findViewById(R.id.MYORDER);


        MYORDER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDetailsActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}