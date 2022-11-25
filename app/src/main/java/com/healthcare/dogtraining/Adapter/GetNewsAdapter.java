package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.GetNewsFeedModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.NEWSFEEDS.CommentActivity;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;
import static com.healthcare.dogtraining.API.Base_Url.newsfeedLike;
import static com.healthcare.dogtraining.API.Base_Url.newsfeedUnLike;

public class GetNewsAdapter extends RecyclerView.Adapter<GetNewsAdapter.ViewHolder> {
    Session1 session1;
    private final List<GetNewsFeedModel> getNewsFeedModels;
    private final Context context;

    public GetNewsAdapter(List<GetNewsFeedModel> getNewsFeedModels, Context context) {
        this.getNewsFeedModels = getNewsFeedModels;
        this.context = context;
    }

    @Override
    public GetNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsfeedlist, parent, false);
        return new GetNewsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetNewsAdapter.ViewHolder holder, final int position) {
        final GetNewsFeedModel allCommunityModel = getNewsFeedModels.get(position);
        holder.tv_pet_name.setText(getNewsFeedModels.get(position).getTitle());
        holder.tv_total_likes.setText(getNewsFeedModels.get(position).getLike() + "likes");
        holder.tv_total_comments.setText(getNewsFeedModels.get(position).getComment() + "comments");

        session1 = new Session1(context);
        Glide.with(context)
                .load(categoryimagepasth + allCommunityModel.getImage())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .dontTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .skipMemoryCache(true)
                .apply(new RequestOptions().override(600, 200)).into(holder.pet_image);


        holder.txt_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String news_id = getNewsFeedModels.get(position).getId();
                CallLikeApi(news_id, holder.tv_total_likes);
            }
        });

        holder.txt_unlikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txt_likes.setVisibility(View.VISIBLE);
                holder.txt_unlikes.setVisibility(View.GONE);

                String news_id = getNewsFeedModels.get(position).getId();
                CallUnlikeApi(news_id, holder.tv_total_likes);
            }
        });


        holder.txt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("news_id", getNewsFeedModels.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    private void CallUnlikeApi(String news_id, TextView tv_total_likes) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + newsfeedUnLike,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String status = obj.getString("status");
                            String msg = obj.getString("msg");
                            System.out.println("<><>===");
                            if (result.equalsIgnoreCase("true")) {
                                String count = obj.getString("count");
                                tv_total_likes.setText(count + "likes");
                                System.out.println("<><===count" + count);
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session1.getUserId());
                params.put("news_id", news_id);
                Log.e("TAG", "getParams: " + params);
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


    private void CallLikeApi(String news_id, TextView tv_total_likes) {
        System.out.println("<><==newsid" + news_id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + newsfeedLike,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            int status = obj.getInt("status");
                            String msg = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")) {
                                if (status == 0) {
                                    String count = obj.getString("count");
                                    tv_total_likes.setText(count + "likes");

                                    System.out.println("<><>count" + count);

                                } else if (status == 1) {
                                    String count = obj.getString("count");
                                    tv_total_likes.setText(count + "likes");

                                    System.out.println("<><>countll" + count);

                                }
                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session1.getUserId());
                params.put("news_id", news_id);


                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    @Override
    public int getItemCount() {
        return getNewsFeedModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pet_image;
        TextView tv_pet_name, txt_comment, tv_total_likes, tv_total_comments, txt_likes, txt_unlikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pet_image = itemView.findViewById(R.id.pet_image);
            tv_pet_name = itemView.findViewById(R.id.tv_pet_name);
            tv_total_likes = itemView.findViewById(R.id.tv_total_likes);
            tv_total_comments = itemView.findViewById(R.id.tv_total_comments);
            txt_likes = itemView.findViewById(R.id.txt_likes);
            txt_unlikes = itemView.findViewById(R.id.txt_unlikes);
            txt_comment = itemView.findViewById(R.id.txt_comment);

        }
    }
}

