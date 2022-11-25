package com.healthcare.dogtraining.ui.ADOPTION;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.Model.AdoptionModel;
import com.healthcare.dogtraining.Model.GetImagesModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.UserMyAdoption;
import static com.healthcare.dogtraining.API.Base_Url.getAdopt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link My_AdoptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class My_AdoptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerview;
    LinearLayout trainingpacage;
    Button btn_adoption;
    List<AdoptionModel> adoptionModelList = new ArrayList<>();
    MyAdoptionAdapter adoptionAdapter;
    List<GetImagesModel> imagemodels = new ArrayList<>();
    Session1 session1;
    String image1;
    RelativeLayout progress_view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public My_AdoptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdoptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static My_AdoptionFragment newInstance(String param1, String param2) {
        My_AdoptionFragment fragment = new My_AdoptionFragment();
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
        View root = inflater.inflate(R.layout.fragment_adoption, container, false);
        recyclerview = root.findViewById(R.id.recyclerview);
        trainingpacage = root.findViewById(R.id.trainingpacage);
        btn_adoption = root.findViewById(R.id.btn_adoption);
        progress_view = root.findViewById(R.id.progress_view);
        session1 = new Session1(getActivity());
        GetAdorptionList();

      /*  trainingpacage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AdoptionDetailsActivity.class) ;
                startActivity(intent);
            }
            });*/

        btn_adoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdoptionFormActivity.class);
                startActivity(intent);
            }
        });

        return root;

    }

    private void GetAdorptionList() {
        adoptionModelList.clear();
        String url = BaseUrl + UserMyAdoption;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id",session1.getUserId())

                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progress_view.setVisibility(View.INVISIBLE);
                        try {
                            adoptionModelList.clear();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            Log.e("TAG", "jsonObject:UserMyAdoption        "+jsonObject.toString() );
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    AdoptionModel allCommunityModel = new AdoptionModel
                                            (dataObject.getString("id"),
                                                    dataObject.getString("user_id"),
                                                    dataObject.getString("name"),
                                                    dataObject.getString("age"),
                                                    dataObject.getString("gender"),
                                                    dataObject.getString("breed"),
                                                    dataObject.getString("weight"),
                                                    dataObject.getString("imagess"),
                                                    dataObject.getString("description"),
                                                    dataObject.getString("status"),
                                                    dataObject.getString("created_at"),
                                                    dataObject.getString("update_at")
                                            );
                                    adoptionModelList.add(allCommunityModel);

                                    //System.out.println("namfiif"+adoptionModelList.get(0).getImage());
                                }
                            } else {

                            }
                            adoptionAdapter = new MyAdoptionAdapter(adoptionModelList, getActivity());
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                            recyclerview.setLayoutManager(mLayoutManger);
                            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setAdapter(adoptionAdapter);
                            adoptionAdapter.notifyDataSetChanged();
                            progress_view.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress_view.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progress_view.setVisibility(View.INVISIBLE);
                        Log.e("error_my_join", anError.toString());
                    }
                });
    }

    @Override
    public void onResume() {
        GetAdorptionList();
        super.onResume();
    }
}

