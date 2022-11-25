package com.healthcare.dogtraining.ui.POLICIES;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.healthcare.dogtraining.HomeActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.get_notification_promolistcount;
import static com.healthcare.dogtraining.API.Base_Url.getpolicy;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PoliciesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PoliciesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tv_policy,tv_title;

    public PoliciesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PoliciesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PoliciesFragment newInstance(String param1, String param2) {
        PoliciesFragment fragment = new PoliciesFragment();
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
     View root=inflater.inflate(R.layout.fragment_policies, container, false);
        tv_title=root.findViewById(R.id.tv_title);
        tv_policy=root.findViewById(R.id.tv_content);

        CallPolicyApi();

        return root;
        }

        private void CallPolicyApi() {
        String url = BaseUrl + getpolicy ;
                StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    String result = obj.getString("result");
                                    String msg = obj.getString("msg");
                                    System.out.print("<><===notificationcount"+obj);
                                    if (result.equalsIgnoreCase("true")) {
                                        JSONObject obj1 = obj.getJSONObject("data");
                                        String id=obj1.getString("id");
                                        String title=obj1.getString("title");
                                        String content=obj1.getString("content");

                                        tv_title.setText(title);
                                        tv_policy.setText(content);



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
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                      //  params.put("user_id",session1.getUserId());
                        return params;
                    }
                };

                VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
            }



}