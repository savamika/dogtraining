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
import com.healthcare.dogtraining.Model.BreedinfoModel;
import com.healthcare.dogtraining.Model.DogMartModel;
import com.healthcare.dogtraining.ProductlistActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.BREEDINFO.BridinfoDetailsActivity;


import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class BreedinfoAdapter extends RecyclerView.Adapter<BreedinfoAdapter.ViewHolder> {
    private List<BreedinfoModel> breedinfoModels;
    private Context context;
    Session1 session1;

    public BreedinfoAdapter(List<BreedinfoModel> breedinfoModels, Context context) {
        this.breedinfoModels = breedinfoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BreedinfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.breedinfo_list, parent, false);
        return new BreedinfoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BreedinfoAdapter.ViewHolder holder, final int position) {
        final BreedinfoModel allCommunityModel = breedinfoModels.get(position);
        try {
            String upperString = allCommunityModel.getBreed().substring(0, 1).toUpperCase() + allCommunityModel.getBreed().substring(1).toLowerCase();
            holder.tv_breedname.setText(upperString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_breedname.setVisibility(View.GONE);
        holder.pet_name.setText("Breed name :" + allCommunityModel.getName());
        holder.tv_age.setText("Life span :" + allCommunityModel.getAge());
        holder.tv_weight.setText("Weight :" + allCommunityModel.getWeight());
        holder.tvheight.setText("Height :" + allCommunityModel.getCreated_at());
        holder.tvdesc.setText("Description :" + allCommunityModel.getUpdeted_at());

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

        holder.trainingpacage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* EasyTransitionOptions options =
                        EasyTransitionOptions.makeTransitionOptions(context,
                                findViewById(R.id.iv_icon),
                                findViewById(R.id.tv_name)); // add as many views as you like
*/
               Intent intent = new Intent(context, BridinfoDetailsActivity.class);
                intent.putExtra("id",allCommunityModel.getId());
               context.startActivity(intent);
            }
        });

    }
        @Override
        public int getItemCount() {
        return breedinfoModels.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pet_image;
        TextView tv_breedname,pet_name,tv_age,tv_weight,tvdesc,tvheight;
        LinearLayout trainingpacage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pet_image=itemView.findViewById(R.id.pet_image);
            tv_breedname=itemView.findViewById(R.id.tv_breedname);
            pet_name=itemView.findViewById(R.id.pet_name);
            tv_age=itemView.findViewById(R.id.tv_age);
            tv_weight=itemView.findViewById(R.id.tv_weight);
            trainingpacage =itemView.findViewById(R.id.trainingpacage);

            tvdesc=itemView.findViewById(R.id.tvdesc);
                    tvheight=itemView.findViewById(R.id.tvheight);
        }
    }
}

