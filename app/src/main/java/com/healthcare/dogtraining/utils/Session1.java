package com.healthcare.dogtraining.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.healthcare.dogtraining.HomeActivity;

public class Session1 extends Object{
    private static final String TAG = Session1.class.getSimpleName();
    private static final String PREF_NAME = "Rapidine_pref2";
    private static final String IS_LOGGEDIN = "isLoggedIn";
    private static final String FAV = "fav";
    private static final String Mobile = "mobile";
    private static final String Email = "email";
    private static final String UserId = "user_id";
    private static final String User_name = "user_name";
    private static final String Pro_Image = "pro_img";
    private static final String role_ = "user_role";
    private static final String Address = "address";
    private static final String AddressID = "AddressID";
    private static final String OLD_price = "price";
    private static final String Size_id = "size";
    private static final String Color_name = "color";
    private static final String sub_category = "sub_category";
    private static final String shopid = "shopid";
    private static final String cartcount = "cartc";
    private static final String add_btn_status = "cartc";
    private static final String adres_redirect = "adres_redirect";
    private static final String count = "count";
    private static final String link = "link";

    private Context _context;
    private SharedPreferences Rapidine_pref;
    private SharedPreferences.Editor  editor;

    public Session1(Context context) {
        this._context = context;


        Rapidine_pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = Rapidine_pref.edit();
        editor.apply();
    }




    public void setMobile(String mobile, String email) {
        editor.putString(Mobile, mobile);
        editor.putString(Email, email);
        editor.apply();
        editor.commit();
    }

    public String getMobile() {
        return Rapidine_pref.getString(Mobile, "");

    }
    public String getUser_name() {
        return Rapidine_pref.getString(User_name, "");

    }
    public void setUserId(String userId) {
        editor.putString(UserId, userId);
        this.editor.apply();
    }

    public void set_role(String role) {
        editor.putString(role_, role);
        editor.apply();
        editor.commit();
    }

    public String get_role()
    {
        return Rapidine_pref.getString(role_, "");
    }

    public String getUserId() {
        return Rapidine_pref.getString(UserId, "");
    }




    public void setUser_name(String user_name) {
        editor.putString(User_name, user_name);
        this.editor.apply();
    }

    public String getEmail() {
        return Rapidine_pref.getString(Email, "");
    }


    public String getAddress() {
        return Rapidine_pref.getString(Address, "");
    }

    public void setAddress(String address) {
        editor.putString(Address, address);
        this.editor.apply();
    }



    public String getSize_id() {
        return Rapidine_pref.getString(Size_id, "");
    }

    public void setSize_id(String address) {
        editor.putString(Size_id, address);
        this.editor.apply();
    }


    public String getColor_name() {
        return Rapidine_pref.getString(Color_name, "");
    }

    public void setColor_name(String address) {
        editor.putString(Color_name, address);
        this.editor.apply();
    }



    public String getSub_category() {
        return Rapidine_pref.getString(sub_category, "");
    }

    public void setSub_category(String address) {
        editor.putString(sub_category, address);
        this.editor.apply();
    }


    public String getShopid() {
        return Rapidine_pref.getString(shopid, "");
    }

    public void setShopid(String address) {
        editor.putString(shopid, address);
        this.editor.apply();
    }


    public String getLink() {
        return Rapidine_pref.getString(link, "");
    }

    public void setLink(String Link) {
        editor.putString(link, Link);
        this.editor.apply();
    }




    public String getCartcount() {
        return Rapidine_pref.getString(cartcount, "");
    }

    public void setCartcount(String address) {
        editor.putString(cartcount, address);
        this.editor.apply();
    }







    public String getOldprice() {
        return Rapidine_pref.getString(OLD_price, "");
    }

    public void setOldprice(String address) {
        editor.putString(OLD_price, address);
        this.editor.apply();
    }
    public String getcount() {
        return Rapidine_pref.getString(count, "");
    }

    public void setCount(String address) {
        editor.putString(count, address);
        this.editor.apply();
    }









    public void logout() {
        editor.clear();
        editor.apply();
        Intent showLogin = new Intent(_context, HomeActivity.class);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(showLogin);
    }




    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {

        return Rapidine_pref.getBoolean(IS_LOGGEDIN, false);
    }


    public void setAddress(Context context, String address) {
        editor.putString(Address, address);
        this.editor.apply();
    }


    public void setAddressID(Context context, String addressID) {
        editor.putString(AddressID, addressID);
        this.editor.apply();
    }




    public String getAddressID() {
        return Rapidine_pref.getString(AddressID, "");
    }

//add to cart btn status

    public void set_addbtn(String role) {
        editor.putString(add_btn_status, role);
        editor.apply();
        editor.commit();
    }

    public String get_addbtn()
    {
        return Rapidine_pref.getString(add_btn_status, "");
    }

//    adres page manage session

    public void set_addresmange(String role) {
        editor.putString(adres_redirect, role);
        editor.apply();
        editor.commit();
    }

    public String get_addresmange()
    {
        return Rapidine_pref.getString(adres_redirect, "");
    }

}








