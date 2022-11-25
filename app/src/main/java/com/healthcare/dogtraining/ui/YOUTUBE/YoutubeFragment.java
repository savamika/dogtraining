package com.healthcare.dogtraining.ui.YOUTUBE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.Adapter.GEtYoutubeadapter;
import com.healthcare.dogtraining.Adapter.GetNewsAdapter;
import com.healthcare.dogtraining.Model.GetNewsFeedModel;
import com.healthcare.dogtraining.Model.GetYoutubeModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.ui.VideoViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.getNewsfeed;
import static com.healthcare.dogtraining.API.Base_Url.getYouTubleLink;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YoutubeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YoutubeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<GetYoutubeModel>getYoutubeModels=new ArrayList<>();
    RecyclerView youtuberecyclerview;
    GEtYoutubeadapter adapter;
    Button btn;
    RelativeLayout progress_view;

    public YoutubeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YoutubeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YoutubeFragment newInstance(String param1, String param2) {
        YoutubeFragment fragment = new YoutubeFragment();
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
     View root=inflater.inflate(R.layout.fragment_youtube, container, false);
        youtuberecyclerview=root.findViewById(R.id.youtuberecyclerview);
        btn=root.findViewById(R.id.btn);
        progress_view=root.findViewById(R.id.progress_view);
        GetYoutbelist();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), YoutubevideoviewActivity.class);
                startActivity(intent);
                }
               });

         return root;

         }

          private void GetYoutbelist() {

               String url = BaseUrl +getYouTubleLink;
               AndroidNetworking.get(url)
                       .build()
                       .getAsJSONObject(new JSONObjectRequestListener() {
                           @Override
                           public void onResponse(JSONObject jsonObject) {
                               progress_view.setVisibility(View.INVISIBLE);
                               try {
                                   String result = jsonObject.getString("result");
                                   String msg = jsonObject.getString("msg");
                                   if (result.equalsIgnoreCase("true")) {
                                       JSONArray jsonArray = jsonObject.getJSONArray("data");
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           JSONObject dataObject = jsonArray.getJSONObject(i);
                                           GetYoutubeModel allCommunityModel = new GetYoutubeModel
                                                   (dataObject.getString("id"),
                                                           dataObject.getString("youtube_link"),
                                                           dataObject.getString("thumbnail")
                                                   );
                                           getYoutubeModels.add(allCommunityModel);
                                           Log.e("youtube", "onResponse: "+dataObject.getString("youtube_link") );
                                           progress_view.setVisibility(View.INVISIBLE);
                                       }
                                   } else {
                                       progress_view.setVisibility(View.INVISIBLE);
                                   }
                                  adapter = new GEtYoutubeadapter(getYoutubeModels,getActivity());
                                   RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                                   youtuberecyclerview.setLayoutManager(mLayoutManger);
                                   youtuberecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                   youtuberecyclerview.setItemAnimator(new DefaultItemAnimator());
                                   youtuberecyclerview.setItemAnimator(new DefaultItemAnimator());
                                   youtuberecyclerview.setAdapter(adapter);
                                   adapter.notifyDataSetChanged();
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
                       }); }
           }