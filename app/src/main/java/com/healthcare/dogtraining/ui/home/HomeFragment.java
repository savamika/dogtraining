package com.healthcare.dogtraining.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.healthcare.dogtraining.Adapter.AdapterNearBY;
import com.healthcare.dogtraining.Adapter.AdapterNearbyModel;
import com.healthcare.dogtraining.Adapter.DogMArtAdapter;

import com.healthcare.dogtraining.DogcategorylistActivity;
import com.healthcare.dogtraining.HomeActivity;
import com.healthcare.dogtraining.Model.BannerImage;
import com.healthcare.dogtraining.Model.DogMArtModelMain;
import com.healthcare.dogtraining.Model.DogMartModel;
import com.healthcare.dogtraining.Model.HomeSlidermodel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.VendorSignup.AddVendorActivity;
import com.healthcare.dogtraining.ui.ADOPTION.AdoptionFragment;
import com.healthcare.dogtraining.ui.BREEDINFO.BreedinfoFragment;
import com.healthcare.dogtraining.ui.DOGACCESSORIES.DogAccessoriesActivity;
import com.healthcare.dogtraining.ui.DOGACCESSORIES.DogaccessoriesFragment;
import com.healthcare.dogtraining.ui.DOGMART.DogmartFragment;
import com.healthcare.dogtraining.ui.MYPROFILE.MyprofileFragment;
import com.healthcare.dogtraining.ui.NEWSFEEDS.NewsfeedFragment;
import com.healthcare.dogtraining.ui.TRAININGCOURSE.TtainingcourseFragment;
import com.healthcare.dogtraining.ui.YOUTUBE.YoutubeFragment;
import com.healthcare.dogtraining.ui.chat.ChatActivity;
import com.healthcare.dogtraining.ui.firebase.NotificationActivity;
import com.healthcare.dogtraining.utils.VolleySingleton;
import com.viewpagerindicator.CirclePageIndicator;

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
import static com.healthcare.dogtraining.API.Base_Url.Sldier_url;
import static com.healthcare.dogtraining.API.Base_Url.getCategory;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;
import static com.healthcare.dogtraining.API.Base_Url.get_home_slider;
import static com.healthcare.dogtraining.API.Base_Url.gethomeslider;
import static com.healthcare.dogtraining.API.Base_Url.getmartlist;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    LinearLayout Dogtraining, l_acessories, NEWSFEED, DOMART, l_breedinfo, l_freeadoption, l_youtube, l_chat;
    Session1 session1;
    private RequestQueue rQueue;
    CircleImageView profileimage;
    TextView tv_name, tv_pet_name, tv_pet_age, viewmore;
    RelativeLayout progress_view;
  //  private static ViewPager mPager;
    CirclePageIndicator indicator;
    List<DogMArtModelMain> dogMArtModelMainList = new ArrayList<>();
    int currentPage = 0;
    ArrayList<SlideModel> slideModelArrayList = new ArrayList<>();
    ///////////////////slider///////////////////
    ArrayList arraylist = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrayList_image = new ArrayList<>();
    HashMap<String, String> map = new HashMap<String, String>();
    public ArrayList<BannerImage> hero = new ArrayList<>();
    ArrayList<HomeSlidermodel> SliderModelArrayList = new ArrayList<>();
  //  HomeSlider_adapter sladapter;
    TextView tvViewAllNewArrival;
    TextView viewmoremart;
    ImageSlider image_slider;
    RecyclerView rvNearByProduct;
    RecyclerView rvNewArrival;
    AdapterNearBY adapterNearBY;
    AdapterNewArrrival adapterNewArrrival;
    ArrayList<AdapterNearbyModel> nearbyModelArrayList = new ArrayList<>();
    ArrayList<AdapterccesModel> accesorieslist = new ArrayList<>();
    TextView startSelling;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvViewAllNewArrival = root.findViewById(R.id.tvViewAllNewArrival);
        profileimage = root.findViewById(R.id.profileimage);
        rvNewArrival = root.findViewById(R.id.rvNewArrival);
        DOMART = root.findViewById(R.id.DOMART);
        tv_name = root.findViewById(R.id.tv_name);
        tv_pet_name = root.findViewById(R.id.tv_pet_name);
        tv_pet_age = root.findViewById(R.id.tv_pet_age);
        Dogtraining = root.findViewById(R.id.Dogtraining);
        NEWSFEED = root.findViewById(R.id.NEWSFEED);
        progress_view = root.findViewById(R.id.progress_view);
        NEWSFEED = root.findViewById(R.id.NEWSFEED);
        l_breedinfo = root.findViewById(R.id.l_breedinfo);
        l_freeadoption = root.findViewById(R.id.l_freeadoption);
        l_youtube = root.findViewById(R.id.l_youtube);
        l_chat = root.findViewById(R.id.l_chat);
        l_acessories = root.findViewById(R.id.l_acessories);
        viewmoremart = root.findViewById(R.id.viewmoremart);
        rvNearByProduct = root.findViewById(R.id.rvNearByProduct);
        startSelling = root.findViewById(R.id.startSelling);
        session1 = new Session1(getActivity());
     //   mPager = (ViewPager) root.findViewById(R.id.mPager);
        indicator = (CirclePageIndicator) root.findViewById(R.id.indicator);
        image_slider = root.findViewById(R.id.image_slider);
        /*getProfile();*/
        GetHomeBanner();
       // getimage();
        nearbyModelArrayList.clear();
        accesorieslist.clear();
        // GetDogMartApi();
        getMartData();
        getAccesrieslist();
        Dogtraining.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new TtainingcourseFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });
        l_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("planname", "Chat");
                    intent.putExtra("command_id", "Help");
                intent.putExtra("plan_order_id", "Help");
                startActivity(intent);
                getActivity().finish();
              /*  Fragment fragment = new CHAT();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        });
        l_freeadoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AdoptionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        NEWSFEED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NewsfeedFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        startSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddVendorActivity.class);
                startActivity(intent);
            }
        });
        l_breedinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BreedinfoFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        l_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new YoutubeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        tvViewAllNewArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DogcategorylistActivity.class);
                intent.putExtra("from", "newArrival");
                startActivity(intent);

            }
        });



        DOMART.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), DogcategorylistActivity.class);
                intent.putExtra("from", "mart");
                startActivity(intent);

            }
        });
        l_acessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DogcategorylistActivity.class);
                intent.putExtra("from", "newArrival");
                startActivity(intent);

            }
        });


        return root;

    }

    private void GetDogMartApi() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String url = BaseUrl + getmartlist;
        AndroidNetworking.get(url)

                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            dogMArtModelMainList.clear();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    DogMArtModelMain allCommunityModel = new DogMArtModelMain
                                            (dataObject.getString("id"),
                                                    dataObject.getString("cat_id"),
                                                    dataObject.getString("vendor_id"),
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
                                                    dataObject.getString("description"),
                                                    dataObject.getString("category_id"),
                                                    dataObject.getString("product_type")
                                            );
                                    dogMArtModelMainList.add(allCommunityModel);
                                }

                            } else {

                            }
                               /* dogMArtAdapter = new DogMArtAdapter(dogMartModels,DogcategorylistActivity.this);
                                RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(DogcategorylistActivity.this);
                                recyclerview.setLayoutManager(mLayoutManger);
                                recyclerview.setLayoutManager(new GridLayoutManager(DogcategorylistActivity.this,2));
                                recyclerview.setItemAnimator(new DefaultItemAnimator());
                                recyclerview.setAdapter(dogMArtAdapter);
                                dogMArtAdapter.notifyDataSetChanged();*/
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


    private void getMartData() {
        progress_view.setVisibility(View.VISIBLE);
        nearbyModelArrayList.clear();
        String url = BaseUrl + "getmartlist";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getmartlist ", response.toString());


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("jsonarray", heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                String id = heroObject.getString("id");
                                Log.e("id", "onResponse: " + id);
                                String cat_id = heroObject.getString("cat_id");
                                String vendor_id = heroObject.getString("vendor_id");
                                String name = heroObject.getString("name");
                                String price = heroObject.getString("price");
                                String discounted_price = heroObject.getString("discounted_price");
                                String discounted_percentage = heroObject.getString("discounted_percentage");
                                String image = heroObject.getString("image");
                                String image1 = heroObject.getString("image1");
                                String image2 = heroObject.getString("image2");
                                String image3 = heroObject.getString("image3");
                                String image4 = heroObject.getString("image4");
                                String color = heroObject.getString("color");
                                String age = heroObject.getString("age");
                                String status = heroObject.getString("status");
                                String created_at = heroObject.getString("created_at");
                                String updated_at = heroObject.getString("updated_at");
                                String description = heroObject.getString("description");
                                String category_id = heroObject.getString("category_id");
                                String product_type = heroObject.getString("product_type");
                                Log.e("price", "onResponse: " + price + discounted_price);


                                nearbyModelArrayList.add(new AdapterNearbyModel(id, cat_id, vendor_id, name, price, discounted_price, discounted_percentage, image, image1, image2, image3, image4, color, age, status, created_at, updated_at, description, category_id, product_type));

                            }

                            AdapterNearBY adapter = new AdapterNearBY(nearbyModelArrayList, getActivity());
                            rvNearByProduct.setAdapter(adapter);

                            LinearLayoutManager linearLayoutManager
                                    = new LinearLayoutManager(
                                    getActivity(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false);
                            // at last set adapter to recycler view.
                            rvNearByProduct.setLayoutManager(linearLayoutManager);
                            rvNearByProduct.setAdapter(adapter);
                            progress_view.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            progress_view.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_view.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void getAccesrieslist() {
        progress_view.setVisibility(View.VISIBLE);

        accesorieslist.clear();
        String url = BaseUrl + "getaccesriselist";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("bannerresponse", response.toString());
                        accesorieslist.clear();

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.e(TAG, " :res "+response.toString() );
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                String id = heroObject.getString("id");
                                String cat_id = heroObject.getString("cat_id");
                                String vendor_id = heroObject.getString("vendor_id");
                                String name = heroObject.getString("name");
                                String price = heroObject.getString("price");
                                String discounted_price = heroObject.getString("discounted_price");
                                String discounted_percentage = heroObject.getString("discounted_percentage");
                                String image = heroObject.getString("image");
                                String image1 = heroObject.getString("image1");
                                String image2 = heroObject.getString("image2");
                                String image3 = heroObject.getString("image3");
                                String image4 = heroObject.getString("image4");
                                String color = heroObject.getString("color");
                                String age = heroObject.getString("age");
                                String status = heroObject.getString("status");
                                String created_at = heroObject.getString("created_at");
                                String updated_at = heroObject.getString("updated_at");
                                String description = heroObject.getString("description");
                                String category_id = heroObject.getString("category_id");
                                String product_type = heroObject.getString("product_type");


                                accesorieslist.add(new AdapterccesModel(id, cat_id, vendor_id, name, price, discounted_price, discounted_percentage, image, image1, image2, image3, image4, color, age, status, created_at, updated_at, description, category_id, product_type));

                            }

                            AdapterNewArrrival adapterNewArrrival = new AdapterNewArrrival(accesorieslist, getActivity());
                            // rvNearByProduct.setAdapter(adapterNewArrrival);

                            LinearLayoutManager linearLayoutManager
                                    = new LinearLayoutManager(
                                    getActivity(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false);
                            // at last set adapter to recycler view.
                            rvNewArrival.setLayoutManager(linearLayoutManager);
                            rvNewArrival.setAdapter(adapterNewArrrival);
                            progress_view.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            progress_view.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_view.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void getimage() {
        String url = BaseUrl + gethomeslider;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("bannerresponse", response.toString());
                        System.out.println("bannerr response  " + response.toString());

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("jsonarray", heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                arrayList_image.add(heroObject.getString("image"));

                                map.put(heroObject.getString("id"), heroObject.getString("image"));

                                arraylist.add(map);

                                //creating a hero object and giving them the values from json object
                                hero.add(i, new BannerImage(heroObject.getString("id"), heroObject.getString("image")

                                ));

                                HomeSlidermodel homeSlidermodel = new HomeSlidermodel(heroObject.getString("id"), heroObject.getString("image"));
                                SliderModelArrayList.add(homeSlidermodel);


                            }
                            for (String name : map.keySet()) {

                            }
                         //   sladapter = new HomeSlider_adapter(getActivity(), SliderModelArrayList);
                         //   mPager.setAdapter(sladapter);
                         //   indicator.setViewPager(mPager);
                          //  mPager.setCurrentItem(0);
                         //   sladapter.setTimer(mPager, 5);

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onResume() {
        /*getProfile();*/

        super.onResume();
    }

    public void GetHomeBanner() {

        slideModelArrayList.clear();

        AndroidNetworking.get(BaseUrl + get_home_slider)

                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        Log.e("Signup", "Signup: " + jsonObject);
                        try {
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");

                                    String image = object.getString("image");
                                    SlideModel slideModel = new SlideModel(Sldier_url + image, ScaleTypes.FIT);
                                    slideModelArrayList.add(slideModel);
                                }

                                image_slider.setImageList(slideModelArrayList, ScaleTypes.FIT);


                            } else {
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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

}