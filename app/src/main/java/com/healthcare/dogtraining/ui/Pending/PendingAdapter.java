package com.healthcare.dogtraining.ui.Pending;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.HomeActivity;
import com.healthcare.dogtraining.LoginActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.VendorSignup.AddVendorActivity;
import com.healthcare.dogtraining.ui.Upcoming.UpcomingAdapter;
import com.healthcare.dogtraining.ui.Upcoming.UpcomingOrderhistoryModel;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.cancel_order;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;
import static com.healthcare.dogtraining.API.Base_Url.get_notification_promolistcount;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder> {
    private List<PendingModel> pendingModels;
    private Context context;
    Session1 session1;

    public  PendingAdapter(List<PendingModel> pendingModels, Context context) {
        this.pendingModels = pendingModels;
        this.context = context;
    }


    @NonNull
    @Override
    public PendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pendinglist, parent, false);
        return new PendingAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingAdapter.ViewHolder holder, final int position) {
        PendingModel upcomingOrderhistoryModel=pendingModels.get(position);
        holder.txtname.setText(pendingModels.get(position).getName());
        holder.disocunt_price.setText("₹"+pendingModels.get(position).getDiscounted_price());
        holder.ed_mrp.setText("₹"+pendingModels.get(position).getPrice());
        holder.ed_mrp.setPaintFlags(holder.ed_mrp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtqty.setText(pendingModels.get(position).getQuanitity());
        holder.tv_color.setText("Color : "+pendingModels.get(position).getColor());

        holder.txtorid.setText("Orderid : "+pendingModels.get(position).getOrder_id());
        holder.txtdate.setText("Date : "+pendingModels.get(position).getDate_time());


String age=pendingModels.get(position).getAge();
        if (age != null && !age.isEmpty() && !age.equals("null") && !age.equals("0")) {
            holder.tv_age.setText("Age : "+pendingModels.get(position).getAge());
        }else {
            holder.tv_age.setVisibility(View.GONE);
        }
        session1=new Session1(context);
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

        holder.Cartcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialogView = new Dialog(context);
                dialogView.setContentView(R.layout.logout_dailog_view);
                dialogView.setCancelable(false);
                dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogView.show();
                CardView yesbutton=dialogView.findViewById(R.id.yesbutton);
                TextView nobutton=dialogView.findViewById(R.id.nobutton);
                yesbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogView.dismiss();

                        String orderid=pendingModels.get(position).getId();
                        CartCancelcallApi(orderid,position);
                    }
                });  nobutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      dialogView.dismiss();
                    }
                });


            }
        });
    }

        private void CartCancelcallApi(String orderid, int position) {
        String url = BaseUrl + cancel_order ;
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    String result = obj.getString("result");
                                    String msg = obj.getString("msg");
                                    System.out.print("cancelorder"+obj);
                                    if (result.equalsIgnoreCase("true")) {
                                        pendingModels.remove(position);
                                        notifyDataSetChanged();
                                        showdialog();
                                    } else {
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },

                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("order_id",orderid);
                        params.put("user_id",session1.getUserId());
                        Log.e("CANCEL OREDER--=-=    ", "getParams: "+params );
                        return params;
                       }
                       };
                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
                       }








                       @Override
        public int getItemCount() {
        return pendingModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtname,disocunt_price,ed_mrp,txtorid,txtqty,tv_color,tv_age,txtdate;
        LinearLayout Cartcancel;

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
            Cartcancel=itemView.findViewById(R.id.Cartcancel);
              }

              }




    private void showdialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Order cancelled successfully");
        builder1.setCancelable( true);
        builder1.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

           /* builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });*/


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
