package com.healthcare.dogtraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Session1 extends Object{

    private static final String TAG = com.healthcare.dogtraining.Session1.class.getSimpleName();
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
    private static final String Dog_breed = "dogbreed";
    private static final String Dog_Age = "dogage";
    private static final String Userimage= "userimage";

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
    public void setDog_breed( String dog_breed) {
        editor.putString(Dog_breed, dog_breed);
        editor.apply();
        editor.commit();
    }

    public String getDog_breed() {
        return Rapidine_pref.getString(Dog_breed, "");

    }


    public void setUserimage( String userimage) {
        editor.putString(Userimage, userimage);

        editor.apply();
        editor.commit();
    }

    public String getUserimage() {
        return Rapidine_pref.getString(Userimage, "");

    }





    public void setDog_Age( String dog_Age) {
        editor.putString(Dog_Age, dog_Age);

        editor.apply();
        editor.commit();
    }

    public String getDog_Age() {
        return Rapidine_pref.getString(Dog_Age, "");

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


    public void logout() {
        editor.clear();
        editor.apply();
        Intent showLogin = new Intent(_context, WelcomeActivity.class);
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


}



