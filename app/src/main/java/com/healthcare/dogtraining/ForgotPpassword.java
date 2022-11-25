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
import static com.healthcare.dogtraining.API.Base_Url.setNewPassword;

public class ForgotPpassword extends AppCompatActivity {

    Button FORGOTPASSWORD;
    EditText NewPassword,RePassword;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_ppassword);
        FORGOTPASSWORD= findViewById(R.id.FORGOTPASSWORD);
        NewPassword= findViewById(R.id.NewPassword);
        RePassword= findViewById(R.id.RePassword);


        if (getIntent() != null) {
            id = getIntent().getStringExtra("user_id");
            System.out.println("userid========" + id);
        }




        findViewById(R.id.FORGOTPASSWORD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!NewPassword.getText().toString().isEmpty()&&
                        !RePassword.getText().toString().isEmpty() )
                {

                    if (NewPassword.getText().toString().equals(RePassword.getText().toString())) {

                        CallnewpasswordApi();
                    }
                }
                else {

                    Toast.makeText(  ForgotPpassword.this, "please enter password", Toast.LENGTH_SHORT).show();


                }




            }
        });
    }

           private void CallnewpasswordApi() {
           final ProgressDialog progressDialog = new ProgressDialog(  ForgotPpassword.this);
                   progressDialog.setTitle("Loading...");
                   progressDialog.show();
                   StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + setNewPassword,
                           new Response.Listener<String>() {
                              @Override
                               public void onResponse(String response) {
                                   Log.e("check_error", response.toString());
                                   try {
                                       progressDialog.dismiss();
                                       JSONObject obj = new JSONObject(response);
                                       String result = obj.getString("result");
                                       String msg = obj.getString("msg");
                                       System.out.println("<><====newpassword"+obj);
                                       if (result.equalsIgnoreCase("true")) {
                                           Log.e("response",response);

                                           Toast.makeText( ForgotPpassword.this, "Password updated Successfully.", Toast.LENGTH_LONG).show();
                                           Intent intent = new Intent(ForgotPpassword.this, SucessActivity .class);
                                           startActivity(intent);
                                           finish();

                                       } else {

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
                           params.put("user_id", id);
                           params.put("new_password", NewPassword.getText().toString());
                           params.put("confim_password",RePassword.getText().toString() );
                           return params;
                       }
                   };
                   VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

               }



           }
