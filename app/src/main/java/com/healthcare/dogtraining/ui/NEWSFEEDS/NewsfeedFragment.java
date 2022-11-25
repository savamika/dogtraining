package com.healthcare.dogtraining.ui.NEWSFEEDS;

import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.Adapter.DogAccessoriesAdapter;
import com.healthcare.dogtraining.Adapter.GetNewsAdapter;
import com.healthcare.dogtraining.Model.DogAccessoriesModel;
import com.healthcare.dogtraining.Model.GetNewsFeedModel;
import com.healthcare.dogtraining.ProductlistActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.ui.DOGACCESSORIES.DogAccessoriesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.getCategory;
import static com.healthcare.dogtraining.API.Base_Url.getNewsfeed;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsfeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsfeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerview;
    List<GetNewsFeedModel>getNewsFeedModels=new ArrayList<>();
    GetNewsAdapter adapter;
    RelativeLayout progress_view;

    public NewsfeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsfeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsfeedFragment newInstance(String param1, String param2) {
        NewsfeedFragment fragment = new NewsfeedFragment();
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
    View root=inflater.inflate(R.layout.fragment_newsfeed, container, false);
        recyclerview=root.findViewById(R.id.recyclerview);
        progress_view=root.findViewById(R.id.progress_view);
        GetnewsFeedApi();
        return root;
    }

                private void GetnewsFeedApi() {

                String url = BaseUrl +getNewsfeed;
                AndroidNetworking.get(url)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {

                                try {
                                    getNewsFeedModels.clear();
                                    String result = jsonObject.getString("result");
                                    String msg = jsonObject.getString("msg");
                                    if (result.equalsIgnoreCase("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject dataObject = jsonArray.getJSONObject(i);
                                            GetNewsFeedModel allCommunityModel = new GetNewsFeedModel
                                                    (dataObject.getString("id"),
                                                            dataObject.getString("title"),
                                                            dataObject.getString("content"),
                                                            dataObject.getString("image"),
                                                            dataObject.getString("status"),
                                                            dataObject.getString("created_at"),
                                                            dataObject.getString("updated_at"),
                                                            dataObject.getString("Like"),
                                                            dataObject.getString("Comment"),
                                                            dataObject.getString("Share")
                                                    );
                                            getNewsFeedModels.add(allCommunityModel);

                                        }

                                    } else {

                                    }
                                    adapter = new GetNewsAdapter(getNewsFeedModels,getActivity());
                                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                                    recyclerview.setLayoutManager(mLayoutManger);
                                    recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                    recyclerview.setItemAnimator(new DefaultItemAnimator());
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


       @Override
        public void onResume() {
        GetnewsFeedApi();
        super.onResume();
    }
}