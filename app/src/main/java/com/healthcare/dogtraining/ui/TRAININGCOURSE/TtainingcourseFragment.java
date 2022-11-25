package com.healthcare.dogtraining.ui.TRAININGCOURSE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.healthcare.dogtraining.HomeActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.TrainingPacageActivity;
import com.healthcare.dogtraining.ui.MYPROFILE.MyprofileFragment;
import com.healthcare.dogtraining.ui.chat.ChatActivity;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.check_user_plan;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;
import static com.healthcare.dogtraining.API.Base_Url.get_notification_promolistcount;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TtainingcourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TtainingcourseFragment extends Fragment {
    View root;
    LinearLayout dogtraining,chatsupport;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Session1 session1;
    TextView tv_username,tv_breedname,tv_petage;
    CircleImageView userprofileimage;
    private RequestQueue rQueue;

    public TtainingcourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TtainingcourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TtainingcourseFragment newInstance(String param1, String param2) {
        TtainingcourseFragment fragment = new TtainingcourseFragment();
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
        root= inflater.inflate(R.layout.fragment_ttainingcourse, container, false);
        dogtraining= root.findViewById(R.id.dogtraining);
        chatsupport= root.findViewById(R.id.chatsupport);
        tv_username= root.findViewById(R.id.tv_username);
        tv_breedname= root.findViewById(R.id.tv_breedname);
        tv_petage= root.findViewById(R.id.tv_petage);
        userprofileimage= root.findViewById(R.id.userprofileimage);
        session1=new Session1(getActivity());
        tv_username.setText(session1.getUser_name());
        tv_breedname.setText(session1.getDog_breed());
        tv_petage.setText(session1.getDog_Age());

        getProfile();

        dogtraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckuserplanApi();
            }
        });

        chatsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   /* Intent intent = new Intent(getActivity(), ChatActivity.class)
                            .putExtra("planname","Training Course");
                    startActivity(intent);*/
            }
        });


        userprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyprofileFragment fragment = new MyprofileFragment();
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        });
        return root;

    }

               private void CheckuserplanApi() {
               String url = BaseUrl + check_user_plan ;
               StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                       new com.android.volley.Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                               try {
                                   JSONObject obj = new JSONObject(response);
                                   String result = obj.getString("result");
                                   String msg = obj.getString("msg");
                                   if (result.equalsIgnoreCase("true")) {

                                       System.out.println("PurchasedPlanActivity-----      ");
                                       Intent intent = new Intent(getActivity(), PurchasedPlanActivity.class);
                                       startActivity(intent);

                                   } else {
                                       System.out.println("TrainingPacageActivity-----      ");
                                       Intent intent = new Intent(getActivity(), TrainingPacageActivity.class);
                                       startActivity(intent);
                                   }
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       },

                       new com.android.volley.Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                              // Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }) {
                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                       Map<String, String> params = new HashMap<>();
                       params.put("user_id",session1.getUserId());
                       return params;
                   }
               };

               VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

                                        Glide.with(getActivity())
                                                .load(Image_Path + image)
                                                .placeholder(R.drawable.dogprofile)
                                                .apply(new RequestOptions()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .dontAnimate()
                                                        .centerCrop()
                                                        .dontTransform())
                                                .skipMemoryCache(true)
                                                .into(userprofileimage);

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