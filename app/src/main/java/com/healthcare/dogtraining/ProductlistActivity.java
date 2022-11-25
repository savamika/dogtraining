package com.healthcare.dogtraining;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.healthcare.dogtraining.Adapter.AllProductSortListAdapter;
import com.healthcare.dogtraining.Adapter.DogAccesoriescatByidAdapter;
import com.healthcare.dogtraining.Model.GetCategoryByidModel;
import com.healthcare.dogtraining.Model.SortProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.checkaddtocart;
import static com.healthcare.dogtraining.API.Base_Url.getProductsorting;
import static com.healthcare.dogtraining.API.Base_Url.get_category_byId;
import static com.healthcare.dogtraining.API.Base_Url.get_countcart;

public class ProductlistActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    LinearLayout Productdetails, filter;
    String cat_id, product_type;
    List<GetCategoryByidModel> getCategoryByidModels = new ArrayList<>();
    DogAccesoriescatByidAdapter adapter;
    RecyclerView recyclerview;
    TextView tv_producttype, tv_countcart;
    ImageView iv_back;
    Session1 session1;
    RelativeLayout cartlist;
    String sort_type;
    List<SortProductModel> sortProductModels = new ArrayList<>();
    AllProductSortListAdapter adapter1;
    SearchView searchview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        Productdetails = findViewById(R.id.Productdetails);
        recyclerview = findViewById(R.id.recyclerview);
        tv_producttype = findViewById(R.id.tv_producttype);
        iv_back = findViewById(R.id.iv_back);
        tv_countcart = findViewById(R.id.tv_countcart);
        cartlist = findViewById(R.id.cartlist);
        filter = findViewById(R.id.filter);
        searchview = findViewById(R.id.searchview);
        searchview.setOnQueryTextListener(this);
        session1 = new Session1(ProductlistActivity.this);

        if (getIntent() != null) {
            try {
                cat_id = getIntent().getStringExtra("cat_id");
                product_type = getIntent().getStringExtra("Product_Type");
                System.out.println("<><===catid" + cat_id);
                tv_producttype.setText(product_type);
                Log.e("tv_producttype", "onCreate: " + product_type);
            } catch (Exception e) {
                System.out.println("chekc exc" + e.toString());
            }
        }

        GetcountcartApi();
        //
        GetProductCategoryByidApi(cat_id);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCartStatusApi();

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ProductlistActivity.this);
                View sheetView = mBottomSheetDialog.getLayoutInflater().inflate(R.layout.bottom_layout, null);
                mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mBottomSheetDialog.setContentView(sheetView);
                LinearLayout a_to_z_res = mBottomSheetDialog.findViewById(R.id.a_to_z_res);
                TextView lowtohigh = mBottomSheetDialog.findViewById(R.id.lowtohigh);
                TextView high_to_low = mBottomSheetDialog.findViewById(R.id.high_to_low);

                a_to_z_res.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sort_type = "alphabetical";
                        ProductAPI(sort_type, cat_id);
                        mBottomSheetDialog.dismiss();
                    }
                });


                lowtohigh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sort_type = "low_to_high";
                        ProductAPI(sort_type, cat_id);
                        mBottomSheetDialog.dismiss();
                    }
                });

                high_to_low.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sort_type = "high_to_low";
                        ProductAPI(sort_type, cat_id);
                        mBottomSheetDialog.dismiss();
                    }
                });

                mBottomSheetDialog.show();
            }
        });
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
                                Intent intent = new Intent(ProductlistActivity.this, CartlistActivity.class);
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


    private void ProductAPI(String sort_type, String cat_id) {
        final ProgressDialog progressDialog = new ProgressDialog(ProductlistActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String url = BaseUrl + getProductsorting;
        AndroidNetworking.post(url)
                .addBodyParameter("cat_id", cat_id)
                .addBodyParameter("filter", sort_type)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            sortProductModels.clear();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                System.out.println("<><===" + jsonObject);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    SortProductModel allCommunityModel = new SortProductModel(
                                            dataObject.getString("id"),
                                            dataObject.getString("cat_id"),
                                            dataObject.getString("name"),
                                            dataObject.getString("price"),
                                            dataObject.getString("discounted_price"),
                                            dataObject.getString("discounted_percentage"),
                                            dataObject.getString("image"),
                                            dataObject.getString("image1"),
                                            dataObject.getString("image2"),
                                            dataObject.getString("image3"),
                                            dataObject.getString("image4"),
                                            dataObject.getString("color"),
                                            dataObject.getString("age"),
                                            dataObject.getString("status"),
                                            dataObject.getString("created_at"),
                                            dataObject.getString("updated_at"),
                                            dataObject.getString("description")
                                    );
                                    sortProductModels.add(allCommunityModel);
                                }
                            } else {


                                // nodata.setVisibility(View.VISIBLE);
                                recyclerview.setVisibility(View.GONE);
                            }

                            adapter1 = new AllProductSortListAdapter(sortProductModels, ProductlistActivity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ProductlistActivity.this);
                            recyclerview.setLayoutManager(mLayoutManger);
                            recyclerview.setLayoutManager(new GridLayoutManager(ProductlistActivity.this, 2));
                            // recyclerview.setLayoutManager(new LinearLayoutManager(ProductlistActivity.this, RecyclerView.VERTICAL, false));
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();

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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }


    private void GetProductCategoryByidApi(String cat_id) {
        System.out.println("<<><><===cat_id" + cat_id);
        System.out.println("<<><><===user_id" + session1.getUserId());
        final ProgressDialog progressDialog = new ProgressDialog(ProductlistActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String url = BaseUrl + get_category_byId;
        AndroidNetworking.post(url)
                .addBodyParameter("cat_id", cat_id)
                .addBodyParameter("user_id", session1.getUserId())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            getCategoryByidModels.clear();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    GetCategoryByidModel allCommunityModel = new GetCategoryByidModel
                                            (dataObject.getString("id"),
                                                    dataObject.getString("cat_id"),
                                                    dataObject.getString("name"),
                                                    dataObject.getString("price"),
                                                    dataObject.getString("discounted_price"),
                                                    dataObject.getString("discounted_percentage"),
                                                    dataObject.getString("image"),
                                                    dataObject.getString("image1"),
                                                    dataObject.getString("image2"),
                                                    dataObject.getString("image3"),
                                                    dataObject.getString("image4"),
                                                    dataObject.getString("color"),
                                                   "",
                                                    dataObject.getString("status"),
                                                    dataObject.getString("created_at"),
                                                    dataObject.getString("updated_at"),
                                                    dataObject.getString("description"),
                                                    dataObject.getString("total_cart")
                                            );
                                    getCategoryByidModels.add(allCommunityModel);
                                }

                            } else {

                                Toast.makeText(ProductlistActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                            adapter = new DogAccesoriescatByidAdapter(getCategoryByidModels, ProductlistActivity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ProductlistActivity.this);
                            recyclerview.setLayoutManager(mLayoutManger);
                            recyclerview.setLayoutManager(new LinearLayoutManager(ProductlistActivity.this, RecyclerView.VERTICAL, false));
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
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<GetCategoryByidModel> getCategoryByidModels1 = new ArrayList<>();
        for (GetCategoryByidModel getCategoryByidModel : getCategoryByidModels) {
            String name = getCategoryByidModel.getName().toLowerCase();
            if (name.contains(newText))
                getCategoryByidModels1.add(getCategoryByidModel);
        }
        adapter.setFilter(getCategoryByidModels1);
        return true;
    }

    @Override
    protected void onResume() {
        GetcountcartApi();
        GetProductCategoryByidApi(cat_id);
        // ProductAPI(sort_type,cat_id);
        super.onResume();
    }

}