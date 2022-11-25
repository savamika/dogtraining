package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.DogMartModel;
import com.healthcare.dogtraining.Model.Imagemodel;
import com.healthcare.dogtraining.ProductlistActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private List<Imagemodel> imagemodels;
    private Context context;
    Session1 session1;

    public ImagesAdapter(List<Imagemodel> imagemodels, Context context) {
        this.imagemodels = imagemodels;
        this.context = context;
    }


    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageslist, parent, false);
        return new ImagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagesAdapter.ViewHolder holder, final int position) {
        final Imagemodel allCommunityModel = imagemodels.get(position);

        Glide.with(context)
                .load(categoryimagepasth + allCommunityModel.getImages())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .dontTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .skipMemoryCache(true)
                .apply(new RequestOptions().override(600, 200)).into(holder.image);


    }






    @Override
    public int getItemCount() {
        return imagemodels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
      RoundedImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);

        }
    }
}

