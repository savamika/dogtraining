package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.CartlistActivity;
import com.healthcare.dogtraining.Model.GetAddresslistModel;
import com.healthcare.dogtraining.PlaceOrderActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.deleteAddress;

public class GetAddressAdapter extends RecyclerView.Adapter<GetAddressAdapter.ViewHolder> {
    private List<GetAddresslistModel> getAddresslistModels;
    private Context context;
    Session1 session1;
    private RadioButton lastCheckedRB = null;



    public GetAddressAdapter (List<GetAddresslistModel> getAddresslistModels, Context context) {
        this.getAddresslistModels = getAddresslistModels;
        this.context = context;

    }


    @Override
    public GetAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listaddress, parent, false);
        return new GetAddressAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetAddressAdapter.ViewHolder holder, final int position) {
        final GetAddresslistModel allCommunityModel = getAddresslistModels.get(position);
        session1=new Session1(context);

        holder.adressname.setText(allCommunityModel.getAddress());
        holder.postcode.setText(allCommunityModel.getPostcode());
        holder.city.setText(allCommunityModel.getCity_name());


        if (getAddresslistModels.get(position).getId().equalsIgnoreCase(session1.getAddress())){
            holder.radio_select.setChecked(true);
            //mCheckedPostion =position;
            Log.e("radio_pos", ""+position);
            Log.e("radio_pos1", getAddresslistModels.get(position).getId());
            lastCheckedRB = holder.radio_select;
        }


        holder.radio_select.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RadioButton checked_rb = (RadioButton) v;
                if(lastCheckedRB != null){
                    lastCheckedRB.setChecked(false);
                }


                    lastCheckedRB = checked_rb;
                    session1.setAddress(context,getAddresslistModels.get(position).getAddress());
                    session1.setAddressID(context,getAddresslistModels.get(position).getId());
                    Intent intent=new Intent(context, CartlistActivity.class);
                    context.startActivity(intent);



            }
        });


        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressid=getAddresslistModels.get(position).getId();
                DeleteAddressApi(addressid,position);
            }
            });
            }

        private void DeleteAddressApi(String addressid, int position) {
        String url = BaseUrl +deleteAddress ;
                AndroidNetworking.post(url)
                        .addBodyParameter("id",addressid)
                        .addBodyParameter("user_id",session1.getUserId())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    String result = jsonObject.getString("result");
                                    String msg = jsonObject.getString("msg");
                                    System.out.println("<><====deleteaddress"+jsonObject);
                                    if (result.equalsIgnoreCase("true")) {

                                        getAddresslistModels.remove(position);
                                        notifyDataSetChanged();
//

                                    } else {



                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("error_my_join", anError.toString());
                            }
                            });
                        }





        @Override
        public int getItemCount() {
        return getAddresslistModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView adressname,postcode,city;
        RadioButton radio_select;
        ImageView iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adressname=itemView.findViewById(R.id.adressname);
            postcode=itemView.findViewById(R.id.postcode);
            radio_select=itemView.findViewById(R.id.radio_select);
            iv_delete=itemView.findViewById(R.id.iv_delete);
            city=itemView.findViewById(R.id.city);

        }
    }

}


