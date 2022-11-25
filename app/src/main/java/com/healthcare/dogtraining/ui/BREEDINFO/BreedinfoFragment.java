package com.healthcare.dogtraining.ui.BREEDINFO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.Adapter.BreedinfoAdapter;
import com.healthcare.dogtraining.Adapter.DogMArtAdapter;
import com.healthcare.dogtraining.DogcategorylistActivity;
import com.healthcare.dogtraining.Model.BreedinfoModel;
import com.healthcare.dogtraining.Model.DogMartModel;
import com.healthcare.dogtraining.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.getBreed_list;
import static com.healthcare.dogtraining.API.Base_Url.getCategory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BreedinfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BreedinfoFragment extends Fragment {
    private static final String TAG = "BreedinfoFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    View root;
    private ArrayList<String> list;
    LinearLayout trainingpacage;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<BreedinfoModel>breedinfoModels=new ArrayList<>();
    BreedinfoAdapter adapter;
    RecyclerView recyclerview;
    TextView tv_nodata_found;
    RelativeLayout progress_view;



    public BreedinfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BreedinfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BreedinfoFragment newInstance(String param1, String param2) {
        BreedinfoFragment fragment = new BreedinfoFragment();
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
        View root= inflater.inflate(R.layout.fragment_breedinfo, container, false);

        progress_view = root.findViewById(R.id.progress_view);
        recyclerview = root.findViewById(R.id.recyclerview);
        tv_nodata_found = root.findViewById(R.id.tv_nodata_found);


        GetBreedInfoListApi();

        return root;

        }


        private void GetBreedInfoListApi() {

                String url = BaseUrl +getBreed_list;
                AndroidNetworking.get(url)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {

                                try {
                                    breedinfoModels.clear();
                                    String result = jsonObject.getString("result");
                                    String msg = jsonObject.getString("msg");
                                    Log.e(TAG, "getBreed_listonResponse: "+jsonObject.toString() );
                                    if (result.equalsIgnoreCase("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject dataObject = jsonArray.getJSONObject(i);
                                            BreedinfoModel allCommunityModel = new BreedinfoModel
                                                    (dataObject.getString("id"),
                                                            dataObject.getString("name"),
                                                            dataObject.getString("age"),
                                                            dataObject.getString("image"),
                                                            dataObject.getString("breed"),
                                                            dataObject.getString("weight"),
                                                            dataObject.getString("gender"),
                                                            dataObject.getString("status"),
                                                            dataObject.getString("height"),
                                                            dataObject.getString("description")
                                                    );
                                            breedinfoModels.add(allCommunityModel);
                                                   }
                                                  }
                                            else {

                                        tv_nodata_found.setVisibility(View.VISIBLE);
                                            }
                                    adapter = new BreedinfoAdapter(breedinfoModels,getActivity());
                                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                                    recyclerview.setLayoutManager(mLayoutManger);
                                    recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                    recyclerview.setItemAnimator(new DefaultItemAnimator());
                                    recyclerview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    progress_view.setVisibility(View.INVISIBLE);
                                } catch (JSONException e) {
                                    progress_view.setVisibility(View.INVISIBLE);
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                progress_view.setVisibility(View.INVISIBLE);
                                Log.e("error_my_join", anError.toString());
                            }
                        });


            }


        }