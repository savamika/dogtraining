package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.GetNewsFeedModel;
import com.healthcare.dogtraining.Model.Trainingpackagemodel;
import com.healthcare.dogtraining.PacageListActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.TrainingPacageActivity;
import com.healthcare.dogtraining.ui.NEWSFEEDS.CommentActivity;
import com.healthcare.dogtraining.ui.TRAININGCOURSE.PurchasedPlanActivity;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.NEWIMAGE_PATH;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;
import static com.healthcare.dogtraining.API.Base_Url.check_user_plan;
import static com.healthcare.dogtraining.API.Base_Url.newsfeedLike;

public class GetTrainigAdapter extends RecyclerView.Adapter<GetTrainigAdapter.ViewHolder> {
    private List<Trainingpackagemodel> trainingpackagemodels;
    private Context context;


    public GetTrainigAdapter(List<Trainingpackagemodel> trainingpackagemodels, Context context) {
        this.trainingpackagemodels = trainingpackagemodels;
        this.context = context;
    }


    @NonNull
    @Override
    public GetTrainigAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.traininglist, parent, false);
        return new GetTrainigAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetTrainigAdapter.ViewHolder holder, final int position) {
        final Trainingpackagemodel allCommunityModel = trainingpackagemodels.get(position);
        holder.tv_name.setText(allCommunityModel.getName());

        Glide.with(context)
                .load(NEWIMAGE_PATH + allCommunityModel.getImage())
              .into(holder.iv_dog_image);



        holder.trainingpacage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PacageListActivity.class);
                intent.putExtra("ID",allCommunityModel.getId());
                context.startActivity(intent);
                }

                });

                 }


                 @Override
        public int getItemCount() {
        return trainingpackagemodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_dog_image;
    TextView tv_name,tv_image;
    LinearLayout trainingpacage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_dog_image=itemView.findViewById(R.id.iv_dog_image);
            tv_name=itemView.findViewById(R.id.tv_name);
            trainingpacage=itemView.findViewById(R.id.trainingpacage);



        }
    }
}

