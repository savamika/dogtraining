package com.healthcare.dogtraining.ui.MYPROFILE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.EditProfileActivity;
import com.healthcare.dogtraining.OrderHistoryActivity;
import com.healthcare.dogtraining.ProfileDetailsActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyprofileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyprofileFragment extends Fragment {
    View root;
    LinearLayout MYORDER,l_pet;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Session1 session1;
    private RequestQueue rQueue;
    CircleImageView userimage;
    TextView tv_username,tv_userphonenumber,tv_email,tv_editprpfike;
    RelativeLayout progress_view;

    public MyprofileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyprofileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyprofileFragment newInstance(String param1, String param2) {
        MyprofileFragment fragment = new MyprofileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_myprofile, container, false);

        progress_view= root.findViewById(R.id.progress_view);
        MYORDER= root.findViewById(R.id.MYORDER);
        userimage= root.findViewById(R.id.userimage);
        tv_username= root.findViewById(R.id.tv_username);
        tv_userphonenumber= root.findViewById(R.id.tv_userphonenumber);
        tv_email= root.findViewById(R.id.tv_email);
        tv_editprpfike= root.findViewById(R.id.tv_editprpfike);
        l_pet= root.findViewById(R.id.l_pet);
        session1=new Session1(getActivity());
        getProfile();


        MYORDER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivity(intent);

            }
        });


        tv_editprpfike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });


        l_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PetdetailsActivity.class);
                startActivity(intent);
            }
        });

           return root;

          }

                private void getProfile() {
                System.out.println("<><===userid"+session1.getUserId());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl +getMyProfile,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject= new JSONObject(response);

                                    String msg=jsonObject.getString("msg");
                                    System.out.println("<><===getprofile"+jsonObject);
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

                                            tv_username.setText(fullname);
                                            tv_userphonenumber.setText(mobile);
                                            tv_email.setText(email);
                                            Glide.with(getActivity())
                                                    .load(Image_Path + image)
                                                    .placeholder(R.drawable.dogprofile)
                                                    .apply(new RequestOptions()
                                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                            .dontAnimate()
                                                            .centerCrop()
                                                            .dontTransform())

                                                    .skipMemoryCache(true)
                                                    .into(userimage);
                                            progress_view.setVisibility(View.INVISIBLE);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                            progress_view.setVisibility(View.INVISIBLE);
                                        }
                                    } else {
                                        progress_view.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
                rQueue = Volley.newRequestQueue(getActivity());
                rQueue.add(stringRequest);


            }

       @Override
       public void onResume() {
        getProfile();
        super.onResume();
    }
}