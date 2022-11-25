package com.healthcare.dogtraining.Adapter;

import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.OrderhistoryModel;
import com.healthcare.dogtraining.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*
public class OrderhistoryAdapter extends RecyclerView.Adapter<OrderhistoryAdapter.ViewHolder> {
    private List<OrderhistoryModel> newArivalProductsModel;
    private Context context;


    public  OrderhistoryAdapter(List<OrderhistoryModel> newArivalProductsModel, Context context) {
        this.newArivalProductsModel = newArivalProductsModel;
        this.context = context;
    }


    @NonNull
    @Override
    public OrderhistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new NeworderhistoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderhistoryAdapter.ViewHolder holder, final int position) {
        final OrderHistoryProfileModel allCommunityModel = newArivalProductsModel.get(position);









    }



    @Override
    public int getItemCount() {
        return newArivalProductsModel.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);




        }
    }
}
*/
