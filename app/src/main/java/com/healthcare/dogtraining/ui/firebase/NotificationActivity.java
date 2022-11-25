package com.healthcare.dogtraining.ui.firebase;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.Adapter.NotificationAdapter;
import com.healthcare.dogtraining.Model.NOtificationModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Notification_promotion_list;

public class NotificationActivity extends AppCompatActivity {
    Session1 session1;
    RecyclerView recyclerview;
    List<NOtificationModel> nOtificationModels = new ArrayList<>();
    NotificationAdapter adapter;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        session1 = new Session1(NotificationActivity.this);
        recyclerview = findViewById(R.id.recyclerview);
        iv_back = findViewById(R.id.iv_back);

        GetNotificationApi();


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void GetNotificationApi() {
        final ProgressDialog progressDialog = new ProgressDialog(NotificationActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String url = BaseUrl + Notification_promotion_list;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", session1.getUserId())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            nOtificationModels.clear();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            String count = jsonObject.getString("count");
                            System.out.print("jsonobject<><>===" + jsonObject);
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    NOtificationModel allCommunityModel = new NOtificationModel
                                            (dataObject.getString("id"),
                                                    dataObject.getString("title"),
                                                    dataObject.getString("description"),
                                                    dataObject.getString("image"),
                                                    dataObject.getString("receiver_id"),
                                                    dataObject.getString("seen_status"),
                                                    dataObject.getString("created_date"));
                                    nOtificationModels.add(allCommunityModel);
                                }

                            } else {

                            }
                            adapter = new NotificationAdapter(nOtificationModels, NotificationActivity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(NotificationActivity.this);
                            recyclerview.setLayoutManager(mLayoutManger);
                            recyclerview.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false));
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e("error_my_join", anError.toString());
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}