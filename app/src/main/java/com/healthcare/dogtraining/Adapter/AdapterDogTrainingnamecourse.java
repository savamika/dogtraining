package com.healthcare.dogtraining.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.dogtraining.Model.VaccinationTimeDatum;
import com.healthcare.dogtraining.PacagedetailsActivity;
import com.healthcare.dogtraining.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdapterDogTrainingnamecourse extends RecyclerView.Adapter<AdapterDogTrainingnamecourse.ViewHolder> {
    private List<VaccinationTimeDatum> contactModels = new ArrayList<>();
    Context context;
    VaccinationTimeDatum contactModel;
    Activity fx;
    static List<String> dateselect=new ArrayList<>();
    DogTrainingCoursesDetailsAdapter dogTrainingCoursesDetailsAdapter;
    Onitemclick onitemclick;



    public AdapterDogTrainingnamecourse(List<VaccinationTimeDatum> datum, PacagedetailsActivity activity,Onitemclick onitemclick) {
        this.contactModels=datum;
        this.context=activity;
        this.onitemclick=onitemclick;
    }


    @NonNull
    @Override
    public AdapterDogTrainingnamecourse.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dogcourseforamte, parent, false);
        return new AdapterDogTrainingnamecourse.ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDogTrainingnamecourse.ViewHolder holder, int position) {
        if (contactModels.size() > 0) {
            contactModel = contactModels.get(position);
            holder.plan_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            holder.plan_name.setText(contactModel.getPlan_name());
            String chars = capitalize(contactModel.getPlan_name());
            System.out.println("<><><>setinal"+contactModel.getPlan_name());
            holder.plan_name.setText(chars);


            dogTrainingCoursesDetailsAdapter=new DogTrainingCoursesDetailsAdapter(contactModel.getTimeSlotVaccs(), context,onitemclick);
            LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            holder.recyclerview.setLayoutManager(manager);
            holder.recyclerview.setAdapter(dogTrainingCoursesDetailsAdapter);
        }
        else {
            Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();
        }
    }

    private String capitalize(String day) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(day);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }






    @Override
    public int getItemCount() {
        System.out.print("hhhhhhvvvvv"+contactModels.size());
        return contactModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView plan_name;
        RecyclerView recyclerview;



        public ViewHolder(View parent) {
            super(parent);
            plan_name=parent.findViewById(R.id.plan_name);
            recyclerview=parent.findViewById(R.id.recyclerview);


        }
    }


}
