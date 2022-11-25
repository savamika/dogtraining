package com.healthcare.dogtraining.ui.Upcoming;

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
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.Pending.PendingModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {
    private List<UpcomingOrderhistoryModel> upcomingOrderhistoryModels;
    private Context context;
    Session1 session1;

    public  UpcomingAdapter(List<UpcomingOrderhistoryModel> upcomingOrderhistoryModels, Context context) {
        this.upcomingOrderhistoryModels = upcomingOrderhistoryModels;
        this.context = context;
    }


    @NonNull
    @Override
    public UpcomingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_orderlist, parent, false);
        return new UpcomingAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UpcomingAdapter.ViewHolder holder, final int position) {
        UpcomingOrderhistoryModel upcomingOrderhistoryModel=upcomingOrderhistoryModels.get(position);
        holder.txtname.setText(upcomingOrderhistoryModels.get(position).getName());
        holder.disocunt_price.setText("₹"+upcomingOrderhistoryModels.get(position).getDiscounted_price());
        holder.ed_mrp.setText("₹"+upcomingOrderhistoryModels.get(position).getPrice());
        holder.ed_mrp.setPaintFlags(holder.ed_mrp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtqty.setText(upcomingOrderhistoryModels.get(position).getQuanitity());
        holder.tv_color.setText("Color : "+upcomingOrderhistoryModels.get(position).getColor());
        holder.tv_age.setText("Age : "+upcomingOrderhistoryModels.get(position).getAge());
        holder.txtorid.setText("Orderid : "+upcomingOrderhistoryModels.get(position).getOrder_id());
        holder.txtdate.setText("Date : "+upcomingOrderhistoryModels.get(position).getDate_time());


        Glide.with(context)
                .load(categoryimagepasth + upcomingOrderhistoryModel.getImage())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .dontTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .skipMemoryCache(true)
                .apply(new RequestOptions().override(600, 200)).into(holder.img);

    }


    @Override
    public int getItemCount() {
        return upcomingOrderhistoryModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtname,disocunt_price,ed_mrp,txtorid,txtqty,tv_color,tv_age,txtdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             img=itemView.findViewById(R.id.img);
             txtname=itemView.findViewById(R.id.txtname);
             disocunt_price=itemView.findViewById(R.id.disocunt_price);
             ed_mrp=itemView.findViewById(R.id.ed_mrp);
             txtorid=itemView.findViewById(R.id.txtorid);
             txtqty=itemView.findViewById(R.id.txtqty);
             tv_color=itemView.findViewById(R.id.tv_color);
             tv_age=itemView.findViewById(R.id.tv_age);
             txtorid=itemView.findViewById(R.id.txtorid);
             txtdate=itemView.findViewById(R.id.txtdate);


        }
    }
}
