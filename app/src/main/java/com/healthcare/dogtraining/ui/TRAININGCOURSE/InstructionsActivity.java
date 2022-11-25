package com.healthcare.dogtraining.ui.TRAININGCOURSE;

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
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Adapter.InstructionAdapter;
import com.healthcare.dogtraining.Model.InstructionModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

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
import static com.healthcare.dogtraining.API.Base_Url.plan_command_list_details;

public class InstructionsActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    String ID, command_id, command_name;
    List<InstructionModel> instructionModels = new ArrayList<>();
    InstructionAdapter adapter;
    TextView tv_name, tv_breed, tv_dob;
    CircleImageView profileimage;
    Session1 session1;
    ImageView backbtn;
    RelativeLayout progress_view;
    TextView cpommandname;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        recyclerview = findViewById(R.id.recyclerview);
        progress_view = findViewById(R.id.progress_view);
        tv_name = findViewById(R.id.tv_name);
        tv_breed = findViewById(R.id.tv_breed);
        tv_dob = findViewById(R.id.tv_dob);
        backbtn = findViewById(R.id.backbtn);
        cpommandname = findViewById(R.id.cpommandname);
        profileimage = findViewById(R.id.profileimage);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        session1 = new Session1(InstructionsActivity.this);
        if (getIntent() != null) {
            try {
                ID = getIntent().getStringExtra("ID");
                command_id = getIntent().getStringExtra("command_id");
                command_name = getIntent().getStringExtra("command_name");
                cpommandname.setText(command_name);
                System.out.println("id" + ID + command_id);

            } catch (Exception e) {
                System.out.println("chekc exc" + e.toString());
            }
        }
        GetInstructionApi();
        getprofileApi();
    }

    private void getprofileApi() {
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
                                    Glide.with(InstructionsActivity.this)
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

                                Toast.makeText(InstructionsActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InstructionsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
        rQueue = Volley.newRequestQueue(InstructionsActivity.this);
        rQueue.add(stringRequest);
    }


    private void GetInstructionApi() {
        Log.e("TAG", "GetInstructionApi: " + ID);
        Log.e("TAG", "GetInstructionApi: " + command_id);
        String url = BaseUrl + plan_command_list_details;
        AndroidNetworking.post(url)
                .addBodyParameter("id", ID)
                .addBodyParameter("command_id", command_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progress_view.setVisibility(View.INVISIBLE);
                        try {
                            instructionModels.clear();
                            String result = jsonObject.getString("result");

                            System.out.print("GetInstructionApi<><>===" + jsonObject);
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    InstructionModel allCommunityModel = new InstructionModel
                                            (dataObject.getString("id"),
                                                    dataObject.getString("command_details_id"),
                                                    dataObject.getString("title"),
                                                    dataObject.getString("instruction")

                                            );
                                    instructionModels.add(allCommunityModel);
                                }

                            } else {
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(InstructionsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            adapter = new InstructionAdapter(instructionModels, InstructionsActivity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(InstructionsActivity.this);
                            recyclerview.setLayoutManager(mLayoutManger);
                            recyclerview.setLayoutManager(new LinearLayoutManager(InstructionsActivity.this, RecyclerView.VERTICAL, false));
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progress_view.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            progress_view.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progress_view.setVisibility(View.INVISIBLE);
                        Log.e("error_my_join", anError.toString());
                    }
                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}