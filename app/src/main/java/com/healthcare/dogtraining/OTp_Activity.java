package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.healthcare.dogtraining.API.Base_Url;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.resendOtp;
import static com.healthcare.dogtraining.API.Base_Url.verify_otp;

public class OTp_Activity extends AppCompatActivity {
    Button AVOTP;
    String id,otp;
    TextView signIn_OTPButton,tv_resendcode;
    private EditText[] editTexts;
    EditText edt_pinOne, edt_pinTwo, edt_pinThree,
            edt_pinFour, edt_pinSix;

    Session1 session;
    Button OTPVERIFYE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        AVOTP = findViewById(R.id.AVOTP);
        tv_resendcode = findViewById(R.id.tv_resendcode);
        edt_pinOne = findViewById(R.id.editTexttwo);
        edt_pinTwo = findViewById(R.id.pin_second_edittext);
        edt_pinThree = findViewById(R.id.pin_third_edittext);
        edt_pinFour = findViewById(R.id.pin_forth_edittext);
        session = new Session1(OTp_Activity.this);

        if (getIntent() != null) {
            id = getIntent().getStringExtra("user_id");
            System.out.println("userid========" + id);
        }


        editTexts = new EditText[]{edt_pinOne, edt_pinTwo, edt_pinThree, edt_pinFour};

        AVOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Otpverify(id);
            }
        });
        
        tv_resendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_pinFour.getText().clear();
                edt_pinThree.getText().clear();
                edt_pinTwo.getText().clear();
                edt_pinOne.getText().clear();
                ResendcodeApi();
            }
        });


        edt_pinOne.addTextChangedListener(new OTp_Activity.PinTextWatcher(0));
        edt_pinTwo.addTextChangedListener(new OTp_Activity.PinTextWatcher(1));
        edt_pinThree.addTextChangedListener(new OTp_Activity.PinTextWatcher(2));
        edt_pinFour.addTextChangedListener(new OTp_Activity.PinTextWatcher(3));

        // edt_pinSix.addTextChangedListener(new PinTextWatcher(5));

        edt_pinOne.setOnKeyListener(new OTp_Activity.PinOnKeyListener(0));
        edt_pinTwo.setOnKeyListener(new OTp_Activity.PinOnKeyListener(1));
        edt_pinThree.setOnKeyListener(new OTp_Activity.PinOnKeyListener(2));
        edt_pinFour.setOnKeyListener(new OTp_Activity.PinOnKeyListener(3));

        //  edt_pinSix.setOnKeyListener(new PinOnKeyListener(5));


    }

        private void ResendcodeApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl+resendOtp,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                String result = obj.getString("result");
                                String msg = obj.getString("msg");
                                System.out.println("<>===+resendotp"+obj);
                                if (result.equalsIgnoreCase("true")) {
                                    Toast.makeText(OTp_Activity.this, msg, Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(OTp_Activity.this, msg, Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",id);
                    return params;
                }
            };

            VolleySingleton.getInstance(OTp_Activity.this).addToRequestQueue(stringRequest);
        }







    private void Otpverify(String id) {
        final String pin_One = edt_pinOne.getText().toString().trim();
        final String pin_Two = edt_pinTwo.getText().toString().trim();
        final String pin_three = edt_pinThree.getText().toString().trim();
        final String pin_four = edt_pinFour.getText().toString().trim();
        otp = new String(pin_One + pin_Two + pin_three + pin_four);
        if (edt_pinOne.getText().toString().length() == 1) {
            if (edt_pinTwo.getText().toString().length() == 1) {
                if (edt_pinThree.getText().toString().length() == 1) {
                    if (edt_pinFour.getText().toString().length() == 1) {
                        String url = BaseUrl + verify_otp;
                        OTPVerifyApi(url, otp, id);
                    } else {
                        Toast.makeText(this, "Please enter correct otp.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Please enter currect otp.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Please enter currect otp.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please enter currect otp.", Toast.LENGTH_SHORT).show();
        }
    }


    //OTPApi

    private void OTPVerifyApi(String url, final String otp, final String id) {
        System.out.println("<><>===otpdkdkkdk" + id);
        final ProgressDialog progressDialog = new ProgressDialog(OTp_Activity.this);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.v("<>verify_otp  ", response.toString());
                        System.out.println("otp response ===== " + response.toString());
                        try {

                            progressDialog.dismiss();
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            System.out.println("<><>otpverify" + obj);
                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(OTp_Activity.this, ForgotPpassword.class);
                                intent.putExtra("user_id", id);
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
                params.put("otp", otp);
                params.put("user_id", id);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }


}