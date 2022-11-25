package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.healthcare.dogtraining.utils.Config;
import com.healthcare.dogtraining.utils.PrefrenceManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 4000;
    ImageView imageView;
    Session1 session1;
    public static String regId;
    PrefrenceManager prefrenceManager;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);
        session1=new Session1(SplashActivity.this);
        mContext = this;
        printHashKey(this);
        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                prefrenceManager = new PrefrenceManager(SplashActivity.this);
                prefrenceManager.setTokenId(mContext, newToken);
                putFirebaseRegId(newToken);
                setPreference(mContext, "regId", newToken);
                getPreference(mContext, "regId");
                displayFirebaseRegId();

                System.out.println("<dfjdfjdfjdfjdfjdfjtoken" + newToken);


            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session1.isLoggedIn())
                {
                    Intent i=new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }
                }

                }, SPLASH_SCREEN_TIME_OUT);
                }

    private void printHashKey(SplashActivity splashActivity) {
        String TAG = "HASHKEY";
            try {
                PackageInfo info = SplashActivity.this.getPackageManager().getPackageInfo(SplashActivity.this.getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String hashKey = new String(Base64.encode(md.digest(), 0));
                    // Log.e("HASH KEY", "printHashKey: "+hashKey);
                    //  Log.e(TAG, "printHashKey: "+ hashKey);
                    System.out.println("printHashKey      =========       "+hashKey);
//                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
                }
            } catch (NoSuchAlgorithmException e) {
                // Log.e(TAG, "printHashKey()", e);
            } catch (Exception e) {
                Log.e(TAG, "printHashKey()", e);
            }
        }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(SplashActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                }
                return;
            }
        }
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
        Log.e("check splash", " : " + regId);
    }

    private void putFirebaseRegId(String newToken) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("regId", newToken);
        editor.commit();
    }

    public static boolean setPreference(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
        String value=settings.getString(key,"default value");
        Log.e("get fb key ++",value);
        return settings.getString(key, "defaultValue");
    }


}