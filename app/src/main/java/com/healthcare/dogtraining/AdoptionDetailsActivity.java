package com.healthcare.dogtraining;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Adapter.ImagesAdapteradoptiondetails;
import com.healthcare.dogtraining.Model.GetImagesModel;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;
import static com.healthcare.dogtraining.API.Base_Url.getAdoptbyid;

public class AdoptionDetailsActivity extends AppCompatActivity {
    String ID;
    RecyclerView recyclerview;
    TextView tv_pet_name, tv_breedname, tv_age, tv_gender, tv_weight, tv_description, tv_adres,
            tv_mob;
    List<GetImagesModel> getImagesModels = new ArrayList<>();
    ImagesAdapteradoptiondetails adapter;
    CircleImageView dog_image;
    ImageView iv_icon;
    Button btn_adoption;
    String mobile;
    ImageView backbutton;
    RelativeLayout progress_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_details);
        backbutton = findViewById(R.id.backbutton);
        progress_view = findViewById(R.id.progress_view);
        tv_pet_name = findViewById(R.id.tv_pet_name);
        tv_breedname = findViewById(R.id.tv_breedname);
        tv_age = findViewById(R.id.tv_age);
        tv_gender = findViewById(R.id.tv_gender);
        tv_weight = findViewById(R.id.tv_weight);
        tv_description = findViewById(R.id.tv_description);
        recyclerview = findViewById(R.id.recyclerview);
        dog_image = findViewById(R.id.dog_image);
        iv_icon = findViewById(R.id.iv_icon);
        btn_adoption = findViewById(R.id.btn_adoption);
        tv_mob = findViewById(R.id.tv_mob);
        tv_adres = findViewById(R.id.tv_adres);
        if (getIntent() != null) {
            ID = getIntent().getStringExtra("ID");

            System.out.println("<><><====id" + ID);

        }
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GetAdoptByIdApi(ID);

        btn_adoption.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    call_action();

                }
            }
        });
    }

    private void call_action() {
        System.out.println("<><>mobile" + mobile);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobile));
        startActivity(callIntent);
    }

    private void GetAdoptByIdApi(String ID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + getAdoptbyid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progress_view.setVisibility(View.INVISIBLE);
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            System.out.println("<><=====" + obj);
                            if (result.equals("true")) {
                                JSONObject userJson = obj.getJSONObject("data");
                                String id = userJson.getString("id");
                                String user_id = userJson.getString("user_id");
                                String name = userJson.getString("name");
                                String age = userJson.getString("age");
                                String gender = userJson.getString("gender");
                                String breed = userJson.getString("breed");
                                String weight = userJson.getString("weight");
                                String description = userJson.getString("description");
                                String status = userJson.getString("status");
                                String created_at = userJson.getString("created_at");
                                String update_at = userJson.getString("update_at");
                                String adoption_address = userJson.getString("adoption_address");
                                String adoption_mobile = userJson.getString("adoption_mobile");
                                mobile = userJson.getString("mobile");


                                tv_age.setText(age);
                                tv_weight.setText(weight);
                                tv_pet_name.setText(name);
                                tv_description.setText(description);
                                tv_breedname.setText(breed);
                                tv_gender.setText(gender);
                                tv_adres.setText(adoption_address);
                                        tv_mob.setText(adoption_mobile);
                                progress_view.setVisibility(View.INVISIBLE);
                                String image = userJson.getString("image");
                                JSONArray array = userJson.getJSONArray("image");
                                for (int i = 0; i < array.length(); i++) {

                                    if (array != null && array.length() > 0) {
                                        JSONObject relatedobject = array.getJSONObject(i);
                                        System.out.println("size_details" + array.toString());
                                        GetImagesModel allCommunityModel = new GetImagesModel(
                                                relatedobject.getString("id"),
                                                relatedobject.getString("adop_id"),
                                                relatedobject.getString("images")
                                        );
                                        getImagesModels.add(allCommunityModel);
                                    }

                                    adapter = new ImagesAdapteradoptiondetails(getImagesModels, AdoptionDetailsActivity.this);
                                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(AdoptionDetailsActivity.this);
                                    recyclerview.setLayoutManager(mLayoutManger);
                                    recyclerview.setLayoutManager(new LinearLayoutManager(AdoptionDetailsActivity.this, RecyclerView.HORIZONTAL, false));
                                    recyclerview.setItemAnimator(new DefaultItemAnimator());
                                    recyclerview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();


                                    try {

                                        Glide.with(AdoptionDetailsActivity.this)
                                                .load(categoryimagepasth + getImagesModels.get(0).getImages())
                                                .apply(new RequestOptions()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .dontAnimate()
                                                        .centerCrop()
                                                        .dontTransform())
                                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                                                .skipMemoryCache(true)
                                                .apply(new RequestOptions().override(600, 200)).into(iv_icon);

                                        Glide.with(AdoptionDetailsActivity.this)
                                                .load(categoryimagepasth + getImagesModels.get(0).getImages())
                                                .apply(new RequestOptions()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .dontAnimate()
                                                        .centerCrop()
                                                        .dontTransform())
                                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                                                .skipMemoryCache(true)
                                                .apply(new RequestOptions().override(600, 200)).into(dog_image);


                                    } catch (Exception e) {
                                        progress_view.setVisibility(View.INVISIBLE);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress_view.setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_view.setVisibility(View.INVISIBLE);
                        Toast.makeText(AdoptionDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", ID);
                return params;
            }
        };

        VolleySingleton.getInstance(AdoptionDetailsActivity.this).addToRequestQueue(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
