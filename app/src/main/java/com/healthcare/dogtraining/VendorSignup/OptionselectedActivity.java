package com.healthcare.dogtraining.VendorSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.SignupActivity;

public class OptionselectedActivity extends AppCompatActivity {
TextView tv_usersignup,tv_vendorsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optionselected);
        tv_usersignup=findViewById(R.id.tv_usersignup);
        tv_vendorsignup=findViewById(R.id.tv_vendorsignup);

        tv_usersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OptionselectedActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        tv_vendorsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OptionselectedActivity.this, AddVendorActivity.class);
                startActivity(intent);
            }
        });
    }
}