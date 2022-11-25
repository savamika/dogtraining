package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.NOtificationModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.ui.firebase.NotificationActivity;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.Notificationpath;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    List<NOtificationModel> nOtificationModels;

    public NotificationAdapter(List<NOtificationModel> nOtificationModels, NotificationActivity notificationActivity) {

        this.context=notificationActivity;
        this.nOtificationModels=nOtificationModels;

    }


    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationlayout, parent, false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {
        NOtificationModel specialityData1 = nOtificationModels.get(position);
        holder.title.setText(nOtificationModels.get(position).getTitle());
        holder.description.setText(nOtificationModels.get(position).getDescription());
        Glide.with(context)
                .load(Notificationpath + nOtificationModels.get(position).getImage())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .dontTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .skipMemoryCache(true)
                .apply(new RequestOptions().override(600, 200)).into(holder.userimage);

    }

        @Override
        public int getItemCount() {
        return nOtificationModels.size();
         }

    public class ViewHolder extends RecyclerView.ViewHolder {
          TextView title,description;
          ImageView userimage;
        public ViewHolder( View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            userimage=itemView.findViewById(R.id.userimage);

        }

    }



}


