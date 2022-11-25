package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.healthcare.dogtraining.Model.GetCartModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;
import static com.healthcare.dogtraining.API.Base_Url.remove_cart;
import static com.healthcare.dogtraining.API.Base_Url.update_cart_quant;

public class GetCartListAdapter extends RecyclerView.Adapter<GetCartListAdapter.ViewHolder> {
    private List<GetCartModel> getCartModels;
    private Context context;
    int rowindex=-1;
    Session1 session1;
    Cartupdatelistner cartupdatelistner;

    public GetCartListAdapter(List<GetCartModel> getCartModels, Context context,Cartupdatelistner cartupdatelistner) {
        this.getCartModels = getCartModels;
        this.context = context;
        this.cartupdatelistner = cartupdatelistner;

    }

    @NonNull
    @Override
    public GetCartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allcartlist, parent, false);
        return new GetCartListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetCartListAdapter.ViewHolder holder, final int position) {
        final GetCartModel allCommunityModel = getCartModels.get(position);
        session1=new Session1(context);
        Glide.with(context)
                .load(categoryimagepasth+allCommunityModel.getImage())
                .centerCrop()
                .into(holder.product_image);
        String discount=allCommunityModel.getDiscounted_price();
        discount = discount.replaceAll("[-+^]*", "");

        holder.colorname.setText("Color:" + getCartModels.get(position).getColor());
        holder.tv_age.setText("Age:"+getCartModels.get(position).getAge());
        holder.selling_price.setText("₹"+allCommunityModel.getDiscounted_price());
        holder.quantity.setText(allCommunityModel.getQuantity());
        Float qty = Float.valueOf(holder.quantity.getText().toString());
        holder.product_name.setText(allCommunityModel.getName());



        if(getCartModels.get(position).getPrice().equals(getCartModels.get(position).getDiscounted_price())){
            holder.ed_mrp.setVisibility(View.GONE);


        }else{

            holder.ed_mrp.setVisibility(View.VISIBLE);
            holder.ed_mrp.setText("₹"+allCommunityModel.getPrice());
            holder.ed_mrp.setPaintFlags(holder.ed_mrp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }


           holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=getCartModels.get(position).getProduct_id();
                String cart_id=getCartModels.get(position).getCart_id();
                String squantity=getCartModels.get(position).getQuantity();
                int qty = Integer.valueOf(holder.quantity.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;
                    holder.quantity.setText(qty+"");
                }
                UpdateItemInCart(item_id,position,cart_id,squantity,String.valueOf(qty),holder.quantity);

            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item_id = getCartModels.get(position).getProduct_id();
                String cart_id = getCartModels.get(position).getCart_id();
                String squantity = getCartModels.get(position).getQuantity();
                int qty = Integer.valueOf(holder.quantity.getText().toString());
                qty = qty + 1;
                holder.quantity.setText(qty+"");
                UpdateItemInCart(item_id, position, cart_id, squantity,String.valueOf(qty), holder.quantity);
            }
            });

           holder.removeCartProductlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String cartid=getCartModels.get(position).getCart_id();
               RemoveCartApi(cartid,position);
             }
             });
             }

        private void UpdateItemInCart(String item_id, int position, String cart_id, String squantity, final String qty_nmbr, TextView quantity) {
        String url = BaseUrl +update_cart_quant ;
            AndroidNetworking.post(url)
                    .addBodyParameter("cart_id",cart_id)
                    .addBodyParameter("quantity",qty_nmbr)
                    .addBodyParameter("user_id",session1.getUserId() )
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                System.out.println("update cart-----------      "+jsonObject.toString());
                                String result = jsonObject.getString("result");
                                String msg = jsonObject.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                    String qty = jsonObject1.getString("quantity");
                                    getCartModels.get(position).setQuantity(qty);
                                    quantity.setText(qty);
                                    cartupdatelistner.onGeekEvent(true);
                                } else {

                                    Toast.makeText(context, "Product is out of stock.", Toast.LENGTH_SHORT).show();
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












    private void RemoveCartApi(String cartid, int position) {
                String url = BaseUrl + remove_cart ;
                AndroidNetworking.post(url)
                        .addBodyParameter("cart_id",cartid)
                        .addBodyParameter("user_id",session1.getUserId())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    String result = jsonObject.getString("result");
                                    String msg = jsonObject.getString("msg");
                                    System.out.println("<><====removecart"+jsonObject);
                                    if (result.equalsIgnoreCase("true")) {
                                        getCartModels.remove(position);
                                        notifyDataSetChanged();
                                        cartupdatelistner.onGeekEvent(true);
//
                                       } else {
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
        return getCartModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout removeCartProductlist;
        ImageView product_image,minus,plus;
        TextView product_name,colorname,tv_age,selling_price,ed_mrp,ed_product_off,quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image=itemView.findViewById(R.id.product_image);
            product_name=itemView.findViewById(R.id.product_name);
            colorname=itemView.findViewById(R.id.colorname);
            tv_age=itemView.findViewById(R.id.tv_age);
            selling_price=itemView.findViewById(R.id.selling_price);
            ed_mrp=itemView.findViewById(R.id.ed_mrp);
            ed_product_off=itemView.findViewById(R.id.ed_product_off);
            minus=itemView.findViewById(R.id.minus);
            quantity=itemView.findViewById(R.id.quantity);
            plus=itemView.findViewById(R.id.plus);
            removeCartProductlist=itemView.findViewById(R.id.removeCartProductlist);

        }
    }
}

