package com.healthcare.dogtraining.ui.NEWSFEEDS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.API.Base_Url;
import com.healthcare.dogtraining.Adapter.CommentListAdapter;
import com.healthcare.dogtraining.Adapter.GetNewsAdapter;
import com.healthcare.dogtraining.Model.GetCommentListModel;
import com.healthcare.dogtraining.Model.GetNewsFeedModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.utils.Session1;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.getCommentList;
import static com.healthcare.dogtraining.API.Base_Url.getNewsfeed;
import static com.healthcare.dogtraining.API.Base_Url.newsfeedComment;

public class CommentActivity extends AppCompatActivity {
ImageView img_send_comment;
EditText et_post_comment;
String news_id;
Session1 session1;
RecyclerView recycler_comment_list;
CommentListAdapter adapter;
private List<GetCommentListModel> getCommentListModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        img_send_comment=findViewById(R.id.img_send_comment);
        et_post_comment=findViewById(R.id.et_post_comment);
        recycler_comment_list=findViewById(R.id.recycler_comment_list);
        session1=new Session1(CommentActivity.this);

        if (getIntent() != null) {
            try {
                news_id = getIntent().getStringExtra("news_id");
                System.out.println("<><===id"+news_id);

            } catch (Exception e) {

                System.out.println("chekc exc" + e.toString());
            }
        }
        CommentlistAPi(news_id);

        img_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComentApi(news_id);
            }
        });

    }

       private void sendComentApi(String news_id) {
        final String commenttxt=et_post_comment.getText().toString();
               StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + newsfeedComment,
                       new Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                               try {
                                   JSONObject obj = new JSONObject(response);
                                   String result = obj.getString("result");
                                   String msg = obj.getString("msg");
                                   if (result.equalsIgnoreCase("true")) {
                                        et_post_comment.getText().clear();
                                         CommentlistAPi(news_id);

                                   } else {

                                       Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_LONG).show();

                                   }

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       },
                       new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {

                               Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }) {
                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                       Map<String, String> params = new HashMap<>();
                       params.put("user_id",session1.getUserId());
                       params.put("news_id",news_id);
                       params.put("comment",commenttxt );
                       return params;
                   }
               };

               VolleySingleton.getInstance(CommentActivity.this).addToRequestQueue(stringRequest);
           }

        private void CommentlistAPi(String news_id) {
        final ProgressDialog progressDialog = new ProgressDialog(CommentActivity.this);
                progressDialog.setTitle("Loading..");
                progressDialog.show();
                String url = BaseUrl + getCommentList;
                AndroidNetworking.post(url)
                        .addBodyParameter("news_id",news_id)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                progressDialog.dismiss();
                                try {
                                    getCommentListModels.clear();
                                    String result = jsonObject.getString("result");
                                    String msg = jsonObject.getString("msg");
                                    if (result.equalsIgnoreCase("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject dataObject = jsonArray.getJSONObject(i);
                                            GetCommentListModel allCommunityModel = new GetCommentListModel
                                                    (dataObject.getString("id"),
                                                            dataObject.getString("user_id"),
                                                            dataObject.getString("news_id"),
                                                            dataObject.getString("comment"),
                                                            dataObject.getString("create_date"),
                                                            dataObject.getString("date"),
                                                            dataObject.getString("fullname"),
                                                            dataObject.getString("image"));
                                            getCommentListModels.add(allCommunityModel);

                                        }

                                    } else {

                                    }
                                    adapter = new CommentListAdapter(getCommentListModels,CommentActivity.this);
                                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(CommentActivity.this);
                                    recycler_comment_list.setLayoutManager(mLayoutManger);
                                    recycler_comment_list.setLayoutManager(new LinearLayoutManager(CommentActivity.this, RecyclerView.VERTICAL, false));
                                    recycler_comment_list.setItemAnimator(new DefaultItemAnimator());
                                    recycler_comment_list.setItemAnimator(new DefaultItemAnimator());
                                    recycler_comment_list.setAdapter(adapter);
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