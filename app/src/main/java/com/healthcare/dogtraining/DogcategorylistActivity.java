package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.Adapter.DogMArtAdapter;
import com.healthcare.dogtraining.Model.DogMartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.checkaddtocart;
import static com.healthcare.dogtraining.API.Base_Url.getCategory;
import static com.healthcare.dogtraining.API.Base_Url.get_countcart;

public class DogcategorylistActivity extends AppCompatActivity {
    LinearLayout Productlist;
    RelativeLayout cartlist;
    String type = "";
    List<DogMartModel> dogMartModels = new ArrayList<>();
    DogMArtAdapter dogMArtAdapter;
    RecyclerView recyclerview;
    ImageView iv_back;
    Session1 session1;
    TextView tv_countcart, titletext;
    androidx.appcompat.widget.SearchView searchView;
    RelativeLayout progress_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogcategorylist);
        progress_view= findViewById(R.id.progress_view);
        if (getIntent() != null) {
            String from = getIntent().getStringExtra("from");
            if (from.equalsIgnoreCase("newArrival")) {
                type = "Accessories";
                GetDogMArtApi(type);
            }else{
                type = "Mart";
                GetDogMArtApi(type);
            }

        }else{

        }
        titletext = findViewById(R.id.titletext);
        searchView = findViewById(R.id.searchView);
        cartlist = findViewById(R.id.cartlist);
        recyclerview = findViewById(R.id.recyclerview);
        iv_back = findViewById(R.id.iv_back);
        tv_countcart = findViewById(R.id.tv_countcart);
        session1 = new Session1(DogcategorylistActivity.this);
        Searchfuncition();
        GetcountcartApi();


/*

        Productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogcategorylistActivity.this, ProductlistActivity.class);
                startActivity(intent);
                finish();
            }
        });
*/


        cartlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallCartStatusApi();

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void Searchfuncition() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titletext.setVisibility(View.GONE);
                iv_back.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titletext.setVisibility(View.VISIBLE);
                iv_back.setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("TAG", "onQueryTextSubmit: " + query.length());
                dogMArtAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("TAG", "onQueryTextSubmit: " + newText.length());

                dogMArtAdapter.getFilter().filter(newText);
                return false;
            }
        });
        // listening to search query text change

    }

    private void CallCartStatusApi() {
        String url = BaseUrl + checkaddtocart;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", session1.getUserId())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                String data = jsonObject.getString("data");
                                Intent intent = new Intent(DogcategorylistActivity.this, CartlistActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("error_my_join", anError.toString());
                    }
                });
    }


    private void GetcountcartApi() {
        String url = BaseUrl + get_countcart;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", session1.getUserId())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                String data = jsonObject.getString("data");
                                tv_countcart.setText(data);
                            } else {
                                tv_countcart.setText("0");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("error_my_join", anError.toString());
                    }
                });
    }


    private void GetDogMArtApi(String type) {

        String url = BaseUrl + getCategory;
        AndroidNetworking.post(url)
                .addBodyParameter("product_type", type)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progress_view.setVisibility(View.INVISIBLE);
                        try {
                            dogMartModels.clear();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    DogMartModel allCommunityModel = new DogMartModel
                                            (dataObject.getString("id"),
                                                    dataObject.getString("name"),
                                                    dataObject.getString("product_type"),
                                                    dataObject.getString("image"),
                                                    dataObject.getString("status"),
                                                    dataObject.getString("created_at"),
                                                    dataObject.getString("updated_at")
                                            );
                                    dogMartModels.add(allCommunityModel);
                                }

                            } else {

                            }
                            dogMArtAdapter = new DogMArtAdapter(dogMartModels, DogcategorylistActivity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(DogcategorylistActivity.this);
                            recyclerview.setLayoutManager(mLayoutManger);
                            recyclerview.setLayoutManager(new GridLayoutManager(DogcategorylistActivity.this, 2));
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setAdapter(dogMArtAdapter);
                            dogMArtAdapter.notifyDataSetChanged();
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


    @Override
    protected void onResume() {
        GetDogMArtApi(type);
        GetcountcartApi();
        super.onResume();
    }
}
