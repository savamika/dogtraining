package com.healthcare.dogtraining.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.healthcare.dogtraining.Model.GetCommentListModel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.deleteComment;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder>{


    private List<GetCommentListModel> getCommentListModels;

    // private UserInfoData userInfoData;
    private Context context;

    String pastdate,user_id;
    Session1 session1;


    public CommentListAdapter(List<GetCommentListModel> getCommentListModels, Context context) {
        this.getCommentListModels = getCommentListModels;
        this.context = context;

        notifyDataSetChanged();
    }






    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new CommentListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentListAdapter.ViewHolder holder, final int position) {

        final GetCommentListModel commentModel =getCommentListModels.get(position);
        session1=new Session1(context);
        if (getCommentListModels.size() > 0) {
            holder.tv_comment_body.setText(commentModel.getComment());
            holder.tv_name.setText(commentModel.getFullname());

            Glide.with(context)
                    .load(Image_Path + getCommentListModels.get(position).getImage())
                    .into(holder.avatar);

        }
        if (getCommentListModels.get(position).getUser_id().equalsIgnoreCase(session1.getUserId())) {

            holder.comment_edit_layout.setVisibility(View.VISIBLE);

          }else {
            holder.comment_edit_layout.setVisibility(View.GONE);
        }

        holder.deletecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=getCommentListModels.get(position).getId();
              DeletecommentApi(id,position);
            }
        });
        
        holder.edit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // UpdateCommentApi();
            }
        });
        }


    private void DeletecommentApi(String id, int position) {
             AlertDialog.Builder builder = new AlertDialog.Builder(context);
             builder.setMessage("Are you sure want to delete");
             builder.setPositiveButton("OK",
                     new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             
                             deleteComment(id,position);
                         }
                     });
             builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                     Toast.makeText(context,
                             android.R.string.no, Toast.LENGTH_SHORT).show();


                 }
             });
             builder.setCancelable(false);
             builder.show();



         }

            private void deleteComment(String id, int position) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl+deleteComment,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject obj = new JSONObject(response);
                                String result = obj.getString("result");
                                String msg = obj.getString("msg");
                                System.out.println("<><obj"+obj);
                                if (result.equalsIgnoreCase("true")) {

                                    getCommentListModels.remove(position);
                                    notifyDataSetChanged();

                                } else {
                                    Toast.makeText(context, "false", Toast.LENGTH_LONG).show();

                                }

                            } catch (JSONException e) {


                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("id",id );
                    params.put("user_id",session1.getUserId());

                    return params;
                }
            };

            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


        }







    @Override
    public int getItemCount() {
        return getCommentListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_user_name_post,edit_comment,deletecomment,tv_name,tv_comment_body,tv_time_comment;
        CircleImageView avatar;
        LinearLayout l_comment,comment_edit_layout;



        public ViewHolder(View itemView) {
            super(itemView);

            avatar=itemView.findViewById(R.id.avatar);

            // tv_user_name_post = itemView.findViewById(R.id.tv_user_name_post);
            tv_comment_body = itemView.findViewById(R.id.tv_comment_body);
            tv_name = itemView.findViewById(R.id.tv_name);
            //  l_comment = itemView.findViewById(R.id.l_comment);
            deletecomment = itemView.findViewById(R.id.deletecomment);
            comment_edit_layout = itemView.findViewById(R.id.comment_edit_layout);
            edit_comment = itemView.findViewById(R.id.edit_comment);




        }
    }
}

