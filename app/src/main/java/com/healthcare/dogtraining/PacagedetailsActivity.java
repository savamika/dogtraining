package com.healthcare.dogtraining;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Adapter.AdapterDogTrainingnamecourse;
import com.healthcare.dogtraining.Adapter.Command_model;
import com.healthcare.dogtraining.Adapter.CommandlistAdapter;
import com.healthcare.dogtraining.Adapter.Onitemclick;
import com.healthcare.dogtraining.Model.GetAllDataModel;
import com.healthcare.dogtraining.ui.ADOPTION.APIClient;
import com.healthcare.dogtraining.ui.ADOPTION.ApiService;
import com.healthcare.dogtraining.ui.MYPROFILE.MyprofileFragment;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;
import static com.healthcare.dogtraining.API.Base_Url.get_command_list;
import static com.healthcare.dogtraining.API.Base_Url.payfortraing;
import static java.lang.Float.parseFloat;

public class PacagedetailsActivity extends AppCompatActivity implements Onitemclick {
    private static final String TAG = "PacagedetailsActivity";
    Button Paynow;
    RelativeLayout progress_view;
    CircleImageView userimage;
    TextView tv_username, tv_breedname, tv_dob, tv_name, tv_selectcommands;
    Session1 session1;
    RecyclerView recyclerview;
    AdapterDogTrainingnamecourse adapterDogTrainingCoursename;
    String course_trining_id, select_command, name, price, trialid, allIds, coursetrainingid;
   public static List<String> ids = new ArrayList<String>();
    List<Command_model> command_modelslist = new ArrayList<Command_model>();
    ImageView backbtn;

