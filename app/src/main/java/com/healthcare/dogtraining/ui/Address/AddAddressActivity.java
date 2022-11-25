package com.healthcare.dogtraining.ui.Address;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.CartlistActivity;
import com.healthcare.dogtraining.Model.CityModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.add_address;
import static com.healthcare.dogtraining.API.Base_Url.getCityList;

public class AddAddressActivity extends AppCompatActivity {
    ArrayList<CityModel> CountryName = new ArrayList<>();
    ArrayList<String> City = new ArrayList<>();
    String country_name_, Spin_country_id,main_city_id;
    Spinner select_city;
    Button Add_newAddress,existinglocation;
    Session1 session1;
    EditText et_Address,et_pincode;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        select_city=findViewById(R.id.select_city);
        Add_newAddress=findViewById(R.id.Add_newAddress);
        et_Address=findViewById(R.id.et_Address);
        et_pincode=findViewById(R.id.et_pincode);
        existinglocation=findViewById(R.id.existinglocation);
        iv_back=findViewById(R.id.iv_back);
        session1=new Session1(AddAddressActivity.this);

        GetCityListApi();

        Add_newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!et_Address.getText().toString().isEmpty()&&!et_pincode.getText().toString().isEmpty()) {
                  if(main_city_id!=null){
                      AddAddressApi();
                  }else{
                      Toast.makeText(AddAddressActivity.this, "Please select city", Toast.LENGTH_SHORT).show();

                  }

              } else{

                  Toast.makeText(AddAddressActivity.this, "Please select all fields", Toast.LENGTH_SHORT).show();

              }

            }
        });

        existinglocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(AddAddressActivity.this,GetAddressListActivity.class
              );
              startActivity(intent);
            }
        });

        select_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                main_city_id = CountryName.get(i).getCity_id();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackPressed();
              }
             });
    }

            private void AddAddressApi() {
            final ProgressDialog progressDialog = new ProgressDialog(AddAddressActivity.this);
            progressDialog.setTitle("Loading..");
            progressDialog.show();
            String url = BaseUrl + add_address ;
            AndroidNetworking.post(url)
                    .addBodyParameter("user_id", session1.getUserId())
                    .addBodyParameter("address",et_Address.getText().toString() )
                    .addBodyParameter("city", main_city_id)
                    .addBodyParameter("postcode",et_pincode.getText().toString())
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            progressDialog.dismiss();
                            try {
                                String result = jsonObject.getString("result");
                                String msg = jsonObject.getString("msg");
                                Log.e("adress", jsonObject.toString());
                                if (result.equalsIgnoreCase("true")) {
                                    Toast.makeText(AddAddressActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(AddAddressActivity.this, GetAddressListActivity.class);
                                startActivity(intent);
                                }else{
                                }

                              } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Log.e("error_my_join", anError.toString());
                        }
                    });
                        }
                        private void GetCityListApi() {
               final ProgressDialog loading = new ProgressDialog(AddAddressActivity.this);
               loading.setMessage("Please Wait...");
               loading.show();
               StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseUrl + getCityList,
                       new Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                               try {
                                   loading.dismiss();
                                   JSONObject eventObject = new JSONObject(response);
                                   String result = eventObject.getString("result");
                                   String msg = eventObject.getString("msg");
                                   if (result.equals("true")) {
                                       JSONArray jsonArray = eventObject.getJSONArray("data");
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                           String city_id = jsonObject1.getString("city_id");
                                           String city_name = jsonObject1.getString("city_name");
                                           String state_id = jsonObject1.getString("state_id");
                                           City.add(city_name);
                                           CountryName.add(new CityModel(city_id,city_name,state_id
                                                   ));
                                       }
                                       select_city.setAdapter(new ArrayAdapter<String>(AddAddressActivity.this,
                                               android.R.layout.simple_spinner_dropdown_item, City));

                                   } else {

                                       Toast.makeText(AddAddressActivity.this,"" +msg, Toast.LENGTH_SHORT).show();
                                   }
                               } catch (Exception e) {
                                   Log.d("Tag", e.getMessage());

                               }
                           }
                       },

                       new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                               loading.dismiss();
                               Toast.makeText(AddAddressActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                           }
                       }) {
                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                       Map<String, String> map = new HashMap<String, String>();
                       return map;
                   }
               };

               RequestQueue requestQueue = Volley.newRequestQueue(this);
               requestQueue.add(stringRequest);
            }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}