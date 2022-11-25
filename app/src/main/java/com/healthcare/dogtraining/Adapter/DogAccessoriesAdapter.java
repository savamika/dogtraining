package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.DogAccessoriesModel;
import com.healthcare.dogtraining.Model.DogMartModel;
import com.healthcare.dogtraining.ProductlistActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class DogAccessoriesAdapter extends RecyclerView.Adapter<DogAccessoriesAdapter.ViewHolder> implements Filterable {
    private List<DogAccessoriesModel> dogAccessoriesModelss;
    private List<DogAccessoriesModel> contactListFiltered;
    private Context context;
    Session1 session1;

    public DogAccessoriesAdapter(List<DogAccessoriesModel> dogAccessoriesModels, Context context) {
        this.dogAccessoriesModelss = dogAccessoriesModels;
        this.contactListFiltered = dogAccessoriesModels;
        this.context = context;
    }


    @NonNull
    @Override
    public DogAccessoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dogmartlist, parent, false);
        return new DogAccessoriesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DogAccessoriesAdapter.ViewHolder holder, final int position) {
        final DogAccessoriesModel allCommunityModel = contactListFiltered.get(position);
        holder.tv_name.setText(contactListFiltered.get(position).getName());

        Glide.with(context)
                .load(categoryimagepasth + contactListFiltered.get(position).getImage())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .dontTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .skipMemoryCache(true)
                .apply(new RequestOptions().override(600, 200)).into(holder.dog_image);


        holder.Productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductlistActivity.class);
                intent.putExtra("cat_id", contactListFiltered.get(position).getId());
                intent.putExtra("Product_Type", contactListFiltered.get(position).getProduct_type());
                context.startActivity(intent);

            }
        });


    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();

                if (charString.isEmpty()) {
                    contactListFiltered = dogAccessoriesModelss;
                } else {

                    List<DogAccessoriesModel> filterList = new ArrayList<>();

                    for (DogAccessoriesModel data : dogAccessoriesModelss) {

                        if (data.getName().toLowerCase().contains(charString)) {
                            filterList.add(data);
                        }
                    }

                    contactListFiltered = filterList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                contactListFiltered = (List<DogAccessoriesModel>) results.values;
                notifyDataSetChanged();
            }
        };

    }


    @Override
    public int getItemCount() {
        return dogAccessoriesModelss.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView dog_image;
        LinearLayout Productlist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            dog_image = itemView.findViewById(R.id.dog_image);
            Productlist = itemView.findViewById(R.id.Productlist);
        }
    }
}


