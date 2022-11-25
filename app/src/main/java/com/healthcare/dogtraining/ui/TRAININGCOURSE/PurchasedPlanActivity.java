package com.healthcare.dogtraining.ui.TRAININGCOURSE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.healthcare.dogtraining.Adapter.PurchasedCommandPlanListAdapter;
import com.healthcare.dogtraining.Adapter.commandclk;
import com.healthcare.dogtraining.Model.PurchasedPlanModel;
import com.healthcare.dogtraining.Model.PurchaseplanInsideData;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.TrainingPacageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;
import static com.healthcare.dogtraining.API.Base_Url.plan_command_list;

public class PurchasedPlanActivity extends AppCompatActivity implements commandclk {
    public static RelativeLayout progress_view;
    CircleImageView profileimage;
    TextView tv_name, tv_breed, tv_dob, tv_addnewplan;
    Session1 session1;
    List<PurchasedPlanModel> purchasedPlanModels = new ArrayList<>();
    List<PurchaseplanInsideData> purchasedPlanInsideModels = new ArrayList<>();
    //PurchasedPlanListAdapter adapter;
    RecyclerView recyclerview;
    PurchasedCommandPlanListAdapter adapter;
    ImageView backbutton;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_plan);
        profileimage = findViewById(R.id.profileimage);
        progress_view = findViewById(R.id.progress_view);
        tv_name = findViewById(R.id.tv_name);
        tv_breed = findViewById(R.id.tv_breed);
        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_dob = findViewById(R.id.tv_dob);
        recyclerview = findViewById(R.id.recyclerview);
        tv_addnewplan = findViewById(R.id.tv_addnewplan);
        session1 = new Session1(PurchasedPlanActivity.this);
        session1.setAddressID(PurchasedPlanActivity.this, "Demo");

        GetProfileApi();
        // GetPlanCommandListApi();

        GetPlanCommandListApi();

        tv_addnewplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchasedPlanActivity.this, TrainingPacageActivity.class);
                startActivity(intent);
            }
        });

    }


    private void GetPlanCommandListApi() {
        final ProgressDialog progressDialog = new ProgressDialog(PurchasedPlanActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String url = BaseUrl + plan_command_list;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", session1.getUserId())
                //.addBodyParameter("course_trainig_id","")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("jsonObject", "onResponse: " + jsonObject.toString());
                        progressDialog.dismiss();
                        try {
                            purchasedPlanModels.clear();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);

                                    String plan_order_id = dataObject.getString("plan_order_id");
                                    String certificate_status = dataObject.getString("certificate_status");
                                    String certificate_file = dataObject.getString("certificate_file");
                                    Log.e("certificate_status", "onResponse: " + certificate_status);


                                    PurchasedPlanModel allCommunityModel = new PurchasedPlanModel
                                            (dataObject.getString("course_id"),
                                                    dataObject.getString("course_trainig_id"),
                                                    dataObject.getString("name"));
                                    purchasedPlanModels.add(allCommunityModel);

                                    JSONArray command = dataObject.getJSONArray("course_ids");


                                    for (int j = 0; j < command.length(); j++) {
                                        JSONObject comanddataObject = command.getJSONObject(j);

                                        PurchaseplanInsideData allCommunityModel1 = new PurchaseplanInsideData
                                                (comanddataObject.getString("id"),
                                                        dataObject.getString("course_trainig_id"),
                                                        comanddataObject.getString("name"),
                                                        dataObject.getString("name"),
                                                        comanddataObject.getString("command_name"),
                                                        comanddataObject.getString("command_id"),
                                                        comanddataObject.getString("video"),
                                                        comanddataObject.getString("audio"),
                                                        comanddataObject.getString("thumbnail"),
                                                        comanddataObject.getString("image"),
                                                        comanddataObject.getString("date_time"),
                                                        plan_order_id, certificate_status,certificate_file);
                                        purchasedPlanInsideModels.add(allCommunityModel1);


                                    }


                                }
                                adapter = new PurchasedCommandPlanListAdapter(purchasedPlanInsideModels, PurchasedPlanActivity.this);
                                RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(PurchasedPlanActivity.this);
                                recyclerview.setLayoutManager(mLayoutManger);
                                recyclerview.setLayoutManager(new LinearLayoutManager(PurchasedPlanActivity.this, RecyclerView.VERTICAL, false));
                                recyclerview.setItemAnimator(new DefaultItemAnimator());
                                recyclerview.setItemAnimator(new DefaultItemAnimator());
                                recyclerview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();


                            } else {

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


    private void GetProfileApi() {
        progress_view.setVisibility(View.INVISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + getMyProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            System.out.println("<><===getprofile" + jsonObject);
                            if (jsonObject.optString("result").equals("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                String id = jsonObject1.getString("id");
                                String fullname = jsonObject1.getString("fullname");
                                String email = jsonObject1.getString("email");
                                String mobile = jsonObject1.getString("mobile");
                                String pet_name = jsonObject1.getString("pet_name");
                                String pet_age = jsonObject1.getString("pet_age");
                                String pet_bareed = jsonObject1.getString("pet_bareed");
                                String image = jsonObject1.getString("image");
                                try {

                                    tv_name.setText(fullname);
                                    tv_breed.setText(pet_bareed);
                                    tv_dob.setText(pet_age);
                                    Glide.with(PurchasedPlanActivity.this)
                                            .load(Image_Path + image)
                                            .apply(new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .dontAnimate()
                                                    .centerCrop()
                                                    .dontTransform())
                                            .skipMemoryCache(true)
                                            .into(profileimage);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                Toast.makeText(PurchasedPlanActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PurchasedPlanActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session1.getUserId());
                System.out.println("id=======       " + session1.getUserId());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(PurchasedPlanActivity.this);
        rQueue.add(stringRequest);
    }


    @Override
    public void Oncommandclk(String name, String course_trining_id) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
