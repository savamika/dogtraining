package com.healthcare.dogtraining.ui.BREEDINFO;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
import com.healthcare.dogtraining.Adapter.ImagesAdapter;
import com.healthcare.dogtraining.Model.Imagemodel;
import com.healthcare.dogtraining.R;
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
import static com.healthcare.dogtraining.API.Base_Url.getBareed_details;

public class BridinfoDetailsActivity extends AppCompatActivity {
    ImageView iv_icon;
    TextView tv_name, tv_age, tv_weight, tv_description, tv_brred,tv_height;
    RecyclerView imagesrecyclerview;
    String id;
    List<Imagemodel> imagemodels = new ArrayList<>();
    ImagesAdapter adapter;
    CircleImageView circleimage;
    ImageView backbutton;
    RelativeLayout progress_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_bridinfo_details);


        progress_view = findViewById(R.id.progress_view);
        iv_icon = findViewById(R.id.iv_icon);
        tv_name = findViewById(R.id.tv_name);
        tv_age = findViewById(R.id.tv_age);
        tv_height = findViewById(R.id.tv_height);
        tv_weight = findViewById(R.id.tv_weight);
        tv_description = findViewById(R.id.tv_description);
        tv_brred = findViewById(R.id.tv_brred);
        imagesrecyclerview = findViewById(R.id.imagesrecyclerview);
        circleimage = findViewById(R.id.circleimage);
        backbutton = findViewById(R.id.backbutton);

        if (getIntent() != null) {
            try {
                id = getIntent().getStringExtra("id");
                System.out.println("<><===id" + id);
            } catch (Exception e) {
                System.out.println("chekc exc" + e.toString());
            }
        }
        GetBreedInfodetailsApi();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void GetBreedInfodetailsApi() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + getBareed_details,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            System.out.println("GetBreedInfodetailsApi<><=====   " + obj);
                            if (result.equals("true")) {
                                JSONObject userJson = obj.getJSONObject("data");
                                String id = userJson.getString("id");
                                String name = userJson.getString("name");
                                String age = userJson.getString("age");
                                String breed = userJson.getString("breed");
                                String weight = userJson.getString("weight");
                                String gender = userJson.getString("gender");
                                String description = userJson.getString("description");
                                String height = userJson.getString("height");
                                String status = userJson.getString("status");
                                String created_at = userJson.getString("created_at");
                                String updeted_at = userJson.getString("updeted_at");


                                tv_age.setText(age);
                                tv_weight.setText(weight);
                                tv_name.setText(name);
                                tv_description.setText(description);
                                tv_brred.setText(breed);
                                tv_height.setText(height);
                                tv_brred.setVisibility(View.GONE);
                                progress_view.setVisibility(View.INVISIBLE);
                                String image = userJson.getString("image");
                                JSONArray array = userJson.getJSONArray("image");
                                for (int i = 0; i < array.length(); i++) {

                                    if (array != null && array.length() > 0) {


                                        JSONObject relatedobject = array.getJSONObject(i);
                                        System.out.println("size_details" + array.toString());
                                        Imagemodel allCommunityModel = new Imagemodel(
                                                relatedobject.getString("id"),
                                                relatedobject.getString("bareed_id"),
                                                relatedobject.getString("images")
                                        );
                                        imagemodels.add(allCommunityModel);
                                    }

                                    adapter = new ImagesAdapter(imagemodels, BridinfoDetailsActivity.this);
                                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(BridinfoDetailsActivity.this);
                                    imagesrecyclerview.setLayoutManager(mLayoutManger);
                                    imagesrecyclerview.setLayoutManager(new LinearLayoutManager(BridinfoDetailsActivity.this, RecyclerView.HORIZONTAL, false));
                                    imagesrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                    imagesrecyclerview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    progress_view.setVisibility(View.INVISIBLE);

                                    Glide.with(BridinfoDetailsActivity.this)
                                            .load(categoryimagepasth + imagemodels.get(0).getImages())
                                            .apply(new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .dontAnimate()
                                                    .centerCrop()
                                                    .dontTransform())
                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                                            .skipMemoryCache(true)
                                            .apply(new RequestOptions().override(600, 200)).into(iv_icon);

                                    Glide.with(BridinfoDetailsActivity.this)
                                            .load(categoryimagepasth + imagemodels.get(0).getImages())
                                            .apply(new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .dontAnimate()
                                                    .centerCrop()
                                                    .dontTransform())
                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                                            .skipMemoryCache(true)
                                            .apply(new RequestOptions().override(600, 200)).into(circleimage);

                                    progress_view.setVisibility(View.INVISIBLE);

                                }

                            }
                        } catch (JSONException e) {
                            progress_view.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_view.setVisibility(View.INVISIBLE);
                        Toast.makeText(BridinfoDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        VolleySingleton.getInstance(BridinfoDetailsActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}