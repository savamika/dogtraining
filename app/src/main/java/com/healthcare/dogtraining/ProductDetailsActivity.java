package com.healthcare.dogtraining;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.healthcare.dogtraining.Adapter.RelatedproductsAdapter;
import com.healthcare.dogtraining.Adapter.SlidingProfile_Adaptersingleimage;
import com.healthcare.dogtraining.Model.RelatedproductsModel;
import com.healthcare.dogtraining.gps.GPSTracker;
import com.healthcare.dogtraining.ui.Address.AddAddressActivity;
import com.healthcare.dogtraining.utils.VolleySingleton;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.add_to_cart;
import static com.healthcare.dogtraining.API.Base_Url.get_Accessories_details;
import static com.healthcare.dogtraining.API.Base_Url.related_product;

public class ProductDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final String TAG = "ProductDetailsActivity";
    private static final Integer[] IMAGES = {R.drawable.dogprofile, R.drawable.dogmartlogo};
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    LinearLayout CartProductlist;
    SliderLayout mDemoSilder;
    PageIndicator custom_indicator;
    TextView textviewprice, ed_mrp, tv_address, product_name, product_description, tv_price, tv_age, tv_colors;
    String id, cat_id;
    CirclePageIndicator indicator;
    ImageView iv_back;
    Session1 session1;
    TextView addcart, tv_username, tv_change;
    RecyclerView related_recycler;
    RelatedproductsAdapter relatedproductsAdapter;
    List<RelatedproductsModel> relatedproductsModels = new ArrayList<>();
    TextView relatedtext;
    RelativeLayout progress_view;
    private Double lat;
    private Double lng;
    private GPSTracker tracker;
    private final ArrayList<String> ImagesArray = new ArrayList<>();
    private final ArrayList<String> ImagesArray1 = new ArrayList<>();
TextView txtreturn;

