package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.healthcare.dogtraining.VendorSignup.AddVendorActivity;
import com.healthcare.dogtraining.VendorSignup.OptionselectedActivity;

public class WelcomeActivity extends AppCompatActivity {
    Button signin, signup;
    LinearLayout signIn_signupvendor;
    ImageView signIn_google;
    private FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        signin= findViewById(R.id.signin);
        signup= findViewById(R.id.signup);
        signIn_signupvendor= findViewById(R.id.signIn_signupvendor);
        //signIn_google= findViewById(R.id.signIn_google);


        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });



        findViewById(R.id.signIn_signupvendor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, AddVendorActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}