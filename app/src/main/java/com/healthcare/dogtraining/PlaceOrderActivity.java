package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.healthcare.dogtraining.utils.VolleySingleton;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.add_to_cart;
import static com.healthcare.dogtraining.API.Base_Url.orderPayment;

public class PlaceOrderActivity extends AppCompatActivity implements PaymentResultListener {
    LinearLayout linear_onlinepaymnet;
    Float Total;
    TextView mPrice;
    String payment_method="";
    String orderId,certificate_price,type;
    LinearLayout l_cahs;
    private static final String TAG = PlaceOrderActivity.class.getSimpleName();
    LinearLayout Confirmation;
    Checkout checkout;
    String planid,mrp;
    Button btn_pay;
    private Checkout chackout;
    private String razorpayKey;
    Session1 session1;
    CheckBox cirtificate_add_checkbox;
    String Status="false";
    int price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        linear_onlinepaymnet= findViewById(R.id.linear_onlinepaymnet);
        mPrice= findViewById(R.id.mPrice);
        l_cahs= findViewById(R.id.l_cahs);
        cirtificate_add_checkbox= findViewById(R.id.cirtificate_add_checkbox);
        session1=new Session1(PlaceOrderActivity.this);

        checkout = new Checkout();
        checkout.preload(getApplicationContext());

//       checkout.setKeyID("rzp_live_lKVGe5q85Kypq0");
//       new liveky
//        old live key rzp_live_1Bd38v2kXKp505
       //checkout.setKeyID("rzp_test_fGyRn0eyLIbSxF");

        if (getIntent() != null) {
            Total=getIntent().getFloatExtra("total",0);
            orderId=getIntent().getStringExtra("orderId");
            type=getIntent().getStringExtra("type");
            certificate_price=getIntent().getStringExtra("certificate_price");


            System.out.println("<><><> "+certificate_price);
            System.out.println("<><><> "+Total);
            System.out.println("<><><>orderId  "+orderId);
            System.out.println("<><><>product  "+type);
            System.out.println("<><><>userid   "+session1.getUserId());

            mPrice.setText("₹"+Total);
            }
        if (session1.get_role().equalsIgnoreCase("Purchase_package")){
            cirtificate_add_checkbox.setVisibility(View.VISIBLE);
        }else{
            cirtificate_add_checkbox.setVisibility(View.GONE);
        }

        cirtificate_add_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Total= (Total+Integer.parseInt(certificate_price));
                    Log.e("TAG", "onCheckedChanged: "+Total );
                    mPrice.setText("₹"+Total);
                    Status="true";
                }else{
                    Total= (Total-Integer.parseInt(certificate_price));
                    Log.e("TAG", "onCheckedChanged: "+Total );
                    mPrice.setText("₹"+Total);
                    Status="false";

                }
            }
        });
        findViewById(R.id.l_cahs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallbillingApi(orderId,type);
            }
        });

        linear_onlinepaymnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rezorpayCall();

               }
               });

         }

        private void rezorpayCall() {
           // razorpayKeylive="rzp_live_1Bd38v2kXKp505";
        razorpayKey="rzp_live_lKVGe5q85Kypq0"; //Generate your razorpay key from Settings-> API Keys-> copy Key Id
        //razorpayKey="rzp_test_fGyRn0eyLIbSxF"; //Generate your razorpay key from Settings-> API Keys-> copy Key Id
                chackout = new Checkout();
                chackout.setKeyID(razorpayKey);
                try {
                    JSONObject options = new JSONObject();
                    options.put("name",session1.getUser_name() );
                    options.put("description", "Razorpay Payment ");
                    options.put("currency", "INR");
                    double total = Double.parseDouble(String.valueOf(Total));
                    total = total * 100;
                    options.put("amount", total);
                    JSONObject preFill = new JSONObject();
                    preFill.put("email", session1.getEmail());
                    preFill.put("contact",session1.getMobile());
                    options.put("prefill", preFill);
                    chackout.open(PlaceOrderActivity.this, options);

                   } catch (Exception e) {
                    Toast.makeText(PlaceOrderActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                   }
                   }








        private void CallbillingApi(String orderId,String type) {
        String url = BaseUrl + orderPayment ;
        final ProgressDialog progressDialog = new ProgressDialog(PlaceOrderActivity.this);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("after_pay_bill",response.toString());
                                try {
                                    progressDialog.dismiss();
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);
                                    String result = obj.getString("result");
                                    String msg = obj.getString("msg");
                                    System.out.println("<>===="+obj);
                                    if (result.equalsIgnoreCase("true")){
                                        Intent intent = new Intent(PlaceOrderActivity.this, SucessActivity.class);
                                        intent.putExtra("orderid",orderId );
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();
                                   
                                    }else{

                                        Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

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
                        params.put("order_id", orderId);

                        params.put("type",type);

                        params.put("payment_method","netbanking");

                        params.put("total_amount", String.valueOf(Total));
                        params.put("transaction_id","razorpayPaymentID");
                        params.put("status","1");
                        params.put("certificate_status",Status);
                        System.out.println("<><===="+params);
                        return params;
                      }
                     };
                VolleySingleton.getInstance(PlaceOrderActivity.this).addToRequestQueue(stringRequest);
                }


         @Override
        public void onPaymentSuccess(String razorpayPaymentID) {
        System.out.println("<><><===razorpaytrans"+razorpayPaymentID);
             String url = BaseUrl + orderPayment ;
             final ProgressDialog progressDialog = new ProgressDialog(PlaceOrderActivity.this);
             progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
             progressDialog.setMessage("");
             progressDialog.show();
             StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                     new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             Log.e("after_pay_bill",response.toString());
                             try {
                                 progressDialog.dismiss();
                                 //converting response to json object
                                 JSONObject obj = new JSONObject(response);
                                 String result = obj.getString("result");
                                 String msg = obj.getString("msg");
                                 System.out.println("<>====success"+obj);
                                 if (result.equalsIgnoreCase("true")){
                                     Intent intent = new Intent(PlaceOrderActivity.this, SucessActivity.class);
                                     intent.putExtra("orderid",orderId );

                                     startActivity(intent);
                                     finish();
                                     Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();
                                 }else{
                                     Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

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
                     params.put("order_id", orderId);
                     params.put("payment_method","netbanking");
                     params.put("type",type);
                     params.put("total_amount", String.valueOf(Total));
                     params.put("transaction_id",razorpayPaymentID);
                     params.put("status","1");
                     params.put("certificate_status",Status);
                     System.out.println("<><===="+params);
                     return params;
                 }
             };
             VolleySingleton.getInstance(PlaceOrderActivity.this).addToRequestQueue(stringRequest);




    }


        @Override
        public void onPaymentError(int i, String error) {
        Toast.makeText(PlaceOrderActivity.this, "Transaction unsuccessful: "+ error , Toast.LENGTH_LONG).show();


    }

    @Override
    public void onBackPressed() {
        session1.set_role("");
        super.onBackPressed();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}