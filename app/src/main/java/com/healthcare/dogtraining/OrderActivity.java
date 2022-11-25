package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 6000;
    String orderId;
    TextView txtorderid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        txtorderid=findViewById(R.id.txtorderid);
         if (getIntent()!=null){
            orderId = getIntent().getStringExtra("orderid");

        }
        txtorderid.setText("Your order with Id "+orderId+" has been placed successfully" );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(OrderActivity.this, HomeActivity.class);
                i.putExtra("orderid",orderId );
                startActivity(i);
                finish();

            }

        }, SPLASH_SCREEN_TIME_OUT);

    }
}