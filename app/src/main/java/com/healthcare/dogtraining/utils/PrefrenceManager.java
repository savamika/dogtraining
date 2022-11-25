package com.healthcare.dogtraining.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceManager {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    public static final String MyPREFERENCES = "MyPrefss";
    private static String TOKEN_ID="com.ilaj.ui.common.utils.TOKEN_ID";





    public PrefrenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = pref.edit();


    }

    public static String getTokenId(Context context) {
        return TOKEN_ID;
    }

    public static void setTokenId(Context context,String tokenId) {
        TOKEN_ID = tokenId;
    }



}
