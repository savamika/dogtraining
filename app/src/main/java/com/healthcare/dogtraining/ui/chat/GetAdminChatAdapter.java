package com.healthcare.dogtraining.ui.chat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.dogtraining.Model.ChatMessage;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class GetAdminChatAdapter extends RecyclerView.Adapter<GetAdminChatAdapter.ViewHolder> {

    String user_id, product_id, wish_id;
    private List<ChatMessage> allCommunityModels;
    private Context context;
    private Session1 session;


    public GetAdminChatAdapter(Context context, List<ChatMessage> getAdminChatModels) {
        this.allCommunityModels = getAdminChatModels;
        this.context = context;
    }


    public void setFilter(List<ChatMessage> apointUserModelArrayList1) {
        allCommunityModels = new ArrayList<>();
        allCommunityModels.addAll(apointUserModelArrayList1);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GetAdminChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new GetAdminChatAdapter.ViewHolder(itemView);

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GetAdminChatAdapter.ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {

            session = new Session1(context);
            final ChatMessage allCommunityModel = allCommunityModels.get(position);
            Log.e("TAG", "SenderID: " + allCommunityModel.getSenderID());

            Log.e("TAG", "onBindViewHolder: " + session.getUserId());


            if (allCommunityModel.getSenderID().equalsIgnoreCase(session.getUserId())) {


                if (allCommunityModel.getMessage().equalsIgnoreCase("Image")) {
                    holder.admin_time.setVisibility(View.GONE);
                    holder.iv_photo_user.setVisibility(View.VISIBLE);
                    holder.usercard.setVisibility(View.VISIBLE);
                    holder.userlayout_layout.setVisibility(View.VISIBLE);
                    holder.iv_photo_user.setImageBitmap(convert(allCommunityModel.getImage()));

                    holder.my_time.setText("");

                    holder.customer_msg.setVisibility(View.GONE);
                    holder.image_play.setVisibility(View.GONE);

                    holder.admin_lin.setVisibility(View.GONE);
                    holder.iv_photo_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialogView = new Dialog(context);
                            dialogView.setContentView(R.layout.imageviewwer_dailog_view);
                            dialogView.setCancelable(false);
                            dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogView.show();
                            VideoView videoView = dialogView.findViewById(R.id.video_view);
                            videoView.setVisibility(View.GONE);
                            ImageView fulimageshow = dialogView.findViewById(R.id.fulimageshow);
                            CardView imagecard = dialogView.findViewById(R.id.imagecard);
                            fulimageshow.setVisibility(View.VISIBLE);
                            imagecard.setVisibility(View.VISIBLE);
                            fulimageshow.setImageBitmap(convert(allCommunityModel.getImage()));
                            ImageView cancel = dialogView.findViewById(R.id.cancel);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogView.dismiss();
                                    dialogView.cancel();
                                }
                            });

                        }
                    });

                } else if (allCommunityModel.getMessage().equalsIgnoreCase("Vid")) {
                    holder.iv_photo_user.setVisibility(View.GONE);
                    holder.usercard.setVisibility(View.VISIBLE);
                    holder.admin_time.setVisibility(View.GONE);
                    holder.image_play.setVisibility(View.VISIBLE);
                    holder.iv_photo_user.setVisibility(View.VISIBLE);
                    holder.userlayout_layout.setVisibility(View.VISIBLE);
                    holder.userlayout_layout.setBackgroundColor(context.getResources().getColor(R.color.gray));
                    holder.iv_photo_user.setImageBitmap(convert(allCommunityModel.getImage()));
                    holder.my_time.setText("");

                    holder.customer_msg.setVisibility(View.GONE);


                    holder.admin_lin.setVisibility(View.GONE);
                    holder.image_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialogView = new Dialog(context);
                            dialogView.setContentView(R.layout.imageviewwer_dailog_view);
                            dialogView.setCancelable(false);
                            dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogView.show();

                            VideoView videoView = dialogView.findViewById(R.id.video_view);
                            MediaController mediaController = new MediaController(context);
                            mediaController.setAnchorView(videoView);
                            videoView.setMediaController(mediaController);

                            Log.e("===", "My Send Video-: " + allCommunityModel.getVideo());



                            videoView.setVideoURI(Uri.parse(allCommunityModel.getVideo()));
                            videoView.requestFocus();
                            videoView.start();
                            ImageView fulimageshow = dialogView.findViewById(R.id.fulimageshow);
                            fulimageshow.setVisibility(View.GONE);
                            videoView.setVisibility(View.VISIBLE);
                            ImageView cancel = dialogView.findViewById(R.id.cancel);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogView.dismiss();
                                    dialogView.cancel();
                                }
                            });
                        }
                    });
                } else if (!allCommunityModel.getMessage().equalsIgnoreCase("")) {
                    holder.customer_msg.setText(allCommunityModel.getMessage());
                    holder.usrname.setText(allCommunityModel.getUsername());
                    holder.my_time.setText(allCommunityModel.getTime());
                    holder.iv_photo_user.setVisibility(View.GONE);
                    holder.image_play.setVisibility(View.GONE);
                    holder.admin_time.setVisibility(View.GONE);
                    holder.admin_lin.setVisibility(View.GONE);
                }


            } else if (allCommunityModel.getReceiveerID().equalsIgnoreCase(session.getUserId())) {
                Log.e("Else", "onBindViewHolder: " + allCommunityModel.getMessage());
                if (allCommunityModel.getMessage().equalsIgnoreCase("Image")) {

                    Log.e("Admin Send", "onBindViewHolder: " + convert(allCommunityModel.getImage()));
                    holder.admin_time.setVisibility(View.GONE);
                    holder.admin_lin.setVisibility(View.VISIBLE);
                    holder.admin_msg.setVisibility(View.GONE);
                    holder.iv_photo_user.setVisibility(View.GONE);
                    holder.image_play.setVisibility(View.GONE);
                    holder.cust_lin.setVisibility(View.GONE);
                    holder.my_time.setVisibility(View.GONE);
                    holder.iv_cameraimageee_admin.setVisibility(View.VISIBLE);
                    holder.iv_cameraimageee_admin.setImageBitmap(convert(allCommunityModel.getImage()));
                    holder.my_time.setText("");


                } else if (allCommunityModel.getMessage().equalsIgnoreCase("Vid")) {
                    holder.adminvideocard.setVisibility(View.VISIBLE);
                    holder.iv_admin_video_thumbnail.setVisibility(View.VISIBLE);
                    holder.admin_msg.setVisibility(View.GONE);
                    holder.iv_photo_user.setVisibility(View.GONE);
                    holder.image_play.setVisibility(View.GONE);
                    holder.cust_lin.setVisibility(View.GONE);
                    holder.my_time.setVisibility(View.GONE);
                    holder.admin_time.setVisibility(View.GONE);

                    holder.adminvideocard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialogView = new Dialog(context);
                            dialogView.setContentView(R.layout.imageviewwer_dailog_view);
                            dialogView.setCancelable(false);
                            dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogView.show();

                            VideoView videoView = dialogView.findViewById(R.id.video_view);
                            MediaController mediaController = new MediaController(context);
                            mediaController.setAnchorView(videoView);
                            videoView.setMediaController(mediaController);

                            Log.e("===", "My Send Video-: " + allCommunityModel.getVideo());



                            videoView.setVideoURI(Uri.parse(allCommunityModel.getVideo()));
                            videoView.requestFocus();
                            videoView.start();
                            ImageView fulimageshow = dialogView.findViewById(R.id.fulimageshow);
                            fulimageshow.setVisibility(View.GONE);
                            videoView.setVisibility(View.VISIBLE);
                            ImageView cancel = dialogView.findViewById(R.id.cancel);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogView.dismiss();
                                    dialogView.cancel();
                                }
                            });
                        }
                    });
                    Log.e("TAG", "onBindViewHolder: " + allCommunityModel.getVideo());


                } else {
                    holder.admin_msg.setText(allCommunityModel.getMessage());
                    holder.usrname.setText(allCommunityModel.getUsername());
                    holder.admin_time.setText(allCommunityModel.getTime());
                    holder.iv_photo_user.setVisibility(View.GONE);
                    holder.image_play.setVisibility(View.GONE);
                    holder.cust_lin.setVisibility(View.GONE);
                    holder.my_time.setVisibility(View.GONE);

                    holder.admin_lin.setVisibility(View.VISIBLE);
                }
            }


        }

    }

    @Override
    public int getItemCount() {
        return allCommunityModels.size();
    }

    public Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String encodeImage(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView customer_msg, admin_msg, admin_time, my_time, usrname;
        ImageView iv_photo_user, iv_cameraimageee_admin, image_play, iv_admin_video_thumbnail;
        LinearLayout payment_lin;
        RelativeLayout rel_user, userlayout_layout;
        CardView cust_lin, admin_lin, usercard, adminvideocard;

        ViewHolder(View parent) {
            super(parent);
            usercard = parent.findViewById(R.id.usercard);
            admin_msg = parent.findViewById(R.id.admin_msg);
            customer_msg = parent.findViewById(R.id.customer_msg);
            admin_lin = parent.findViewById(R.id.admin_lin);
            cust_lin = parent.findViewById(R.id.cust_lin);
            usrname = parent.findViewById(R.id.usrname);
            iv_photo_user = parent.findViewById(R.id.iv_photo_user);
            iv_cameraimageee_admin = parent.findViewById(R.id.iv_cameraimageee_admin);
            admin_time = parent.findViewById(R.id.admin_time);
            my_time = parent.findViewById(R.id.my_time);

            rel_user = parent.findViewById(R.id.rel_user);
            image_play = parent.findViewById(R.id.image_play);
            userlayout_layout = parent.findViewById(R.id.userlayout_layout);
            adminvideocard = parent.findViewById(R.id.adminvideocard);
            iv_admin_video_thumbnail = parent.findViewById(R.id.iv_admin_video_thumbnail);
        }
    }

}









