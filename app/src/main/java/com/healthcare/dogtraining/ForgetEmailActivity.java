package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.forgetPassword;

public class ForgetEmailActivity extends AppCompatActivity {
    EditText email;
    Button signIn_signInforgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_email);
        email=findViewById(R.id.email);
        signIn_signInforgot=findViewById(R.id.signIn_signInforgot);



        signIn_signInforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().isEmpty())
                {
                    Forgot_APi_Call(email.getText().toString());
                }
                else {

                    Toast.makeText( ForgetEmailActivity.this, "please enter email id", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }





    private void Forgot_APi_Call(final String mstrEmail) {
        System.out.println("<><>dfidfkjdfjkjfdkj"+mstrEmail);
        final ProgressDialog progressDialog = new ProgressDialog( ForgetEmailActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + forgetPassword,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("check_error", response.toString());
                        try {
                            progressDialog.dismiss();
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");


                            if (result.equalsIgnoreCase("true")) {
                                String data=obj.getString("data");



                                Toast.makeText(ForgetEmailActivity.this, "Please check your registered email for otp. ", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getBaseContext(), OTp_Activity.class).putExtra("user_id",data));
                                finish();


                            } else {

                                //Toast.makeText(ForgetActivity.this, msg, Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {

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
                params.put("email", mstrEmail);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }





}
