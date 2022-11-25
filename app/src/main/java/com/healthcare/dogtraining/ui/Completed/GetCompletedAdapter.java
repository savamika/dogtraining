package com.healthcare.dogtraining.ui.Completed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.Refund.Activity_refund;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class GetCompletedAdapter extends RecyclerView.Adapter<GetCompletedAdapter.ViewHolder> {
    Session1 session1;
    private final List<GetCompletedModel> getCompletedModels;
    private final Context context;

    public GetCompletedAdapter(List<GetCompletedModel> getCompletedModels, Context context) {
        this.getCompletedModels = getCompletedModels;
        this.context = context;
    }


    @NonNull
    @Override
    public GetCompletedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.completedlist, parent, false);
        return new GetCompletedAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetCompletedAdapter.ViewHolder holder, final int position) {
        GetCompletedModel upcomingOrderhistoryModel = getCompletedModels.get(position);
        holder.txtname.setText(getCompletedModels.get(position).getName());
        holder.disocunt_price.setText("₹" + getCompletedModels.get(position).getDiscounted_price());

          if (getCompletedModels.get(position).getPrice() != null && !getCompletedModels.get(position).getPrice().isEmpty() && !getCompletedModels.get(position).getPrice().equals("null") && !getCompletedModels.get(position).getPrice().equals("0")) {
              holder.ed_mrp.setText("₹" + getCompletedModels.get(position).getPrice());
              holder.ed_mrp.setPaintFlags(holder.ed_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

          }else {
              holder.ed_mrp.setVisibility(View.GONE);
          }

        holder.txtqty.setText(getCompletedModels.get(position).getQuanitity());
        holder.tv_color.setText("Color : " + getCompletedModels.get(position).getColor());

        holder.txtorid.setText("Orderid : " + getCompletedModels.get(position).getOrder_id());
        holder.txtdate.setText("Date : " + getCompletedModels.get(position).getDate_time());
        String age = getCompletedModels.get(position).getAge();
        if (age != null && !age.isEmpty() && !age.equals("null") && !age.equals("0")) {
            holder.tv_age.setText("Age : " + getCompletedModels.get(position).getAge());
        } else {
            holder.tv_age.setVisibility(View.GONE);
        }

        if (getCompletedModels.get(position).getIs_refund().equalsIgnoreCase("0")){


            holder.txtrefund.setText("Refund");
            holder.txtrefund.setClickable(false);

        }else {

            holder.txtrefund.setText("Refund requested");
            holder.txtrefund.setClickable(true);

        }

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

        holder.txtrefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, Activity_refund.class);
                intent.putExtra("id", getCompletedModels.get(position).getId());
                intent.putExtra("prid", getCompletedModels.get(position).getProduct_id());

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return getCompletedModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtname, disocunt_price, ed_mrp, txtorid, txtqty, tv_color, tv_age, txtdate, txtrefund;
LinearLayout Cartcancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            txtname = itemView.findViewById(R.id.txtname);
            disocunt_price = itemView.findViewById(R.id.disocunt_price);
            ed_mrp = itemView.findViewById(R.id.ed_mrp);
            txtorid = itemView.findViewById(R.id.txtorid);
            txtqty = itemView.findViewById(R.id.txtqty);
            tv_color = itemView.findViewById(R.id.tv_color);
            tv_age = itemView.findViewById(R.id.tv_age);
            txtorid = itemView.findViewById(R.id.txtorid);
            txtdate = itemView.findViewById(R.id.txtdate);
            txtrefund = itemView.findViewById(R.id.txtrefund);
            Cartcancel = itemView.findViewById(R.id.Cartcancel);
        }
    }


}

