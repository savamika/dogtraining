package com.healthcare.dogtraining.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.healthcare.dogtraining.Adapter.AdapterNearbyModel;
import com.healthcare.dogtraining.Adapter.Onitemclick;
import com.healthcare.dogtraining.ProductDetailsActivity;
import com.healthcare.dogtraining.R;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class AdapterNewArrrival extends RecyclerView.Adapter<AdapterNewArrrival.ViewHolder> {
    private List<AdapterccesModel> contactModels = new ArrayList<>();
    Context context;
    AdapterccesModel contactModel;
    Activity fx;
    static List<String> dateselect = new ArrayList<>();

    Onitemclick onitemclick;


    public AdapterNewArrrival(List<AdapterccesModel> datum, Context context) {
        this.contactModels = datum;
        this.context = context;
        this.onitemclick = onitemclick;
    }


    @NonNull
    @Override
    public AdapterNewArrrival.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearbyproduct, parent, false);
        return new AdapterNewArrrival.ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNewArrrival.ViewHolder holder, int position) {
        if (contactModels.size() > 0) {
            contactModel = contactModels.get(position);
            holder.itemname.setText(contactModel.getName());
            holder.itemprice.setText("₹ "+contactModel.getDiscounted_price());
            if (contactModel.getPrice() != null && !contactModel.getPrice().isEmpty() && !contactModel.getPrice().equals("null") && !contactModel.getPrice().equals("0")) {
                holder.itemdiscpunt.setText("₹ "+contactModel.getPrice());
                holder.itemdiscpunt.setPaintFlags(holder.itemdiscpunt.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);


            }else {
                holder.itemdiscpunt.setVisibility(View.GONE);
            }
               Glide.with(context)
                    .load(categoryimagepasth + contactModels.get(position).getImage())
                    .into(holder.imageshow);

            System.out.println("<><><>setinal" + contactModel.getImage());
            holder.Martcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("id",contactModels.get(position).getId());
                    context.startActivity(intent);
                }
            });
            System.out.println("<><><>setinal" + contactModel.getImage());

        } else {
            Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        System.out.print("hhhhhhvvvvv" + contactModels.size());
        return contactModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemname, itemprice, itemdiscpunt;

        ImageView imageshow;
        CardView Martcard;


        public ViewHolder(View parent) {
            super(parent);
            Martcard = parent.findViewById(R.id.Martcard);
            imageshow = parent.findViewById(R.id.imageshow);
            itemname = parent.findViewById(R.id.itemname);
            itemprice = parent.findViewById(R.id.itemprice);
            itemdiscpunt = parent.findViewById(R.id.itemdiscpunt);


        }
    }


}
