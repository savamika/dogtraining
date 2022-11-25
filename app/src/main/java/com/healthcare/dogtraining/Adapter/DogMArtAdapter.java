package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.DogMartModel;
import com.healthcare.dogtraining.ProductlistActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class DogMArtAdapter extends RecyclerView.Adapter<DogMArtAdapter.ViewHolder> implements Filterable {
    private List<DogMartModel> dogMartModels;
    private List<DogMartModel> contactListFiltered;
    private Context context;
    Session1 session1;

    public DogMArtAdapter(List<DogMartModel> dogMartModels, Context context) {
        this.dogMartModels = dogMartModels;
        this.contactListFiltered = dogMartModels;
        this.context = context;
    }


    @NonNull
    @Override
    public DogMArtAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dogmartlist, parent, false);
        return new DogMArtAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DogMArtAdapter.ViewHolder holder, final int position) {
        final DogMartModel allCommunityModel = contactListFiltered.get(position);
        holder.tv_name.setText(allCommunityModel.getName());

        Glide.with(context)
                .load(categoryimagepasth + allCommunityModel.getImage())
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
                intent.putExtra("cat_id",contactListFiltered.get(position).getId());
                intent.putExtra("Product_Type",contactListFiltered.get(position).getProduct_type());
                context.startActivity(intent);

            }
        });
    }






    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView dog_image;
        LinearLayout Productlist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            dog_image=itemView.findViewById(R.id.dog_image);
            Productlist=itemView.findViewById(R.id.Productlist);
        }
    }

    @Override
    public Filter getFilter()   {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = dogMartModels;
                } else {
                    List<DogMartModel> filteredList = new ArrayList<>();
                    for (DogMartModel row : dogMartModels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<DogMartModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

