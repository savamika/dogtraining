package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.healthcare.dogtraining.Adapter.DogAccessoriesAdapter;
import com.healthcare.dogtraining.Adapter.GetTrainigAdapter;
import com.healthcare.dogtraining.Model.DogAccessoriesModel;
import com.healthcare.dogtraining.Model.Trainingpackagemodel;
import com.healthcare.dogtraining.ui.DOGACCESSORIES.DogAccessoriesActivity;
import com.healthcare.dogtraining.ui.MYPROFILE.MyprofileFragment;

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
import static com.healthcare.dogtraining.API.Base_Url.getCategory;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;
import static com.healthcare.dogtraining.API.Base_Url.get_Training;

public class TrainingPacageActivity extends AppCompatActivity {
    LinearLayout trainingpacage;
    List<Trainingpackagemodel>trainingpackagemodels=new ArrayList<>();
    GetTrainigAdapter getTrainigAdapter;
    RecyclerView recyclerview;
    Session1 session1;
    TextView tv_name,tv_pet_age,tv_dogbreed;
    CircleImageView userimage;
    private RequestQueue rQueue;
    ImageView backbtn;
    RelativeLayout progress_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_pacage);
        trainingpacage = findViewById(R.id.trainingpacage);
        recyclerview = findViewById(R.id.recyclerview);
        tv_name = findViewById(R.id.tv_name);
        progress_view = findViewById(R.id.progress_view);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_pet_age = findViewById(R.id.tv_pet_age);
        tv_dogbreed = findViewById(R.id.tv_dogbreed);
        userimage = findViewById(R.id.userrimage);
        session1 = new Session1(TrainingPacageActivity.this);
        tv_name.setText(session1.getUser_name());
        tv_pet_age.setText(session1.getDog_Age());

        //  getProfile();
        GetTraininglistApi();
    }

       /* userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyprofileFragment fragment = new MyprofileFragment();
                ((FragmentActivity)TrainingPacageActivity.this).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment)
                        .commit();
            // Intent intent=new Intent(TrainingPacageActivity.this,HomeActivity.class);
           //  startActivity(intent);
            }
            });
            }*/


            private void getProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl +getMyProfile,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject= new JSONObject(response);
                                String msg=jsonObject.getString("msg");

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

                                        Glide.with(TrainingPacageActivity.this)
                                                .load(Image_Path + image)
                                                .placeholder(R.drawable.dogprofile)
                                                .apply(new RequestOptions()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .dontAnimate()
                                                        .centerCrop()
                                                        .dontTransform())
                                                .skipMemoryCache(true)
                                                .into(userimage);
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
                          //  Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
            rQueue = Volley.newRequestQueue(TrainingPacageActivity.this);
            rQueue.add(stringRequest);


        }





            private void GetTraininglistApi() {
              // final ProgressDialog progressDialog = new ProgressDialog(TrainingPacageActivity.this);
              //  progressDialog.setTitle("Loading..");
               // progressDialog.show();
                String url = BaseUrl +get_Training;
                AndroidNetworking.get(url)
                        //.addBodyParameter("product_type","Accessories")
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                               // progressDialog.dismiss();
                                Log.e("getTRANNING", "onResponse: "+jsonObject.toString() );
                                try {
                                    trainingpackagemodels.clear();
                                    String result = jsonObject.getString("result");
                                    String msg = jsonObject.getString("msg");
                                    if (result.equalsIgnoreCase("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject dataObject = jsonArray.getJSONObject(i);
                                            Trainingpackagemodel allCommunityModel = new Trainingpackagemodel
                                                    (dataObject.getString("id"),
                                                            dataObject.getString("name"),
                                                            dataObject.getString("image"),
                                                            dataObject.getString("created_date"));
                                            trainingpackagemodels.add(allCommunityModel);

                                        }

                                    } else {
                                        progress_view.setVisibility(View.INVISIBLE);
                                    }
                                    getTrainigAdapter = new GetTrainigAdapter(trainingpackagemodels,TrainingPacageActivity.this);
                                   // RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(TrainingPacageActivity.this);
                                  //  recyclerview.setLayoutManager(mLayoutManger);
                                    recyclerview.setLayoutManager(new LinearLayoutManager(TrainingPacageActivity.this, RecyclerView.VERTICAL, false));
                                  //  recyclerview.setItemAnimator(new DefaultItemAnimator());
                                    recyclerview.setAdapter(getTrainigAdapter);
                                    recyclerview.setNestedScrollingEnabled(false);
                                    progress_view.setVisibility(View.INVISIBLE);
                                   // getTrainigAdapter.notifyDataSetChanged();

                                  } catch (JSONException e) {
                                    e.printStackTrace();
                                    progress_view.setVisibility(View.INVISIBLE);
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