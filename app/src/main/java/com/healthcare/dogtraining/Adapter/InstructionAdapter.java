package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.dogtraining.Model.InstructionModel;
import com.healthcare.dogtraining.Model.Trainingpackagemodel;
import com.healthcare.dogtraining.PacageListActivity;
import com.healthcare.dogtraining.R;

import java.util.List;

public class InstructionAdapter  extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {
    private List<InstructionModel> instructionModels;
    private Context context;


    public InstructionAdapter(List<InstructionModel> instructionModels, Context context) {
        this.instructionModels = instructionModels;
        this.context = context;
    }


    @NonNull
    @Override
    public InstructionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.instructionlist, parent, false);
        return new InstructionAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final InstructionAdapter.ViewHolder holder, final int position) {
        final InstructionModel allCommunityModel = instructionModels.get(position);

         holder.tv_description.setText(instructionModels.get(position).getInstruction());
         holder.tv_title.setText(instructionModels.get(position).getTitle());
    }







    @Override
    public int getItemCount() {
        return instructionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_description=itemView.findViewById(R.id.tv_description);


        }
    }
}

