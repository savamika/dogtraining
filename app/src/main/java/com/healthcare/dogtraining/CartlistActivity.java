package com.healthcare.dogtraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.Adapter.Cartupdatelistner;
import com.healthcare.dogtraining.Adapter.GetCartListAdapter;
import com.healthcare.dogtraining.Model.GetCartModel;
import com.healthcare.dogtraining.ui.Address.AddAddressActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.get_cart_product;
import static com.healthcare.dogtraining.API.Base_Url.place_order;
import static com.healthcare.dogtraining.API.Base_Url.place_order_after;
import static java.lang.Float.parseFloat;

public class CartlistActivity extends AppCompatActivity implements Cartupdatelistner {
    private static final String TAG = "CartlistActivity";
    LinearLayout IV_PlaceOrede,l_details;
    Session1 session1;
    TextView change,adress,tv_totalprice,tv_discountprice,tv_belowTotal_price;
    RecyclerView recyclerview;
    List<GetCartModel>getCartModels=new ArrayList<>();
    GetCartListAdapter adapter;
    ImageView iv_back;
    String total_price;
    CardView card_pricedetails;
    RelativeLayout progress_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        IV_PlaceOrede= findViewById(R.id.IV_PlaceOrede);
        change= findViewById(R.id.change);
        progress_view= findViewById(R.id.progress_view);
        adress= findViewById(R.id.adress);
        recyclerview= findViewById(R.id.recyclerview);
        iv_back=findViewById(R.id.iv_back);
        tv_totalprice=findViewById(R.id.tv_totalprice);
        tv_discountprice=findViewById(R.id.tv_discountprice);
        tv_belowTotal_price=findViewById(R.id.tv_belowTotal_price);
        card_pricedetails=findViewById(R.id.card_pricedetails);
        l_details=findViewById(R.id.l_details);
        session1=new Session1(CartlistActivity.this);
        adress.setText(session1.getAddress());

        GetCartApi();


        IV_PlaceOrede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray passArray = GetAllItemJsonArray();
                if(session1.getAddress().equalsIgnoreCase("")){

                    Toast.makeText(CartlistActivity.this, "Please select address", Toast.LENGTH_SHORT).show();

                }else{

                    PlaceOrderApi();
                }
            }
        });
           change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartlistActivity.this, AddAddressActivity.class);
                startActivity(intent);
                finish();
            }
            });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

// place_order
        private void PlaceOrderApi() {
        String url = BaseUrl +  place_order_after;
                AndroidNetworking.post(url)
                        .addBodyParameter("user_id", session1.getUserId())
                        .addBodyParameter("address_id", session1.getAddressID())

                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {

                                try {

                                    String result = jsonObject.getString("result");
                                    String orderId = jsonObject.getString("orderId");
//                                    String msg = jsonObject.getString("msg");
                                    if (result.equalsIgnoreCase("true")) {
                                        float mTotalPrice;
                                        mTotalPrice = parseFloat(total_price);
                                        float mTestAmount= mTotalPrice;
                                        Log.e(TAG, "orderId Response: "+orderId );
                                        session1.set_role("Purchase_cardItem");
                                        Intent intent = new Intent(CartlistActivity.this, PlaceOrderActivity.class);
                                        intent.putExtra("total",mTestAmount);
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("type","Product");
                                        startActivity(intent);
                                        finish();
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






         private JSONArray GetAllItemJsonArray() {
        {
            JSONArray passArray = new JSONArray();
            if (getCartModels.size() > 0) {
                for (int i = 0; i < getCartModels.size(); i++) {
                    JSONObject jObjP = new JSONObject();
                    try {
                        jObjP.put("cart_id", getCartModels.get(i).getCart_id());
                             passArray.put(jObjP);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }

                }
            return passArray;
        }


    }

              private void GetCartApi() {
                 String url = BaseUrl +get_cart_product ;
                     AndroidNetworking.post(url)
                             .addBodyParameter("user_id", session1.getUserId())
                             .build()
                             .getAsJSONObject(new JSONObjectRequestListener() {
                                 @Override
                                 public void onResponse(JSONObject jsonObject) {
                                     progress_view.setVisibility(View.INVISIBLE);
                                     try {
                                         getCartModels.clear();
                                         String result = jsonObject.getString("result");
                                         String msg = jsonObject.getString("msg");
                                         System.out.println("cartlistactivity"+jsonObject.toString());
                                         if (result.equalsIgnoreCase("true")) {
                                             String discont_price = jsonObject.getString("discont price");
                                             total_price = jsonObject.getString("total price");
                                             JSONArray jsonArray = jsonObject.getJSONArray("data");
                                             for (int i = 0; i < jsonArray.length(); i++) {
                                                 JSONObject dataObject = jsonArray.getJSONObject(i);
                                                 GetCartModel allCommunityModel = new GetCartModel(
                                                         dataObject.getString("cart_id"),
                                                         dataObject.getString("user_id"),
                                                         dataObject.getString("product_id"),
                                                         dataObject.getString("quantity"),
                                                         dataObject.getString("status"),
                                                         dataObject.getString("name"),
                                                         dataObject.getString("price"),
                                                         dataObject.getString("discounted_price"),
                                                         dataObject.getString("image"),
                                                         dataObject.getString("image1"),
                                                         dataObject.getString("image2"),
                                                         dataObject.getString("image3"),
                                                         dataObject.getString("image4"),
                                                         dataObject.getString("color"),
                                                        ""
                                                         );

                                                 l_details.setVisibility(View.VISIBLE);
                                                 getCartModels.add(allCommunityModel);
                                                 tv_totalprice.setText("₹" + total_price);
                                                 tv_discountprice.setText("-₹" + discont_price);
                                                 tv_belowTotal_price.setText("₹" + total_price);

                                                 String cartid= dataObject.getString("cart_id");
                                                 ArrayList<String> list = new ArrayList<String>();
                                                 list.add(cartid);

                                                 List<String> gfg = new ArrayList<String>(list);
                                                 System.out.println("<><====cartid"+gfg);
                                             }

                                             adapter= new GetCartListAdapter(getCartModels, CartlistActivity.this,CartlistActivity.this);
                                             RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(CartlistActivity.this);
                                             recyclerview.setLayoutManager(mLayoutManger);
                                             recyclerview.setLayoutManager(new LinearLayoutManager(CartlistActivity.this, RecyclerView.VERTICAL, false));
                                             recyclerview.setItemAnimator(new DefaultItemAnimator());
                                             recyclerview.setAdapter(adapter);
                                             adapter.notifyDataSetChanged();
                                             progress_view.setVisibility(View.INVISIBLE);
                                         }



                                         else  {

                                             l_details.setVisibility(View.GONE);
                                             card_pricedetails.setVisibility(View.GONE);
                                             tv_totalprice.setVisibility(View.GONE);
                                             tv_discountprice.setVisibility(View.GONE);
                                             tv_belowTotal_price.setVisibility(View.GONE);
                                             progress_view.setVisibility(View.INVISIBLE);
                                         }



                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                         progress_view.setVisibility(View.INVISIBLE);
                                     }
                                     }
                                 @Override
                                 public void onError(ANError anError) {
                                     progress_view.setVisibility(View.INVISIBLE);
                                     Log.e("error_my_join", anError.toString());
                                  }
                                  });
                                  }

       @Override
       public void onGeekEvent(boolean k) {
        GetCartApi();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}