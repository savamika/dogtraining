package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.healthcare.dogtraining.Adapter.ImagesAdapteradoptiondetails;
import com.healthcare.dogtraining.Model.GetImagesModel;
import com.healthcare.dogtraining.ui.MYPROFILE.MyprofileFragment;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;
import static com.healthcare.dogtraining.API.Base_Url.getAdoptbyid;
import static com.healthcare.dogtraining.API.Base_Url.getCourseTraingById;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;

public class PacageListActivity extends AppCompatActivity {
    private static final String TAG = "PacageListActivity";
    Button pacagedetails, back;
    String ID;
    TextView tv_price, tv_validity, tv_name, username, tv_breed, tv_dob,
            tv_email, tv_commands, tv_chatsupport, tv_queries, tv_callsupport, tv_certificate;
    private RequestQueue rQueue;
    Session1 session1;
    CircleImageView userimage;
    String id, name, select_command, price;
    ImageView backbtn;
    TextView titile;
    RelativeLayout progress_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacage_list);
        progress_view = findViewById(R.id.progress_view);
        back = findViewById(R.id.back);
        pacagedetails = findViewById(R.id.pacagedetails);
        backbtn = findViewById(R.id.backbtn);
        titile = findViewById(R.id.titile);
        tv_price = findViewById(R.id.tv_price);
        tv_validity = findViewById(R.id.tv_validity);
        tv_name = findViewById(R.id.tv_name);
        userimage = findViewById(R.id.userimage);
        username = findViewById(R.id.username);
        tv_breed = findViewById(R.id.tv_breed);
        tv_dob = findViewById(R.id.tv_dob);
        tv_commands = findViewById(R.id.tv_commands);
        tv_chatsupport = findViewById(R.id.tv_chatsupport);
        tv_queries = findViewById(R.id.tv_queries);
        tv_email = findViewById(R.id.tv_email);
        tv_callsupport = findViewById(R.id.tv_callsupport);
        tv_certificate = findViewById(R.id.tv_certificate);
        session1 = new Session1(PacageListActivity.this);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent() != null) {
            ID = getIntent().getStringExtra("ID");
            System.out.println("PacageListActivity<><====ID " + ID);
        }

        GetcourseTranigByid();
        getProfile();
        tv_breed.setText(session1.getDog_breed());
        tv_dob.setText(session1.getDog_Age());


        pacagedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id != null) {
                    Intent intent = new Intent(PacageListActivity.this, PacagedetailsActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("select_command", select_command);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
            }
        });
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MyprofileFragment();
                FragmentManager fragmentManager = PacageListActivity.this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PacageListActivity.this, TrainingPacageActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getProfile() {
        System.out.println("<><===userid" + session1.getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + getMyProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        Log.e(TAG, "onResponse:---------      "+response );
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String msg = jsonObject.getString("msg");
                            System.out.println("<><===getprofile " + jsonObject);
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


                                    Glide.with(PacageListActivity.this)
                                            .load(Image_Path + image)
                                            .placeholder(R.drawable.dogprofile)
                                            .apply(new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .dontAnimate()
                                                    .centerCrop()
                                                    .dontTransform())
                                            .skipMemoryCache(true)
                                            .into(userimage);

                                    username.setText(fullname);
                                    progress_view.setVisibility(View.INVISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    progress_view.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                progress_view.setVisibility(View.INVISIBLE);
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
        rQueue = Volley.newRequestQueue(PacageListActivity.this);
        rQueue.add(stringRequest);


    }


    private void GetcourseTranigByid() {
        final ProgressDialog progressDialog = new ProgressDialog(PacageListActivity.this);
     //   progressDialog.setTitle("Loading..");
       // progressDialog.show();
      //  progressDialog.dismiss();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + getCourseTraingById,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                          //  progressDialog.dismiss();
                            JSONObject obj = new JSONObject(response);
                            System.out.println("<><=getCourseTraingById===response=     " + response);

                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            System.out.println("<><=getCourseTraingById====     " + obj);
                            if (result.equals("true")) {
                                JSONObject userJson = obj.getJSONObject("data");
                                id = userJson.getString("id");
                                name = userJson.getString("name");
                                price = userJson.getString("price");
                                String validity = userJson.getString("validity");
                                String description = userJson.getString("description");
                                select_command = userJson.getString("select_command");
                                String queries = userJson.getString("queries");
                                String chat = userJson.getString("chat");
                                String email = userJson.getString("email");
                                String call_suppoprt = userJson.getString("call_suppoprt");
                                String certificate = userJson.getString("certificate");
                                String image = userJson.getString("image");
                                String created_date = userJson.getString("created_date");
                                String select_command_limit = userJson.getString("select_command_limit");
                                String is_all = userJson.getString("is_all");
                                Log.e(TAG, "select_command_limit---- : "+select_command_limit );
                                Log.e(TAG, "is_all---- : "+is_all );

                                tv_price.setText("₹" + price);
                                tv_validity.setText("VALIDITY-" + validity);
                                tv_name.setText(name);
                                titile.setText(name);
                                tv_commands.setText(select_command);
                                tv_chatsupport.setText(chat);
                                tv_queries.setText(queries);
                                tv_callsupport.setText(call_suppoprt);
                                tv_certificate.setText(certificate);
                                tv_email.setText(email);
                                progress_view.setVisibility(View.INVISIBLE);
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
                       // progressDialog.dismiss();
                        Toast.makeText(PacageListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", ID);
                System.out.println("<><=getCourseTraingById===params=     " + params);
                return params;


            }
        };

        VolleySingleton.getInstance(PacageListActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}