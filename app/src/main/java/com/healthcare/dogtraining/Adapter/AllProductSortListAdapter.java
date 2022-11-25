package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.healthcare.dogtraining.CartlistActivity;
import com.healthcare.dogtraining.Model.GetCategoryByidModel;
import com.healthcare.dogtraining.Model.SortProductModel;
import com.healthcare.dogtraining.ProductDetailsActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.add_to_cart;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class AllProductSortListAdapter extends RecyclerView.Adapter<AllProductSortListAdapter.ViewHolder> {
    private List<SortProductModel> sortProductModels;
    private Context context;
    Session1 session1;

    public AllProductSortListAdapter(List<SortProductModel> sortProductModels, Context context) {
        this.sortProductModels = sortProductModels;
        this.context = context;
    }


    @NonNull
    @Override
    public AllProductSortListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.productssortlist, parent, false);
        return new AllProductSortListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllProductSortListAdapter.ViewHolder holder, final int position) {
        final SortProductModel allCommunityModel = sortProductModels.get(position);
        holder.pet_name.setText(allCommunityModel.getName());
        holder.tv_colorname.setText(allCommunityModel.getColor());
        holder.tv_petage.setText(allCommunityModel.getAge());
        holder.tv_price.setText("₹" + allCommunityModel.getDiscounted_price());
        session1=new Session1(context);

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
                intent.putExtra("id",sortProductModels.get(position).getId());
                context.startActivity(intent);
                 }
                 });

        holder.addcartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productid=sortProductModels.get(position).getId();
                AddcartApi(productid);
               }
             });

    }

    private void AddcartApi(String productid) {
        System.out.println("<><productid"+productid);
        System.out.println("<><userid"+session1.getUserId());
        String url = BaseUrl + add_to_cart ;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id",session1.getUserId())
                .addBodyParameter("product_id",productid)
                .addBodyParameter("quantity","1")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            System.out.println("<><><><==="+jsonObject);
                            if (result.equalsIgnoreCase("true")) {
                                Intent intent = new Intent(context, CartlistActivity.class);
                                context.startActivity(intent);

                            } else {

                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error_my_join", anError.toString());
                    }
                });
    }






    @Override
    public int getItemCount() {
        return sortProductModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pet_name,tv_colorname,tv_petage,tv_price;
        ImageView product_image;
        LinearLayout Productdetails;
        Button addcartbtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pet_name=itemView.findViewById(R.id.pet_name);
            product_image=itemView.findViewById(R.id.product_image);
            tv_colorname=itemView.findViewById(R.id.tv_colorname);
            tv_petage=itemView.findViewById(R.id.tv_petage);
            tv_price=itemView.findViewById(R.id.tv_price);
            Productdetails=itemView.findViewById(R.id.Productdetails);
            addcartbtn=itemView.findViewById(R.id.addcartbtn);
        }
    }
}


