package com.healthcare.dogtraining.Adapter;

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
import com.healthcare.dogtraining.ProductDetailsActivity;
import com.healthcare.dogtraining.R;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class AdapterNearBY extends RecyclerView.Adapter<AdapterNearBY.ViewHolder> {
    static List<String> dateselect = new ArrayList<>();
    Context context;
    AdapterNearbyModel contactModel;
    Activity fx;
    DogTrainingCoursesDetailsAdapter dogTrainingCoursesDetailsAdapter;
    Onitemclick onitemclick;
    private List<AdapterNearbyModel> contactModels = new ArrayList<>();


    public AdapterNearBY(List<AdapterNearbyModel> datum, Context context) {
        this.contactModels = datum;
        this.context = context;
        this.onitemclick = onitemclick;
    }


    @NonNull
    @Override
    public AdapterNearBY.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearbyproduct, parent, false);
        return new AdapterNearBY.ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNearBY.ViewHolder holder, int position) {
        if (contactModels.size() > 0) {
            contactModel = contactModels.get(position);
            holder.itemname.setText(contactModel.getName());
            holder.itemprice.setText("₹ " + contactModel.getDiscounted_price());


            if (contactModel.getPrice() != null && !contactModel.getPrice().isEmpty() && !contactModel.getPrice().equals("null") && !contactModel.getPrice().equals("0")) {
                holder.itemdiscpunt.setText("₹ " + contactModel.getPrice());
                holder.itemdiscpunt.setPaintFlags(holder.itemdiscpunt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


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
                    intent.putExtra("id", contactModels.get(position).getId());
                    context.startActivity(intent);
                }
            });

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
            imageshow = parent.findViewById(R.id.imageshow);
            itemname = parent.findViewById(R.id.itemname);
            itemprice = parent.findViewById(R.id.itemprice);
            itemdiscpunt = parent.findViewById(R.id.itemdiscpunt);
            Martcard = parent.findViewById(R.id.Martcard);


        }
    }


}