    TextView titile;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacagedetails);
        progress_view = findViewById(R.id.progress_view);
        userimage = findViewById(R.id.userimage);
        tv_username = findViewById(R.id.tv_username);
        tv_breedname = findViewById(R.id.tv_breedname);
        tv_dob = findViewById(R.id.tv_dob);
        Paynow = findViewById(R.id.Paynow);
        recyclerview = findViewById(R.id.recyclerview);
        titile = findViewById(R.id.titile);
        tv_name = findViewById(R.id.tv_name);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_selectcommands = findViewById(R.id.tv_selectcommands);
        session1 = new Session1(PacagedetailsActivity.this);

        if (getIntent() != null) {
            course_trining_id = getIntent().getStringExtra("id");
            System.out.println("course_trining_id---***`----          " + course_trining_id);
            select_command = getIntent().getStringExtra("select_command");
            name = getIntent().getStringExtra("name");
            price = getIntent().getStringExtra("price");
            tv_name.setText(name);
            titile.setText(name);
            tv_selectcommands.setText(select_command);
        }

        //getProfile();
        getcommandlist(course_trining_id);

        Paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coursetrainingid != null) {
                    GetPaynowApi();
                } else {
                    Toast.makeText(PacagedetailsActivity.this, "No selection !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MyprofileFragment();
                FragmentManager fragmentManager = PacagedetailsActivity.this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void GetPaynowApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + payfortraing,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            System.out.println("<><>===" + obj);
                            if (result.equalsIgnoreCase("true")) {
                                String order_id = obj.getString("order_id");
                                JSONObject object=obj.getJSONObject("certificate_price");

                                String id = object.getString("id");
                                String certificate_price = object.getString("certificate_price");


                                float mTotalPrice;
                                mTotalPrice = parseFloat(price);
                                float mTestAmount = mTotalPrice;
                                session1.set_role("Purchase_package");
                                Intent intent = new Intent(PacagedetailsActivity.this, PlaceOrderActivity.class);
                                intent.putExtra("total", mTestAmount);
                                intent.putExtra("type", "Plan");
                                intent.putExtra("orderId", order_id);
                                intent.putExtra("certificate_price", certificate_price);
                                startActivity(intent);
                                finish();
                                progress_view.setVisibility(View.GONE);
                            } else {
                                progress_view.setVisibility(View.GONE);
                                Toast.makeText(PacagedetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progress_view.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_view.setVisibility(View.GONE);
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session1.getUserId());
                params.put("course_id", allIds);
                params.put("course_trainig_id", coursetrainingid);
                System.out.print("<<><====" + params);
                return params;
            }
        };

        VolleySingleton.getInstance(PacagedetailsActivity.this).addToRequestQueue(stringRequest);
    }


    public void getcommandlistApi(String course_trining_id) {
        Log.e("course_trining_id", "getcommandlistApi: " + course_trining_id);
        Log.e("course_trining_id", "getcommandlistApi: " + session1.getUserId());
        ApiService apiInterface = APIClient.getClient().create(ApiService.class);
        Call<GetAllDataModel> call = apiInterface.GetCommandList(session1.getUserId(), course_trining_id);
        call.enqueue(new Callback<GetAllDataModel>() {
            @Override
            public void onResponse(Call<GetAllDataModel> call, retrofit2.Response<GetAllDataModel> response) {
                Log.e("TAG", "onResponse: " + response.body().toString());
                try {
                    if (response != null) {
                        Log.e("TAG", "onResponse: " + response.body().getVaccinationTimeDatum().get(0).getCommand_name());
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            adapterDogTrainingCoursename = new AdapterDogTrainingnamecourse(response.body().getVaccinationTimeDatum(), PacagedetailsActivity.this, PacagedetailsActivity.this);
                            LinearLayoutManager manager = new LinearLayoutManager(PacagedetailsActivity.this, RecyclerView.VERTICAL, false);
                            recyclerview.setLayoutManager(manager);
                            recyclerview.setAdapter(adapterDogTrainingCoursename);
                            progress_view.setVisibility(View.GONE);
                        } else {
                            progress_view.setVisibility(View.GONE);
                            Toast.makeText(PacagedetailsActivity.this, "No list found", Toast.LENGTH_SHORT).show();

                        }
                    }

                } catch (Exception e) {
                    Log.e("catch error", e.getMessage());
                    progress_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetAllDataModel> call, Throwable t) {
                Log.e("error", t.getMessage());
                progress_view.setVisibility(View.GONE);
            }
        });
    }


    private void getProfile() {
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
                                    Glide.with(PacagedetailsActivity.this)
                                            .load(Image_Path + image)
                                            .placeholder(R.drawable.dogprofile)
                                            .apply(new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .dontAnimate()
                                                    .centerCrop()
                                                    .dontTransform())
                                            .skipMemoryCache(true)
                                            .into(userimage);
                                    tv_username.setText(fullname);
                                    tv_breedname.setText(pet_bareed);
                                    tv_dob.setText(pet_age);
                                } catch (Exception e) {
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
                System.out.println("id=======       " + session1.getUserId());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(PacagedetailsActivity.this);
        rQueue.add(stringRequest);
    }

    private void getcommandlist(String course_trining_id) {
        ids.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + get_command_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");

                            Log.e("TAAG", "get_command_list:------     "+ response);
                            if (jsonObject.optString("result").equals("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String course_trining_id = object.getString("course_trining_id");
                                    String Comanadname = object.getString("name");
                                    String plan_name = object.getString("plan_name");
                                    String select_command_limit = object.getString("select_command_limit");
                                    String is_all = object.getString("is_all");

                                    Command_model  command_model=new Command_model(Comanadname,course_trining_id,plan_name,id,select_command_limit,is_all);
                                    command_modelslist.add(command_model);
                                }
                                CommandlistAdapter commandlistAdapter = new CommandlistAdapter(command_modelslist, PacagedetailsActivity.this, PacagedetailsActivity.this);
                                RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(PacagedetailsActivity.this);
                                recyclerview.setLayoutManager(mLayoutManger);
                                recyclerview.setLayoutManager(new LinearLayoutManager(PacagedetailsActivity.this, RecyclerView.VERTICAL, false));
                                recyclerview.setItemAnimator(new DefaultItemAnimator());
                                recyclerview.setAdapter(commandlistAdapter);
                                commandlistAdapter.notifyDataSetChanged();
                                recyclerview.setItemViewCacheSize(command_modelslist.size());
                                progress_view.setVisibility(View.GONE);

                            } else {
                                progress_view.setVisibility(View.GONE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress_view.setVisibility(View.GONE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_view.setVisibility(View.GONE);

                        //  Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session1.getUserId());
                params.put("course_trining_id",course_trining_id);
                System.out.println("id=======       " + session1.getUserId());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(PacagedetailsActivity.this);
        rQueue.add(stringRequest);
    }


    @Override
    protected void onResume() {
        getProfile();
        super.onResume();
    }

    @Override
    public void Onitemclicktrial(String name, String course_trining_id) {
        coursetrainingid = course_trining_id;

        trialid = name;

        if (!ids.contains(trialid)) {
            ids.add(trialid);
            allIds = TextUtils.join(",", ids);
            // System.out.println("allIdsadd  "+ allIds);

        } else {
            Log.e(TAG, "Onitem  remove: ------     "+trialid );

            ids.remove(trialid);
            allIds = TextUtils.join(",", ids);
            Log.e(TAG, "Onitem  allIds: ------     "+allIds );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}