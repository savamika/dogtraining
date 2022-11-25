package com.healthcare.dogtraining.ui.Ship;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.Pending.PendingAdapter;
import com.healthcare.dogtraining.ui.Pending.PendingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.get_pending_order;
import static com.healthcare.dogtraining.API.Base_Url.get_shipping_order;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShipFragment extends Fragment {
    private static final String TAG = "ShipFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerview;
    List<ShipOrderModel>shipOrderModels=new ArrayList<>();
    Session1 session1;
    ShipAdapter adapter;
    RelativeLayout progress_view;
  TextView hint;
    public ShipFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShipFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShipFragment newInstance(String param1, String param2) {
        ShipFragment fragment = new ShipFragment();
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
     View root=inflater.inflate(R.layout.fragment_ship, container, false);
        recyclerview=root.findViewById(R.id.recyclerview);
        progress_view=root.findViewById(R.id.progress_view);
        hint=root.findViewById(R.id.hint);
        session1=new Session1(getActivity());
        GetShipApi();

        return root;

        }

                  private void GetShipApi() {
                      progress_view.setVisibility(View.VISIBLE);
                  String url = BaseUrl + get_shipping_order;
                  AndroidNetworking.post(url)
                          .addBodyParameter("user_id",session1.getUserId())
                          .build()
                          .getAsJSONObject(new JSONObjectRequestListener() {
                              @Override
                              public void onResponse(JSONObject jsonObject) {
                                  Log.e(TAG, "GetShipApi: "+jsonObject.toString() );
                                  progress_view.setVisibility(View.INVISIBLE);
                                  try {
                                      shipOrderModels.clear();
                                      String result = jsonObject.getString("result");
                                      String msg = jsonObject.getString("msg");
                                      System.out.print("jsonobject<><>==="+jsonObject);
                                      if (result.equalsIgnoreCase("true")) {
                                          JSONArray jsonArray = jsonObject.getJSONArray("data");
                                          for (int i = 0; i < jsonArray.length(); i++) {
                                              JSONObject dataObject = jsonArray.getJSONObject(i);
                                              ShipOrderModel allCommunityModel = new ShipOrderModel
                                                      (dataObject.getString("id"),
                                                              dataObject.getString("user_id"),
                                                              dataObject.getString("address"),
                                                              dataObject.getString("total_amount"),
                                                              dataObject.getString("payment_method"),
                                                              dataObject.getString("transaction_id"),
                                                              dataObject.getString("status"),
                                                              dataObject.getString("order_status"),
                                                              dataObject.getString("date_time"),
                                                              dataObject.getString("order_id"),
                                                              dataObject.getString("product_id"),
                                                              dataObject.getString("quanitity"),
                                                              dataObject.getString("cat_id"),
                                                              dataObject.getString("name"),
                                                              dataObject.getString("price"),
                                                              dataObject.getString("discounted_price"),
                                                              dataObject.getString("image"),
                                                              dataObject.getString("color"),
                                                              dataObject.getString("age")
                                                      );
                                              shipOrderModels.add(allCommunityModel);
                                          }
                                          hint.setVisibility(View.GONE);
                                      } else {
                                          hint.setVisibility(View.VISIBLE);

                                      }
                                      adapter = new ShipAdapter(shipOrderModels,getActivity());
                                      RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                                      recyclerview.setLayoutManager(mLayoutManger);
                                      recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                      recyclerview.setItemAnimator(new DefaultItemAnimator());
                                      recyclerview.setAdapter(adapter);
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
                          });


              }




}