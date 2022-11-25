package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artjimlop.altex.AltexImageDownloader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.healthcare.dogtraining.Model.PurchaseplanInsideData;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.ui.TRAININGCOURSE.InstructionsActivity;
import com.healthcare.dogtraining.ui.TRAININGCOURSE.PurchasedPlanActivity;
import com.healthcare.dogtraining.ui.TRAININGCOURSE.VideoVIewActivity;
import com.healthcare.dogtraining.ui.chat.ChatActivity;
import com.healthcare.dogtraining.ui.chat.Plan_purchasedChatActivity;
import com.healthcare.dogtraining.utils.Session1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Commandpath;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;
import static com.healthcare.dogtraining.API.Base_Url.get_certificate;

public class PurchasedCommandPlanListAdapter extends RecyclerView.Adapter<PurchasedCommandPlanListAdapter.ViewHolder> {
    private final List<PurchaseplanInsideData> purchasedPlanModels;
    private final Context context;
    Session1 session1;
    String certificate_file;

    public PurchasedCommandPlanListAdapter(List<PurchaseplanInsideData> purchasedPlanModels, Context context) {
        this.purchasedPlanModels = purchasedPlanModels;
        this.context = context;
        notifyDataSetChanged();


    }


    @Override
    public PurchasedCommandPlanListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchaseplanlist_adapter, parent, false);
        return new PurchasedCommandPlanListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PurchasedCommandPlanListAdapter.ViewHolder holder, final int position) {
        session1=new Session1(context);




        final PurchaseplanInsideData allCommunityModel = purchasedPlanModels.get(position);
        Log.e("TAG", "onBindViewHolder: "+purchasedPlanModels.get(position).getCourse_trining_name() );
        Log.e("TAG", "onBindViewHolder: "+session1.getAddressID() );

        if (purchasedPlanModels.get(position).getCertificate_status().equalsIgnoreCase("true")){
            if (session1.getAddressID().equalsIgnoreCase(purchasedPlanModels.get(position).getCourse_trining_name())){
                //session1.setAddressID(context, purchasedPlanModels.get(position).getCourse_trining_name());
                holder.download_certificate_text.setVisibility(View.GONE);
                holder._certificate_text.setVisibility(View.GONE);

            }else{
                session1.setAddressID(context, purchasedPlanModels.get(position).getCourse_trining_name());
                if (!purchasedPlanModels.get(position).getCirtificate().equalsIgnoreCase("")){
                    holder.download_certificate_text.setVisibility(View.VISIBLE);
                    holder._certificate_text.setVisibility(View.GONE);
                }else{
                    holder.download_certificate_text.setVisibility(View.GONE);
                    holder._certificate_text.setVisibility(View.GONE);
                }

            }

        }else{
            if (session1.getAddressID().equalsIgnoreCase(purchasedPlanModels.get(position).getCourse_trining_name())){
                holder._certificate_text.setVisibility(View.GONE);
            }else{
                session1.setAddressID(context, purchasedPlanModels.get(position).getCourse_trining_name());
                holder._certificate_text.setVisibility(View.VISIBLE);
            }
            holder.download_certificate_text.setVisibility(View.GONE);

        }
        holder.tv_coursedname.setText(purchasedPlanModels.get(position).getCourse_trining_name());
        holder.tv_commandname.setText(purchasedPlanModels.get(position).getName());
        System.out.println("PurchasedCommandPlanListAdapter name   " + purchasedPlanModels.get(position).getCommand_name());
        if (purchasedPlanModels.get(position).getImage() != null && !purchasedPlanModels.get(position).getImage().equalsIgnoreCase("")) {

            final String img_url = Commandpath + purchasedPlanModels.get(position).getImage();
            RequestOptions requestOptions1 = new RequestOptions();
            requestOptions1.isMemoryCacheable();
            System.out.println("purchase comand plan adapter image------      " + purchasedPlanModels.get(position).getImage());
            try {
                Glide.with(context).setDefaultRequestOptions(requestOptions1)
                        .load(img_url)
                        .into(holder.plan_image);
            } catch (Exception e) {


            }


        }


        holder.tv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchasedPlanModels.get(position).getVideo() != null &&
                        !purchasedPlanModels.get(position).getVideo().equalsIgnoreCase("")) {
                    String vidAddress = Commandpath + purchasedPlanModels.get(position).getVideo();
                    Intent intent = new Intent(context, VideoVIewActivity.class);
                    intent.putExtra("video_url", vidAddress);
                    context.startActivity(intent);


                } else {
                    Toast.makeText(context, "no video yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.tv_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchasedPlanModels.get(position).getAudio() != null &&
                        !purchasedPlanModels.get(position).getAudio().equalsIgnoreCase("")) {
                    String audio = Commandpath + purchasedPlanModels.get(position).getAudio();
                    Intent intent = new Intent(context, VideoVIewActivity.class);
                    intent.putExtra("video_url", audio);
                    context.startActivity(intent);

                } else {

                    Toast.makeText(context, "no audio yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.tv_instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ID", "onClick: "+purchasedPlanModels.get(position).getId() );
                Log.e("ID", "onClick: "+purchasedPlanModels.get(position).getCourse_trining_id() );
                Intent intent = new Intent(context, InstructionsActivity.class);
                intent.putExtra("ID", purchasedPlanModels.get(position).getCourse_trining_id());
                intent.putExtra("command_id", purchasedPlanModels.get(position).getId());
                intent.putExtra("command_name", purchasedPlanModels.get(position).getName());
                context.startActivity(intent);
            }
        });

        holder.tv_discussionpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("planname", purchasedPlanModels.get(position).getName());
                intent.putExtra("command_id", purchasedPlanModels.get(position).getCommand_id());
                context.startActivity(intent);
            }
        });

        holder.tv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("getPlan_order_id", "onClick: "+purchasedPlanModels.get(position).getPlan_order_id() );
                Log.e("getCourse_trining_id", "onClick: "+purchasedPlanModels.get(position).getCourse_trining_id() );
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("planname", purchasedPlanModels.get(position).getName());
                intent.putExtra("command_id", purchasedPlanModels.get(position).getCourse_trining_id());
                intent.putExtra("plan_order_id", purchasedPlanModels.get(position).getPlan_order_id());
                context.startActivity(intent);
            }
        });

        holder.download_certificate_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(v, "Certificate Downloading", Snackbar.LENGTH_LONG);
                snackbar.show();
                AltexImageDownloader.writeToDisk(context, "https://sssdt.com/DogsTraining/assets/images/certificate/"+purchasedPlanModels.get(position).getCirtificate(), "Training Certificate");

                // GetCertificate(purchasedPlanModels.get(position).getCourse_trining_id(),purchasedPlanModels.get(position).getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return purchasedPlanModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_commandname,_certificate_text,download_certificate_text,tv_coursedname, tv_video, tv_instructions, tv_audio, tv_discussionpanel, tv_chat,tv_certificate;
        ImageView plan_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_coursedname = itemView.findViewById(R.id.tv_coursedname);
            download_certificate_text = itemView.findViewById(R.id.download_certificate_text);
            tv_commandname = itemView.findViewById(R.id.tv_commandname);
            plan_image = itemView.findViewById(R.id.plan_image);
            tv_video = itemView.findViewById(R.id.tv_video);
            tv_instructions = itemView.findViewById(R.id.tv_instructions);
            tv_audio = itemView.findViewById(R.id.tv_audio);
            tv_chat = itemView.findViewById(R.id.tv_chat);
            tv_discussionpanel = itemView.findViewById(R.id.tv_discussionpanel);
            tv_certificate = itemView.findViewById(R.id.tv_certificate);
            _certificate_text = itemView.findViewById(R.id._certificate_text);

        }

    }

    private void GetCertificate(String course_trining_id, String id) {

        PurchasedPlanActivity.   progress_view.setVisibility(View.INVISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + get_certificate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG", "onResponse: "+response);
                        PurchasedPlanActivity.   progress_view.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            System.out.println("<><===getprofile" + jsonObject);
                            if (jsonObject.optString("result").equals("true")) {
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                     JSONObject object=jsonArray.getJSONObject(i);
                                      certificate_file=object.getString("certificate_file");
                                     Log.e("TAG", "onResponse: "+certificate_file);


                                }
                                try {
                                    AltexImageDownloader.writeToDisk(context, "https://sssdt.com/DogsTraining/assets/images/certificate/"+certificate_file, "Training Certificate");


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            PurchasedPlanActivity.   progress_view.setVisibility(View.GONE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PurchasedPlanActivity.   progress_view.setVisibility(View.GONE);

                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("course_id",id);
                params.put("course_training_id",course_trining_id);
                params.put("user_id",session1.getUserId());
                System.out.println("id=======       " +params.toString());
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(stringRequest);
    }


}





