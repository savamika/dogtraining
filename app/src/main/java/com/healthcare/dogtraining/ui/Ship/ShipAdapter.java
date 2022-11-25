package com.healthcare.dogtraining.ui.Ship;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.Upcoming.UpcomingAdapter;
import com.healthcare.dogtraining.ui.Upcoming.UpcomingOrderhistoryModel;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class ShipAdapter extends RecyclerView.Adapter<ShipAdapter.ViewHolder> {
    private List<ShipOrderModel> shipOrderModels;
    private Context context;
    Session1 session1;

    public  ShipAdapter(List<ShipOrderModel> shipOrderModels, Context context) {
        this.shipOrderModels = shipOrderModels;
        this.context = context;
    }


    @NonNull
    @Override
    public ShipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ship_order_list, parent, false);
        return new ShipAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShipAdapter.ViewHolder holder, final int position) {
        ShipOrderModel upcomingOrderhistoryModel=shipOrderModels.get(position);
        holder.txtname.setText(shipOrderModels.get(position).getName());
        holder.disocunt_price.setText("₹"+shipOrderModels.get(position).getDiscounted_price());
        holder.ed_mrp.setText("₹"+shipOrderModels.get(position).getPrice());
        holder.ed_mrp.setPaintFlags(holder.ed_mrp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtqty.setText(shipOrderModels.get(position).getQuanitity());
        holder.tv_color.setText("Color : "+shipOrderModels.get(position).getColor());

        holder.txtorid.setText("Orderid : "+shipOrderModels.get(position).getOrder_id());
        holder.txtdate.setText("Date : "+shipOrderModels.get(position).getDate_time());
   String age=     shipOrderModels.get(position).getAge() ;
        if (age != null && !age.isEmpty() && !age.equals("null") && !age.equals("0")) {
            holder.tv_age.setText("Age : "+shipOrderModels.get(position).getAge());
        }else {
            holder.tv_age.setVisibility(View.GONE);
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

    }


    @Override
    public int getItemCount() {
        return shipOrderModels.size();
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
