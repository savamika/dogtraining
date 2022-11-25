package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.GetYoutubeModel;
import com.healthcare.dogtraining.Model.Trainingpackagemodel;
import com.healthcare.dogtraining.PacageListActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.ui.YOUTUBE.YoutubevideoviewActivity;

import java.sql.CallableStatement;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class GEtYoutubeadapter extends RecyclerView.Adapter<GEtYoutubeadapter.ViewHolder> {
    private List<GetYoutubeModel> getYoutubeModels;
    private Context context;
    private MediaController mController;
    private Uri uriYouTube;


    public GEtYoutubeadapter(List<GetYoutubeModel> getYoutubeModels, Context context) {
        this.getYoutubeModels = getYoutubeModels;
        this.context = context;
    }


    @NonNull
    @Override
    public GEtYoutubeadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.getyoutubelist, parent, false);
        return new GEtYoutubeadapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GEtYoutubeadapter.ViewHolder holder, final int position) {
        final GetYoutubeModel allCommunityModel = getYoutubeModels.get(position);


        final String img_url = categoryimagepasth + getYoutubeModels.get(position).getThumbnail();
        RequestOptions requestOptions1 = new RequestOptions();
        requestOptions1.isMemoryCacheable();

        System.out.print("<><>===thumbnail" + getYoutubeModels.get(position).getThumbnail());

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions1)
                .load(categoryimagepasth + getYoutubeModels.get(position).getThumbnail())
                .into(holder.image_view_thm);


        Log.e("youtubelink", "onBindViewHolder: " + getYoutubeModels.get(position).getYoutube_link());


        holder.card_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (getYoutubeModels.get(position).getYoutube_link() != null &&
                            !getYoutubeModels.get(position).getYoutube_link().equalsIgnoreCase("")) {
                        context.startActivity(new Intent(context, YoutubevideoviewActivity.class)
                                .putExtra("mYoutubeLink", getYoutubeModels.get(position).getYoutube_link()));
                    } else {

                        holder.rl_laoyut.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return getYoutubeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_video;
        RelativeLayout rl_laoyut;
        ImageView image_view_thm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_video = itemView.findViewById(R.id.card_video);
            rl_laoyut = itemView.findViewById(R.id.rl_laoyut);
            image_view_thm = itemView.findViewById(R.id.image_view_thm);

        }
    }
}


