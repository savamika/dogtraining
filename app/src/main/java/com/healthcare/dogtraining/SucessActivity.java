package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SucessActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess);
        Toast.makeText(SucessActivity.this, "Your order placed successfully", Toast.LENGTH_SHORT).show();
        if (getIntent()!=null){
            orderId = getIntent().getStringExtra("orderid");

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SucessActivity.this, OrderActivity.class);
                i.putExtra("orderid",orderId );
                    startActivity(i);
                    finish();

            }

        }, SPLASH_SCREEN_TIME_OUT);

    }
}