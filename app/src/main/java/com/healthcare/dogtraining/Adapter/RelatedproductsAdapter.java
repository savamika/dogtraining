package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.healthcare.dogtraining.Model.RelatedproductsModel;
import com.healthcare.dogtraining.ProductDetailsActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class RelatedproductsAdapter extends RecyclerView.Adapter<RelatedproductsAdapter.ViewHolder> {
    private List<RelatedproductsModel> relatedProductsModels;
    private Context context;




    public RelatedproductsAdapter (List<RelatedproductsModel> relatedProductsModels, Context context) {
        this.relatedProductsModels = relatedProductsModels;
        this.context = context;
        notifyDataSetChanged();



    }

    @NonNull
    @Override
    public RelatedproductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatedproducts, parent, false);
        return new RelatedproductsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RelatedproductsAdapter.ViewHolder holder, final int position) {
        final RelatedproductsModel allCommunityModel = relatedProductsModels.get(position);
        holder.pet_name.setText(allCommunityModel.getName());
        holder.tv_colorname.setText(allCommunityModel.getColor());
        holder.tv_petage.setText(allCommunityModel.getAge());
        holder.tv_price.setText("₹" + allCommunityModel.getDiscounted_price());


        Glide.with(context)
                .load(categoryimagepasth + allCommunityModel.getImage())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .dontTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .skipMemoryCache(true)
                .apply(new RequestOptions().override(600, 200))
                .into(holder.product_image);



        holder.Productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("id",relatedProductsModels.get(position).getId());
                context.startActivity(intent);
            }
        });


    }


        @Override
        public int getItemCount() {
        return relatedProductsModels.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pet_name,tv_colorname,tv_petage,tv_price;
        ImageView product_image;
        LinearLayout Productdetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pet_name=itemView.findViewById(R.id.pet_name);
            tv_colorname=itemView.findViewById(R.id.tv_colorname);
            tv_petage=itemView.findViewById(R.id.tv_petage);
            tv_price=itemView.findViewById(R.id.tv_price);
            product_image=itemView.findViewById(R.id.product_image);
            Productdetails=itemView.findViewById(R.id.Productdetails);


        }
    }




}





