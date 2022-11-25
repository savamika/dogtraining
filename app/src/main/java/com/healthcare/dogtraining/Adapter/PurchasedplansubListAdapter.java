package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.dogtraining.Model.PurchasedPlanModel;
import com.healthcare.dogtraining.Model.PurchaseplanInsideData;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.ui.TRAININGCOURSE.PurchasedPlanActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurchasedplansubListAdapter extends RecyclerView.Adapter<PurchasedplansubListAdapter.ViewHolder> {
    public static HashSet<String> timeselect_nursing = new HashSet<>();
    Context context;
    PurchasedPlanModel contactModel;
    Session session;
    Onitemclick onitemclick;
    int check = 0;
    PurchasedCommandPlanListAdapter adapter;
    private List<PurchasedPlanModel> contactModels = new ArrayList<>();
    private List<PurchaseplanInsideData> InModels = new ArrayList<>();

    public PurchasedplansubListAdapter(List<PurchasedPlanModel> vaccinationTimeDatum, List<PurchaseplanInsideData> Insidedata, PurchasedPlanActivity purchasedPlanActivity) {
        this.contactModels = vaccinationTimeDatum;
        this.InModels = Insidedata;
        this.context = purchasedPlanActivity;
        // this.onitemclick=onitemclick;

    }


    @NonNull
    @Override
    public PurchasedplansubListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchaseplan_adapter, parent, false);
        return new PurchasedplansubListAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull PurchasedplansubListAdapter.ViewHolder holder, final int position) {
        System.out.println("PurchasedplansubListAdapter--name---      " + contactModels.get(position).getName());


        System.out.println("contactModels.size()-------       " + contactModels.size());
//        Toast.makeText(context, " "+contactModels.get(position).getName(), Toast.LENGTH_SHORT).show();


        contactModel = contactModels.get(position);
        holder.coursename.setText(contactModels.get(position).getName());
        adapter = new PurchasedCommandPlanListAdapter(InModels, context);
        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.recyclerviewsubplan.setLayoutManager(manager);
        holder.recyclerviewsubplan.setAdapter(adapter);


        if (contactModels.size() > 0) {


        } else {
            Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();
        }


    }

    private String capitalize(String day) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(day);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }


    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView coursename;
        CheckBox chk;
        RecyclerView recyclerviewsubplan;

        public ViewHolder(View parent) {
            super(parent);
            recyclerviewsubplan = parent.findViewById(R.id.recyclerviewsubplan);
            coursename = parent.findViewById(R.id.coursename);


        }
    }


}