String return_policy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        CartProductlist = findViewById(R.id.CartProductlist);
        progress_view = findViewById(R.id.progress_view);
        mPager = findViewById(R.id.pager);
        relatedtext = findViewById(R.id.relatedtext);
        indicator = findViewById(R.id.indicator);
        product_name = findViewById(R.id.product_name);
        product_description = findViewById(R.id.product_description);
        tv_price = findViewById(R.id.tv_price);
        tv_age = findViewById(R.id.tv_age);
        tv_colors = findViewById(R.id.tv_colors);
        tv_address = findViewById(R.id.tv_address);
        textviewprice = findViewById(R.id.textviewprice);
        iv_back = findViewById(R.id.iv_back);
        addcart = findViewById(R.id.addcart);
        ed_mrp = findViewById(R.id.ed_mrp);
        tv_username = findViewById(R.id.tv_username);
        tv_change = findViewById(R.id.tv_change);
        txtreturn = findViewById(R.id.txtreturn);
        related_recycler = findViewById(R.id.related_recycler);
        relatedtext.setVisibility(View.GONE);
        tracker = new GPSTracker(ProductDetailsActivity.this);
        LocationManager locationManager = (LocationManager) ProductDetailsActivity.this.getSystemService(LOCATION_SERVICE);
        session1 = new Session1(ProductDetailsActivity.this);

        tv_username.setText(" DELIVER TO " + session1.getUser_name());

        if (tracker.canGetLocation() == true) {
            lat = tracker.getLatitude();
            lng = tracker.getLongitude();
            Log.e("current_lat ", " " + lat);
            Log.e("current_Lon ", " " + lng);
            getAddress(lat, lng);
        } else if (tracker.canGetLocation() == false) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(ProductDetailsActivity.this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
            } else {
                showGPSDisabledAlertToUser();
            }
        }
        if (getIntent() != null) {
            try {

                id = getIntent().getStringExtra("id");
                //cat_id = getIntent().getStringExtra("cat_id");
                // System.out.println("<><===id"+ id +"<><====" + cat_id);

            } catch (Exception e) {
                System.out.println("chekc exc" + e.toString());
            }
        }


        txtreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(ProductDetailsActivity.this,"Return policy",return_policy);
            }
        });


        GetProductDetails(id);
        GetRelatedproductsApi(id);


        findViewById(R.id.CartProductlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallAddtoCartApi(id);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });









      /*  mDemoSilder = findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.shihtzu);
        file_maps.put("Big Bang Theory",R.drawable.shipord);
        file_maps.put("House of Cards",R.drawable.training1);
        //file_maps.put("Game of Thrones", R.drawable.game_of_thrones);


*/

       /* for (String name : file_maps.keySet()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(ProductDetailsActivity.this);
            defaultSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSilder.addSlider(defaultSliderView);

        }
        mDemoSilder.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSilder.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSilder.setCustomAnimation(new DescriptionAnimation());
        mDemoSilder.setDuration(4000);
        mDemoSilder.addOnPageChangeListener(this);
        mDemoSilder.getPagerIndicator().setDefaultIndicatorColor(R.color.colorPrimaryDark, R.color.colorPrimaryDark);
*/

    }

    private void GetRelatedproductsApi(String id) {

        String url = BaseUrl + related_product;
        AndroidNetworking.post(url)
                .addBodyParameter("product_id", id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progress_view.setVisibility(View.INVISIBLE);
                        try {
                            relatedproductsModels.clear();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            System.out.print("jsonobject<><>===relatedproducts" + jsonObject);
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    RelatedproductsModel allCommunityModel = new RelatedproductsModel
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
                                                    dataObject.getString("age"),
                                                    dataObject.getString("status"),
                                                    dataObject.getString("created_at"),
                                                    dataObject.getString("updated_at"),
                                                    dataObject.getString("description")
                                            );
                                    relatedproductsModels.add(allCommunityModel);
                                    progress_view.setVisibility(View.INVISIBLE);
                                }

                            } else {
                                relatedtext.setVisibility(View.GONE);
                                progress_view.setVisibility(View.INVISIBLE);
                            }
                            // in this method '2' represents number of columns to be displayed in grid view.
                            GridLayoutManager layoutManager = new GridLayoutManager(ProductDetailsActivity.this, 2);
                            relatedtext.setVisibility(View.VISIBLE);
                            relatedproductsAdapter = new RelatedproductsAdapter(relatedproductsModels, ProductDetailsActivity.this);
                            related_recycler.setLayoutManager(layoutManager);
                            related_recycler.setItemAnimator(new DefaultItemAnimator());
                            related_recycler.setAdapter(relatedproductsAdapter);
                            relatedproductsAdapter.notifyDataSetChanged();
                            progress_view.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            progress_view.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progress_view.setVisibility(View.INVISIBLE);
                        //Log.e("error_my_join", anError.toString());
                    }
                });
    }


    private void CallAddtoCartApi(String id) {
        System.out.println("<><productid" + id);
        System.out.println("<><userid" + session1.getUserId());
        String url = BaseUrl + add_to_cart;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", session1.getUserId())
                .addBodyParameter("product_id", id)
                .addBodyParameter("quantity", "1")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            System.out.println("<><><><===" + jsonObject);
                            if (result.equalsIgnoreCase("true")) {
                                Intent intent = new Intent(ProductDetailsActivity.this, CartlistActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                addcart.setText("Already added in your cart");
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


    private void getAddress(Double lat, Double lng) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(ProductDetailsActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            tv_address.setText(address);
            System.out.println("<><>====address" + address);
        } catch (Exception e) {

        }
    }

    private void showGPSDisabledAlertToUser() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ProductDetailsActivity.this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                ProductDetailsActivity.this.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    private void GetProductDetails(String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + get_Accessories_details,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG", "onResponse: " + response);
                        try {
                            progress_view.setVisibility(View.INVISIBLE);
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            if (result.equals("true")) {
                                JSONObject userJson = obj.getJSONObject("data");
                                String id = userJson.getString("id");
                                String cat_id = userJson.getString("cat_id");
                                String name = userJson.getString("name");
                                String price = userJson.getString("price");
                                String color = userJson.getString("color");

                                String status = userJson.getString("status");
                                String created_at = userJson.getString("created_at");
                                String updated_at = userJson.getString("updated_at");
                                String image = userJson.getString("image");
                                String image1 = userJson.getString("image1");
                                String image2 = userJson.getString("image2");
                                String image3 = userJson.getString("image3");
                                String image4 = userJson.getString("image4");
                                  return_policy = userJson.getString("return_policy");
                                String description = userJson.getString("description");
                                String discounted_price = userJson.getString("discounted_price");
                                Log.e(TAG, "return_policy:-=-=-=-=-=-==-=     "+return_policy );
                                product_name.setText(name);
                                product_description.setText(description);
                                tv_price.setText("₹" + discounted_price);

                                tv_colors.setText(color);
                                textviewprice.setText("₹" + discounted_price);

                                if (price.equals(discounted_price)) {
                                    ed_mrp.setVisibility(View.GONE);

                                } else {

                                    if (price != null && !price.isEmpty() && !price.equals("null") && !price.equals("0")) {
                                        ed_mrp.setText("₹" + price);
                                        ed_mrp.setPaintFlags(ed_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                    }else {
                                        ed_mrp.setVisibility(View.GONE);
                                    }

                                }

                                if (image.equals(" ")) {

                                } else {

                                    ImagesArray.add(image);
                                    ImagesArray1.add(id);
                                }

                                if (image1.equals(" ")) {

                                } else {
                                    ImagesArray.add(image1);
                                    ImagesArray1.add(id);
                                }
                                if (image2.equals(" ")) {

                                } else {
                                    ImagesArray.add(image2);
                                    ImagesArray1.add(id);
                                }
                                if (image3.equals(" ")) {

                                } else {
                                    ImagesArray.add(image3);
                                    ImagesArray1.add(id);
                                }
                                if (image4.equals(" ")) {

                                } else {
                                    ImagesArray.add(image4);
                                    ImagesArray1.add(id);
                                }
                            }

                            mPager.setAdapter(new SlidingProfile_Adaptersingleimage(ProductDetailsActivity.this, ImagesArray, ImagesArray1));
                            indicator.setViewPager(mPager);
                            mPager.setCurrentItem(0);

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
                        Toast.makeText(ProductDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        VolleySingleton.getInstance(ProductDetailsActivity.this).addToRequestQueue(stringRequest);

        final float density = getResources().getDisplayMetrics().density;


        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public static void showAlertDialog(final Context context, String title, String message) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // setting Dialog title
        alertDialog.setTitle(title);

        // setting Dialog message
        alertDialog.setMessage(message);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

}
