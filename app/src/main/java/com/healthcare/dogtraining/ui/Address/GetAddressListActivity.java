package com.healthcare.dogtraining.ui.Address;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.API.Base_Url;
import com.healthcare.dogtraining.Adapter.GetAddressAdapter;
import com.healthcare.dogtraining.Model.GetAddresslistModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.getaddresslist;

public class GetAddressListActivity extends AppCompatActivity {
Session1 session1;
List<GetAddresslistModel>getAddresslistModels=new ArrayList<>();
GetAddressAdapter adapter;
RecyclerView recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address_list);
        session1=new Session1(GetAddressListActivity.this);
        recyclerview=findViewById(R.id.recyclerview);
        GetAddressListApi();
    }

        private void GetAddressListApi() {
        final ProgressDialog progressDialog = new ProgressDialog(GetAddressListActivity.this);
                progressDialog.setTitle("Loading..");
                progressDialog.show();
                String url = BaseUrl +getaddresslist ;
                AndroidNetworking.post(url)
                        .addBodyParameter("user_id",session1.getUserId())
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                progressDialog.dismiss();
                                try {

                                    String result = jsonObject.getString("result");
                                    String msg = jsonObject.getString("msg");
                                    System.out.println("<><====addresss"+jsonObject);
                                    if (result.equalsIgnoreCase("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject dataObject = jsonArray.getJSONObject(i);
                                            GetAddresslistModel allCommunityModel = new GetAddresslistModel(
                                                    dataObject.getString("id"),
                                                    dataObject.getString("user_id"),
                                                    dataObject.getString("address"),
                                                    dataObject.getString("city"),
                                                    dataObject.getString("postcode"),
                                                    dataObject.getString("city_name"));
                                            getAddresslistModels.add(allCommunityModel);

                                            }


                                    } else {

                                    }

                                    adapter= new GetAddressAdapter(getAddresslistModels, GetAddressListActivity.this);
                                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(GetAddressListActivity.this);
                                    recyclerview.setLayoutManager(mLayoutManger);
                                    recyclerview.setLayoutManager(new LinearLayoutManager(GetAddressListActivity.this, RecyclerView.VERTICAL, false));
                                    recyclerview.setItemAnimator(new DefaultItemAnimator());
                                    recyclerview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

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
                       }               }