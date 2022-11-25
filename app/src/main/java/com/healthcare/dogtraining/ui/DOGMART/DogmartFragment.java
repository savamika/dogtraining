package com.healthcare.dogtraining.ui.DOGMART;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.healthcare.dogtraining.DogcategorylistActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.MYPROFILE.MyprofileFragment;
import com.healthcare.dogtraining.ui.chat.ChatActivity;

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
 * Use the {@link DogmartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DogmartFragment extends Fragment {
    View root;
    LinearLayout dogmarts,chatsupport;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CircleImageView profileimage;
    TextView tv_name,tv_breed,tv_dob;
    private RequestQueue rQueue;
    Session1 session1;

    public DogmartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DogmartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DogmartFragment newInstance(String param1, String param2) {
        DogmartFragment fragment = new DogmartFragment();
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
        root=inflater.inflate(R.layout.fragment_dogmart, container, false);
        dogmarts=root.findViewById(R.id.dogmarts);
        profileimage=root.findViewById(R.id.profileimage);
        tv_name=root.findViewById(R.id.tv_name);
        tv_breed=root.findViewById(R.id.tv_breed);
        tv_dob=root.findViewById(R.id.tv_dob);
        chatsupport=root.findViewById(R.id.chatsupport);
        session1=new Session1(getActivity());
        tv_breed.setText(session1.getDog_breed());
        tv_dob.setText(session1.getDog_Age());
        chatsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent i=new Intent(getActivity(), ChatActivity.class).  putExtra("planname","Dog Mart");
                startActivity(i);*/
            }
        });
        getProfile();



        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MyprofileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        dogmarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DogcategorylistActivity.class);
                intent.putExtra("from","mart");

                startActivity(intent);

            }
            });

        return root;

    }

            private void getProfile() {
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
                                        tv_name.setText(fullname);
                                        tv_breed.setText(pet_bareed);
                                        tv_dob.setText(pet_age);
                                        Glide.with(getActivity())
                                                .load(Image_Path + image)
                                                .apply(new RequestOptions()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .dontAnimate()
                                                        .centerCrop()
                                                        .dontTransform())
                                                .skipMemoryCache(true)
                                                .into(profileimage);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {

                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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