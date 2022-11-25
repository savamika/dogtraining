package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.dogtraining.Model.TimeSlot_Vacc;
import com.healthcare.dogtraining.Model.VaccinationTimeDatum;
import com.healthcare.dogtraining.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DogTrainingCoursesDetailsAdapter extends RecyclerView.Adapter<DogTrainingCoursesDetailsAdapter.ViewHolder> {
    private List<TimeSlot_Vacc> contactModels = new ArrayList<>();
    Context context;
    TimeSlot_Vacc contactModel;
    Session session;
    public static HashSet<String> timeselect_nursing=new HashSet<>();
    Onitemclick onitemclick;
    int check = 0;

    public DogTrainingCoursesDetailsAdapter(List<TimeSlot_Vacc> timeSlots, Context context,Onitemclick onitemclick) {
        this.contactModels = timeSlots;
        this.context=context;
        this.onitemclick=onitemclick;

    }

    @NonNull
    @Override
    public DogTrainingCoursesDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coursedetails, parent, false);
        return new DogTrainingCoursesDetailsAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull DogTrainingCoursesDetailsAdapter.ViewHolder holder, final int position) {
        if (contactModels.size() > 0) {
            contactModel = contactModels.get(position);
            holder.commandname.setText(contactModel.getCommand_name());
            String chars = capitalize(contactModel.getCommand_name());
            System.out.println("<><><>setinal"+contactModel.getCommand_name());
            holder.commandname.setText(chars);


            holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        check ++;

                       /* if(check>=3){
                            System.out.print("<><===checkcndtn"+check);
                            isChecked=false;


                        }else{*/
                            onitemclick.Onitemclicktrial(contactModels.get(position).getId(),contactModels.get(position).getCourse_trining_id());
                            System.out.print("<><===checkelselll"+check);
                       // }
                        }

                    else
                    {
                        check --;
                        onitemclick.Onitemclicktrial(contactModels.get(position).getId(),contactModels.get(position).getCourse_trining_id());
                    }

                    if(check >= 4)
                    {

                        Toast.makeText(context, "Please Select only 4 Plans", Toast.LENGTH_SHORT).show();

                    }
                    }
                    });



          /*  holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dayId = contactModels.get(position).getDay_id();
                    String availStartTym = contactModels.get(position).getAvail_start_tym();
                    String availEndTym = contactModels.get(position).getAvail_end_tym();
                    String available_time_id=contactModels.get(position).getId();

                    StringBuilder resultday = new StringBuilder();
                    resultday.append(available_time_id);
                    System.out.println("<><><availbaletym"+resultday);

                    Intent intent=new Intent(context, SelectDrTimeActivity.class);
                    intent.putExtra("dayId",dayId);
                    intent.putExtra("Intent","Update");
                    intent.putExtra("availStartTym",availStartTym);
                    intent.putExtra("availEndTym",availEndTym);
                    intent.putExtra("available_time_id",available_time_id);
                    context.startActivity(intent);


                }
            });
*/

        } else {
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
        return contactModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView commandname;
        CheckBox chk;

        public ViewHolder(View parent) {
            super(parent);
            commandname=parent.findViewById(R.id.commandname);
            chk=parent.findViewById(R.id.chk);


        }
    }


}


