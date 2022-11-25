package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.GetImagesModel;
import com.healthcare.dogtraining.Model.Imagemodel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class ImagesAdapteradoptiondetails extends RecyclerView.Adapter<ImagesAdapteradoptiondetails.ViewHolder> {
    private List<GetImagesModel> getImagesModels;
    private Context context;
    Session1 session1;

    public ImagesAdapteradoptiondetails(List<GetImagesModel> getImagesModels, Context context) {
        this.getImagesModels = getImagesModels;
        this.context = context;
    }


    @NonNull
    @Override
    public ImagesAdapteradoptiondetails.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagesadoptionlistadapter, parent, false);
        return new ImagesAdapteradoptiondetails.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagesAdapteradoptiondetails.ViewHolder holder, final int position) {
        final GetImagesModel allCommunityModel = getImagesModels.get(position);

/*

        if(!allCommunityModel.getImages().isEmpty()&&
                allCommunityModel.getImages()!=null&&
        !allCommunityModel.getImages().equals("")){
*/

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



       /* }else{

        }
*/

    }






    @Override
    public int getItemCount() {
        return getImagesModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.roundedimage);

        }
    }
}

