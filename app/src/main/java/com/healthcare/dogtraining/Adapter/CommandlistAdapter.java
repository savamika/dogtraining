package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.dogtraining.PacagedetailsActivity;
import com.healthcare.dogtraining.R;

import java.util.List;

public class CommandlistAdapter extends RecyclerView.Adapter<CommandlistAdapter.ViewHolder> {
    private static final String TAG = "CommandlistAdapter";
    private final List<Command_model> sizedetailsmodels;
    private final Context context;
    Onitemclick onitemclick;
    int selectedPosition = -1;
    int clickable = 0;

//skybackground


    public CommandlistAdapter(List<Command_model> sizedetailsmodels, Context context, Onitemclick onitemclick) {
        this.sizedetailsmodels = sizedetailsmodels;
        this.context = context;
        this.onitemclick = onitemclick;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dogcourseforamte
                , parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Command_model command_model = sizedetailsmodels.get(position);
        Log.e("TAG", "onBindViewHolder: " + position);
        if (position == 0) {
            holder.plan_name.setText(command_model.getPlan_name());
        } else {
            holder.plan_name.setVisibility(View.GONE);
        }

        holder.commandname.setText(command_model.getCommandname());

        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    int k = Integer.parseInt(sizedetailsmodels.get(position).getSelect_command_limit());
                    Log.e(TAG, "k: "+k );
                    Log.e(TAG, "ids.size:<<><>><    " + PacagedetailsActivity.ids.size());
                    if (PacagedetailsActivity.ids.size() + 1 > k) {

                        Log.e(TAG, "iids.size() + 1 > k---   "+ PacagedetailsActivity.ids.size());
holder.chk.setChecked(false);
                    }else {

                        onitemclick.Onitemclicktrial(sizedetailsmodels.get(position).getId(), sizedetailsmodels.get(position).getCourse_trining_id());

                    }


                }else {
                    if (isChecked == false){
                        onitemclick.Onitemclicktrial(sizedetailsmodels.get(position).getId(), sizedetailsmodels.get(position).getCourse_trining_id());

                    }
                }


            }
        });
    }


    @Override
    public int getItemCount() {
        return sizedetailsmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView plan_name, commandname;
        CheckBox chk;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plan_name = itemView.findViewById(R.id.plan_name);
            commandname = itemView.findViewById(R.id.commandname);
            chk = itemView.findViewById(R.id.chk);


        }
    }
}
