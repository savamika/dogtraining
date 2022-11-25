package com.healthcare.dogtraining.VendorSignup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.healthcare.dogtraining.LoginActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.add_accountdetails;

public class AddBankDetails extends AppCompatActivity {
    private static final String TAG = "AddBankDetails";
    String vendor_id = "", bank_name = "", account_no = "", ifsc_code = "", address = "";
    Button btn_submit;
    EditText et_bankname, et_account, et_ifsc, et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_details);

        if (getIntent() != null) {
            vendor_id = getIntent().getStringExtra("id");

        }
        et_bankname = findViewById(R.id.et_bankname);
        et_account = findViewById(R.id.et_account);
        et_ifsc = findViewById(R.id.et_ifsc);
        et_address = findViewById(R.id.et_address);
        btn_submit = findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_name=et_bankname.getText().toString();
                        account_no=et_account.getText().toString();
                ifsc_code=et_ifsc.getText().toString();
                        address=et_address.getText().toString();
                if (bank_name.equalsIgnoreCase("")) {
                    et_bankname.setError("Please enter bank name");
                } else if (account_no.equalsIgnoreCase("")) {
                    et_account.setError("Please enter bank account");
                } else if (ifsc_code.equalsIgnoreCase("")) {
                    et_ifsc.setError("Please enter IFSC");
                } else if (address.equalsIgnoreCase("")) {
                    et_address.setError("Please enter address");
                } else {
                    add_accountdetails();
                }


            }
        });
    }


    private void add_accountdetails() {

        ProgressDialog progressDialog = new ProgressDialog(AddBankDetails.this);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,BaseUrl +add_accountdetails, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e(TAG, "add_accountdetails Response:    " + response);
                    if (jsonObject.getString("result").equals("true")) {


                        progressDialog.dismiss();
                        showdialog();

                    } else {
                        progressDialog.dismiss();

                        //Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();


                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();


                map.put("vendor_id", vendor_id);
                map.put("bank_name", bank_name);
                map.put("account_no", account_no);
                map.put("ifsc_code", ifsc_code);
                map.put("address", address);


                Log.e(TAG, "add_accountdetails Response:  Params: " + map);
                return map;

            }
        };
        VolleySingleton.getInstance(AddBankDetails.this).addToRequestQueue(stringRequest);
    }

    private void showdialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBankDetails.this);
        builder1.setMessage("Your registration has been done successfully. Please wait 3-4 days for your Id approval.");
        builder1.setCancelable( false);
        builder1.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                        Intent intent = new Intent(AddBankDetails.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

           /* builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });*/


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}


//