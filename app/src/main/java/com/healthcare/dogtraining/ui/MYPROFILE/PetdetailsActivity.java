package com.healthcare.dogtraining.ui.MYPROFILE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.EditProfileActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.Update_profile;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;

public class PetdetailsActivity extends AppCompatActivity {
Session1 session1;
    private RequestQueue rQueue;
    EditText tv_petname,tv_age,tv_breed;
    ImageView iv_back;
    Button btn_update;
    DatePickerDialog datePickerDialog;
    private Boolean isDateApply = false;
    String Dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petdetails);
        session1=new Session1(PetdetailsActivity.this);
        tv_petname=findViewById(R.id.tv_petname);
        tv_age=findViewById(R.id.tv_age);
        tv_breed=findViewById(R.id.tv_breed);
        iv_back=findViewById(R.id.iv_back);
        btn_update=findViewById(R.id.btn_update);
        getProfile();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackPressed();
            }
        });

        tv_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final java.util.Calendar c = java.util.Calendar.getInstance();
                int mYear = c.get(java.util.Calendar.YEAR); // current year
                int mMonth = c.get(java.util.Calendar.MONTH); // current month
                int mDay = c.get(java.util.Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(PetdetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                tv_age.setText( dayOfMonth+ "-" + (monthOfYear + 1) + "-" +year );
                                Dob = year+ "-" + (monthOfYear + 1) + "-" +dayOfMonth;
                                System.out.print("dateobmonth"+Dob);
                            }

                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

                }

               });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              updateApi();
            }
        });

    }

    private void OnDateSet() {

        }





    private void updateApi() {
        final ProgressDialog progressDialog = new ProgressDialog(PetdetailsActivity.this);
            progressDialog.setMessage("Processing...");
            progressDialog.show();

            AndroidNetworking.upload(BaseUrl+Update_profile)
                    .addMultipartParameter("user_id",session1.getUserId() )
                    //.addMultipartParameter("name","")
                    //.addMultipartParameter("email","")
                   // .addMultipartParameter("mobile","")
                    //.addMultipartParameter("any_pet","")
                    .addMultipartParameter("pet_name",tv_petname.getText().toString())
                    .addMultipartParameter("pet_age",tv_age.getText().toString())
                    .addMultipartParameter("pet_bareed",tv_breed.getText().toString())
                    .addMultipartFile("image",null)

                    .setTag("edit profile list")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            try {
                                progressDialog.dismiss();
                                Log.e("update profile",jsonObject.toString());
                                System.out.println("update profile=====    "+jsonObject.toString());
                                String result = jsonObject.getString("result");
                                String msg = jsonObject.getString("msg");

                                if (result.equalsIgnoreCase("true")) {


                                    finish();

                                } else {


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


        }





    private void getProfile() {
           System.out.println("<><===userid"+session1.getUserId());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl +getMyProfile,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject= new JSONObject(response);
                                String msg=jsonObject.getString("msg");
                                System.out.println("<><===getprofile"+jsonObject);
                                if (jsonObject.optString("result").equals("true")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    String id=jsonObject1.getString("id");
                                    String fullname=jsonObject1.getString("fullname");
                                    String email=jsonObject1.getString("email");
                                    String mobile=jsonObject1.getString("mobile");
                                    String pet_name=jsonObject1.getString("pet_name");
                                    String pet_age=jsonObject1.getString("pet_age");
                                    String pet_bareed=jsonObject1.getString("pet_bareed");
                                    String image=jsonObject1.getString("image");
                                    try {

                                        tv_petname.setText( pet_name);
                                        tv_age.setText( pet_age);
                                        tv_breed.setText( pet_bareed);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", session1.getUserId());
                    System.out.println("id=======       "+session1.getUserId());
                    return params;
                }
            };
            rQueue = Volley.newRequestQueue(PetdetailsActivity.this);
            rQueue.add(stringRequest);


        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}