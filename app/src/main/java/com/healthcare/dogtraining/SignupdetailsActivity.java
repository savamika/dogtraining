package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.gps.GPSTracker;
import com.healthcare.dogtraining.utils.PrefrenceManager;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Signup_Api;

public class SignupdetailsActivity extends AppCompatActivity {
    Button SIGNUPTYPE;
    private Double lat;
    private Double lng;
    private GPSTracker tracker;
    EditText et_petName,et_pet_age,et_petbreed;
    String intent,name,email,phone,password,anypet,state_id,city_id,zipcode;
    Session1 session1;
    private Boolean isDateApply = false;
    PrefrenceManager prefrenceManager;
    String FCM_ID,imagefile;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupdetails);
        SIGNUPTYPE= findViewById(R.id.SIGNUPTYPE);
        et_petName= findViewById(R.id.et_petName);
        et_pet_age= findViewById(R.id.et_pet_age);
        et_petbreed= findViewById(R.id.et_petbreed);
        session1=new Session1(SignupdetailsActivity.this);

        tracker=new GPSTracker(SignupdetailsActivity.this);
        LocationManager locationManager = (LocationManager)SignupdetailsActivity.this.getSystemService(LOCATION_SERVICE);
          FCM_ID=  prefrenceManager.getTokenId(SignupdetailsActivity.this);


        if (tracker.canGetLocation() == true) {
            lat = tracker.getLatitude();
            lng = tracker.getLongitude();
            Log.e("current_lat ", " " + String.valueOf(lat));
            Log.e("current_Lon ", " " + String.valueOf(lng));

           }else if (tracker.canGetLocation() == false) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Toast.makeText(SignupdetailsActivity.this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
            }else{
                showGPSDisabledAlertToUser();
            }
        }

        if (getIntent() != null) {
            try {
                intent = getIntent().getStringExtra("INTENT");
                name = getIntent().getStringExtra("Et_name");
                email = getIntent().getStringExtra("Et_email");
                phone = getIntent().getStringExtra("Et_mobile");
                password = getIntent().getStringExtra("Et_password");
                anypet = getIntent().getStringExtra("AnyPet");
                state_id = getIntent().getStringExtra("Stateid");
                city_id = getIntent().getStringExtra("Cityid");
                zipcode = getIntent().getStringExtra("Zipcode");
                imagefile = getIntent().getStringExtra("imagefile");
                System.out.println("<><==="+name);
                System.out.println("<><==="+email);
                System.out.println("<><==="+phone);
                System.out.println("<><==="+password);
                System.out.println("<><==="+anypet);
                System.out.println("<><==="+imagefile);


                file = new File(imagefile);
                System.out.println("<><===file"+file);
              } catch (Exception e) {
                System.out.println("chekc exc" + e.toString());
              }
              }


        et_pet_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateSet();
                isDateApply=true;
            }
        });




            SIGNUPTYPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !et_petName.getText().toString().isEmpty() &&
                        !et_petbreed.getText().toString().isEmpty()
                && !et_pet_age.getText().toString().isEmpty()) {

                    CallSignUpApi(name, email, password, zipcode, phone, state_id, city_id);

                }else{
                    Toast.makeText(SignupdetailsActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                   }
                   }
                   });

    }

            private void onDateSet() {
            Calendar mcurrentDate = Calendar.getInstance();
            final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
            final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
            final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
            DatePickerDialog mDatePicker = new DatePickerDialog(SignupdetailsActivity.this,R.style.calender_dialog_theme, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar myCalendar = Calendar.getInstance();
                    myCalendar.set(Calendar.YEAR, selectedyear);
                    myCalendar.set(Calendar.MONTH, selectedmonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                    String myFormat = "yyyy/MM/dd"; //Change as you need
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                    if (isDateApply) {

                        et_pet_age.setText(sdf.format(myCalendar.getTime()));

                    } else {

                    }



                    mDay[0] = selectedday;
                    mMonth[0] = selectedmonth;
                    mYear[0] = selectedyear;
                }
            }, mYear[0], mMonth[0], mDay[0]);

                mDatePicker.show();


        }






    private void showGPSDisabledAlertToUser() {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SignupdetailsActivity.this);
                alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Goto Settings Page To Enable GPS",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent callGPSSettingIntent = new Intent(
                                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        SignupdetailsActivity.this.startActivity(callGPSSettingIntent);
                                    }
                                });
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                android.app.AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }

            private void CallSignUpApi(String username, String email, String password, String zipcode, String phone, String state_id, String city_id) {
           /* final ProgressDialog progressDialog = new ProgressDialog(SignupdetailsActivity.this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + Signup_Api ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("signup data", response.toString());
                        try {
                            progressDialog.dismiss();
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            System.out.println("<><==callsignup"+obj);

                            if (result.equalsIgnoreCase("true")) {
                                JSONObject obj1 = obj.getJSONObject("data");
                                String id=obj1.getString("id");
                                String fullname=obj1.getString("fullname");
                                String email=obj1.getString("email");
                                String mobile=obj1.getString("mobile");

                                session1.setLogin(true);
                                session1.setUserId(id);
                                session1.setMobile(mobile,email);
                                session1.setUser_name(fullname);

                                Toast.makeText(SignupdetailsActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignupdetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            }

                          } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", username);
                params.put("email", email);
                params.put("mobile", phone);
                params.put("password", password);
                params.put("any_pet", anypet);
                params.put("fcm_id", FCM_ID);
                params.put("latitude", lat.toString());
                params.put("longitude", lng.toString());
                params.put("city_id", city_id);
                params.put("state_id", state_id);
                params.put("pincode", zipcode);
                params.put("pet_name", et_petName.getText().toString());
                params.put("pet_age", et_pet_age.getText().toString());
                params.put("pet_bareed", et_petbreed.getText().toString());
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }*/
                final ProgressDialog progressDialog = new ProgressDialog(SignupdetailsActivity.this);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                //System.out.println("Path=======        "+file.getAbsolutePath());
                AndroidNetworking.upload(BaseUrl+Signup_Api)
                        .addMultipartParameter("name",username)
                        .addMultipartParameter("email",email)
                        .addMultipartParameter("mobile",phone)
                        .addMultipartParameter("password",password)
                        .addMultipartParameter("any_pet",anypet)
                        .addMultipartParameter("fcm_id",FCM_ID)
                        .addMultipartParameter("latitude",lat.toString())
                        .addMultipartParameter("longitude",lng.toString())
                        .addMultipartParameter("city_id",city_id)
                        .addMultipartParameter("state_id",state_id)
                        .addMultipartParameter("pincode",zipcode)
                        .addMultipartFile("image",file)
                        .addMultipartParameter("pet_name",et_petName.getText().toString())
                        .addMultipartParameter("pet_age",et_pet_age.getText().toString())
                        .addMultipartParameter("pet_bareed",et_petbreed.getText().toString())


                        .setTag("edit profile list")
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @SuppressLint("WrongConstant")
                            @Override
                            public void onResponse(JSONObject jsonObject) {

                                try {
                                    progressDialog.dismiss();
                                    String result = jsonObject.getString("result");
                                    String msg = jsonObject.getString("msg");

                                    System.out.println("<><====jsonobject"+jsonObject);

                                    if (result.equalsIgnoreCase("true")) {
                                        JSONObject obj1 = jsonObject.getJSONObject("data");
                                        String id=obj1.getString("id");
                                        String fullname=obj1.getString("fullname");
                                        String email=obj1.getString("email");
                                        String mobile=obj1.getString("mobile");

                                        session1.setLogin(true);
                                        session1.setUserId(id);
                                        session1.setMobile(mobile,email);
                                        session1.setUser_name(fullname);

                                        Toast.makeText(SignupdetailsActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SignupdetailsActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {

                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


                                    }

                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                progressDialog.dismiss();
                                Log.e("error = ", "" + error);
                            }
                        });

           }}