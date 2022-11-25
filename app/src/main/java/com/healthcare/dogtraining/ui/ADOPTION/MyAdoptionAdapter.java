package com.healthcare.dogtraining.ui.ADOPTION;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.healthcare.dogtraining.AdoptionDetailsActivity;
import com.healthcare.dogtraining.Model.AdoptionModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;
import static com.healthcare.dogtraining.API.Base_Url.deleteadoptionpost;
import static com.healthcare.dogtraining.API.Base_Url.newsfeedLike;

public class MyAdoptionAdapter extends RecyclerView.Adapter<MyAdoptionAdapter.ViewHolder> {
    private final List<AdoptionModel> adoptionModels;
    private final Context context;

Session1 session1;
    public MyAdoptionAdapter(List<AdoptionModel> adoptionModels, Context context) {
        this.adoptionModels = adoptionModels;
        this.context = context;
    }


    @NonNull
    @Override
    public MyAdoptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myadoption_list, parent, false);
        return new MyAdoptionAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdoptionAdapter.ViewHolder holder, final int position) {
        final AdoptionModel allCommunityModel = adoptionModels.get(position);
        session1=new Session1(context);
        holder.tv_breedname.setText(allCommunityModel.getBreed());
        holder.tv_name.setText("Name" + allCommunityModel.getName());
        holder.tv_age.setText("Age :" + allCommunityModel.getAge());
        holder.tv_gender.setText("Sex : " + allCommunityModel.getGender());

        Glide.with(context)
                .load(categoryimagepasth + allCommunityModel.getImage())
                .centerCrop()
                .into(holder.iv_icon);

        holder.trainingpacage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditAdoptiondETAILSActivity.class);
                intent.putExtra("ID", allCommunityModel.getId());
                context.startActivity(intent);
            }
        });

        holder.imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteadoptionpost(allCommunityModel.getId(),position);

            }
        });






    }


    @Override
    public int getItemCount() {
        return adoptionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_breedname, tv_name, tv_age, tv_gender;
        LinearLayout trainingpacage;
        ImageView iv_icon, imgedit,
                imgdel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_breedname = itemView.findViewById(R.id.tv_breedname);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_gender = itemView.findViewById(R.id.tv_gender);
            trainingpacage = itemView.findViewById(R.id.trainingpacage);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            imgedit = itemView.findViewById(R.id.imgedit);
            imgdel = itemView.findViewById(R.id.imgdel);
        }

    }


    private void deleteadoptionpost(String news_id, int tv_total_likes) {
        System.out.println("<><==newsid" + news_id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + deleteadoptionpost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("TAG", "onResponse:deleteadoptionpost "+response );
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");



                            if (result.equalsIgnoreCase("true")) {

                                adoptionModels.remove(tv_total_likes);
                                notifyDataSetChanged();

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
                params.put("id", news_id);

                Log.e("TAG", "getParams: "+params );
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }




}


