package com.healthcare.dogtraining.ui.ADOPTION;

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
import com.healthcare.dogtraining.AdoptionDetailsActivity;
import com.healthcare.dogtraining.Model.AdoptionModel;
import com.healthcare.dogtraining.Model.BreedinfoModel;
import com.healthcare.dogtraining.R;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class AdoptionAdapter extends RecyclerView.Adapter<AdoptionAdapter.ViewHolder> {
    private List<AdoptionModel> adoptionModels;
    private Context context;


    public AdoptionAdapter(List<AdoptionModel> adoptionModels, Context context) {
        this.adoptionModels = adoptionModels;
        this.context = context;
    }


    @NonNull
    @Override
    public AdoptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adoption_list, parent, false);
        return new AdoptionAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdoptionAdapter.ViewHolder holder, final int position) {
        final AdoptionModel allCommunityModel = adoptionModels.get(position);

         holder.tv_breedname.setText(allCommunityModel.getBreed());
         holder.tv_name.setText("Name" + allCommunityModel.getName());
         holder.tv_age.setText("Age :" + allCommunityModel.getAge());
         holder.tv_gender.setText("Sex : " + allCommunityModel.getGender());

         Glide.with(context)
                .load(categoryimagepasth+allCommunityModel.getImage())
                .centerCrop()
                .into(holder.iv_icon);

         holder.trainingpacage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(context, AdoptionDetailsActivity.class);
                 intent.putExtra("ID",allCommunityModel.getId());
                 context.startActivity(intent);
             }
         });

    }






    @Override
    public int getItemCount() {
        return adoptionModels.size();
    }
       public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_breedname,tv_name,tv_age,tv_gender;
        LinearLayout trainingpacage;
        ImageView iv_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_breedname=itemView.findViewById(R.id.tv_breedname);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_age=itemView.findViewById(R.id.tv_age);
            tv_gender=itemView.findViewById(R.id.tv_gender);
            trainingpacage=itemView.findViewById(R.id.trainingpacage);
            iv_icon=itemView.findViewById(R.id.iv_icon);

        }
    }
}


