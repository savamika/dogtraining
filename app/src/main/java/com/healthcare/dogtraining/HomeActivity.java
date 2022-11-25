package com.healthcare.dogtraining;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.healthcare.dogtraining.API.Base_Url;
import com.healthcare.dogtraining.ui.DOGACCESSORIES.DogAccessoriesActivity;
import com.healthcare.dogtraining.ui.MYPROFILE.MyprofileFragment;
import com.healthcare.dogtraining.ui.firebase.NotificationActivity;
import com.healthcare.dogtraining.ui.home.HomeFragment;
import com.healthcare.dogtraining.utils.VolleySingleton;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.Logout;
import static com.healthcare.dogtraining.API.Base_Url.checkaddtocart;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;
import static com.healthcare.dogtraining.API.Base_Url.get_countcart;
import static com.healthcare.dogtraining.API.Base_Url.get_notification_promolistcount;
import static com.healthcare.dogtraining.API.Base_Url.orderPayment;
import static com.healthcare.dogtraining.API.Base_Url.seen_promotion_list;

public class HomeActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    Session1 session1;
    private RequestQueue rQueue;
    CircleImageView imageView;
    TextView tv_name, tv_notification_count, tv_countcart;
    ImageView img_notification;
    RelativeLayout cartlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dog Tranning");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        img_notification = findViewById(R.id.img_notification);
        tv_notification_count = findViewById(R.id.tv_notification_count);
        tv_countcart = findViewById(R.id.tv_countcart);
        cartlist = findViewById(R.id.cartlist);
        session1 = new Session1(HomeActivity.this);
        System.out.println("useridpriyaa" + session1.getUserId());

        getProfile();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        cartlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, CartlistActivity.class);
                startActivity(intent);
               // CallCartStatusApi();

                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        imageView = headerLayout.findViewById(R.id.imageView);
        tv_name = headerLayout.findViewById(R.id.tv_name);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_TRAININGCOURSE,
                R.id.nav_DOGACCESSORIES,
                R.id.nav_DOGMART,
                R.id.nav_NEWSFEEDS,
                R.id.nav_BREEDINFO,
                R.id.nav_ADOPTION,
                R.id.nav_MyADOPTION,
                R.id.nav_YOUTUBE,
                R.id.nav_POLICIES,
                R.id.nav_SUPPORT,
                R.id.nav_LOGOU)

                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_LOGOU);
        MenuItem menuItemPackage = menu.findItem(R.id.nev_PACKAGE);
        MenuItem menuItemMyorder = menu.findItem(R.id.nav_MYORDER);

        menuItemMyorder.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_MYORDER:
                        Intent intent=new Intent(getApplicationContext(),OrderHistoryActivity.class);
                        startActivity(intent);
                        return true;

                    default:
                        return HomeActivity.super.onContextItemSelected(item);
                }
            }
        });
        menuItemPackage.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nev_PACKAGE:
                      Intent intent=new Intent(getApplicationContext(),TrainingPacageActivity.class);
                      startActivity(intent);
                        return true;

                    default:
                        return HomeActivity.super.onContextItemSelected(item);
                }
            }
        });
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_LOGOU:
                        System.out.println("<><><>====item" + item);
                        Logout_Api();
                        return true;

                    default:
                        return HomeActivity.super.onContextItemSelected(item);
                }
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MyprofileFragment();
                FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawer.close();
            }
        });


        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeenNotificationApi();
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
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
                        Log.e("jsonObject", "onResponse: "+jsonObject.toString());
                        try {
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                String data = jsonObject.getString("data");


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
                        Log.e("TAG", "onResponse: "+jsonObject.toString() );
                        try {
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                String data = String.valueOf(jsonObject.getInt("data"));
                                Log.e("data", "onResponse: "+data);
                                tv_countcart.setText(data);
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

    private void SeenNotificationApi() {
        String url = BaseUrl + seen_promotion_list;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("notification data", "onResponse: "+response);
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            System.out.print("<><===seennotification" + obj);
                            if (result.equalsIgnoreCase("true")) {

                                tv_notification_count.setText("0");
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session1.getUserId());
                params.put("seen_status", "1");
                return params;
            }
        };

        VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
    }


    private void getNotificationCountApi() {
        String url = BaseUrl + get_notification_promolistcount;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response", "onResponse: "+response);
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            System.out.print("<><===notificationcount" + obj);
                            if (result.equalsIgnoreCase("true")) {
                                String data = obj.getString("data");
                                tv_notification_count.setText(data);
                            } else {
                                tv_notification_count.setText("0");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session1.getUserId());
                return params;
            }
        };

        VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
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
                                    Glide.with(HomeActivity.this)
                                            .load(Image_Path + image)
                                            .placeholder(R.drawable.dogprofile)
                                            .apply(new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .dontAnimate()
                                                    .centerCrop()
                                                    .dontTransform())
                                            .skipMemoryCache(true)
                                            .into(imageView);

                                    session1.setDog_breed(pet_bareed);
                                    session1.setDog_Age(pet_age);
                                    session1.setDog_Age(pet_age);
                                    session1.setUserimage(image);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
        rQueue = Volley.newRequestQueue(HomeActivity.this);
        rQueue.add(stringRequest);


    }


    private void Logout_Api() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this).setTitle("Dog Training")
                .setMessage("Are you sure, you want to logout this app");
        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int whichButton) {
                final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setTitle("Loading...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + Logout,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("->>", "onResponse: "+response.toString());
                                try {
                                    progressDialog.dismiss();
                                    JSONObject obj = new JSONObject(response);
                                    String result = obj.getString("result");
                                    String msg = obj.getString("msg");
                                    System.out.println("<><><====logout" + obj);
                                    if (result.equals("true")) {
                                        session1.setLogin(false);

                                        session1.setUserId("");
                                        session1.setMobile("", "");
                                        session1.setUser_name("");
                                        session1.logout();

                                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                                build();

                                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(HomeActivity.this, gso);
                                        googleSignInClient.signOut();
                                        LoginManager.getInstance().logOut();

                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    System.out.println("<><><>" + e.getMessage().toString());
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", session1.getUserId());
                        return params;
                    }
                };
                VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onResume() {
        getProfile();
        getNotificationCountApi();
        GetcountcartApi();
        super.onResume();
    }


}