package com.healthcare.dogtraining.ui.chat;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.dogtraining.R;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    private List<GetFaqModel> getFaqModels;
    private Context context;
    String user_id,product_id;
    private boolean isclick1 = false;


    public FaqAdapter(List<GetFaqModel> getFaqModels, Context context) {
        this.getFaqModels = getFaqModels;
        this.context = context;

    }


    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.faqlist, parent, false);
        return new FaqAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final FaqAdapter.ViewHolder holder, final int position) {
        GetFaqModel getFaqModel = getFaqModels.get(position);

        System.out.print("<><nasmimdidj"+getFaqModel.getQuestion());

        holder.question1.setText(getFaqModels.get(position).getQuestion());

        holder.question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclick1) {
                    holder.ans1.setVisibility(View.GONE);
                    holder.question1.setText(getFaqModels.get(position).getQuestion());

                    holder.question1.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_baseline_expand, //left
                            0);

                    isclick1 = false;
                } else {
                    holder.ans1.setVisibility(View.VISIBLE);
                    holder.ans1.setText(getFaqModels.get(position).getAnswer());
                    holder.ans1.setText(Html.fromHtml(Html.fromHtml(getFaqModels.get(position).getAnswer()).toString()));
                    holder.question1.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_baseline_expand_less, //left
                            0);
                    isclick1 = true;
                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return getFaqModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView question1,ans1;
        public ViewHolder(View parent) {
            super(parent);
            question1=parent.findViewById(R.id.question1);
            ans1=parent.findViewById(R.id.ans1);

        }
    }



}


