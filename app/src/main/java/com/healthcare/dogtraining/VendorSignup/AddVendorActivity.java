package com.healthcare.dogtraining.VendorSignup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.HomeActivity;
import com.healthcare.dogtraining.LoginActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.add_vendor;

public class AddVendorActivity extends AppCompatActivity {
    EditText et_Name, et_email, et_MobileNumber, et_address, et_pswrd, et_shopname, et_city, et_pincode;
    Button SIGNUPTYPE;
    Session1 session1;
    ImageView shopimage, iv_idproof, iv_shopimage;
    LinearLayout l_idproof, l_shop;
    File circleimageprofile=null, idproofFile=null;

    public static File bitmapToFile(Activity mContext, Bitmap bitmap) {
        try {
            String name = System.currentTimeMillis() + ".png";
            File file = new File(mContext.getCacheDir(), name);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bArr);
            fos.flush();
            fos.close();

            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);
        et_Name = findViewById(R.id.et_Name);
        et_email = findViewById(R.id.et_email);
        et_MobileNumber = findViewById(R.id.et_MobileNumber);
        et_address = findViewById(R.id.et_address);
        et_pswrd = findViewById(R.id.et_pswrd);
        SIGNUPTYPE = findViewById(R.id.SIGNUPTYPE);
        iv_shopimage = findViewById(R.id.iv_shopimage);
        l_idproof = findViewById(R.id.l_idproof);
        iv_idproof = findViewById(R.id.iv_idproof);
        et_city = findViewById(R.id.et_city);
        et_pincode = findViewById(R.id.et_pincode);
        l_shop = findViewById(R.id.l_shop);
        et_shopname = findViewById(R.id.et_shopname);

        checkRunTimePermission();


        l_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profileimagepick();
            }
        });

        l_idproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profileimagepick1();
            }
        });


        SIGNUPTYPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_pswrd.getText().toString();
                String name = et_Name.getText().toString();
                String phone = et_MobileNumber.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    et_Name.setError("Please enter your name");
                    et_Name.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    et_email.setError("Please enter your email");
                    et_email.requestFocus();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_email.setError("Enter a valid email");
                    et_email.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(phone)) {
                    et_MobileNumber.setError("Enter a mobile");
                    et_MobileNumber.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    et_pswrd.setError("Enter a password");
                    et_pswrd.requestFocus();
                    return;
                } else if (et_MobileNumber.getText().toString().length() != 10) {

                    Toast.makeText(AddVendorActivity.this, "Please enter 10 digit mobile no." + et_MobileNumber.getText().toString().length(), Toast.LENGTH_SHORT).show();


                } else if (et_city.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddVendorActivity.this, "City Required !", Toast.LENGTH_SHORT).show();
                } else if (et_pincode.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddVendorActivity.this, "PinCode Required !", Toast.LENGTH_SHORT).show();
                } else if (circleimageprofile == null) {
                    Toast.makeText(AddVendorActivity.this, "Please select shop image first!", Toast.LENGTH_SHORT).show();
                } else if (idproofFile == null) {
                    Toast.makeText(AddVendorActivity.this, "Please select ID proof image first!", Toast.LENGTH_SHORT).show();
                } else {
                    AddvendorcallApi();

                }
            }
        });
    }

    private void Profileimagepick1() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {
                if (r.getError() == null) {
                    iv_idproof.setImageBitmap(r.getBitmap());
                    iv_idproof.setScaleType(ImageView.ScaleType.FIT_XY);
                    idproofFile = bitmapToFile1(AddVendorActivity.this, r.getBitmap());
                    String filename = idproofFile.getName();
                    System.out.println("<>,mdfjfjfdoffl" + idproofFile);

                } else {

                    Toast.makeText(AddVendorActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).show((AddVendorActivity.this).getSupportFragmentManager());
    }

    private File bitmapToFile1(AddVendorActivity addVendorActivity, Bitmap bitmap) {
        try {
            String name = System.currentTimeMillis() + ".png";
            File file = new File(AddVendorActivity.this.getCacheDir(), name);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bArr);
            fos.flush();
            fos.close();

            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(permissionArrays, 11111);

        } else {


        }


    }

    private void Profileimagepick() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {
                if (r.getError() == null) {
                    iv_shopimage.setImageBitmap(r.getBitmap());
                    iv_shopimage.setScaleType(ImageView.ScaleType.FIT_XY);

                    circleimageprofile = bitmapToFile(AddVendorActivity.this, r.getBitmap());

                    String filename = circleimageprofile.getName();


                } else {

                    Toast.makeText(AddVendorActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).show((AddVendorActivity.this).getSupportFragmentManager());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {
            boolean isPermitted = false;
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {

                    } else {
                        if (openDialogOnce) {
                            Toast.makeText(AddVendorActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            try {

            } catch (Exception e) {

            }

            if (isPermitted) {

                //Profileimagepick();
                // Profileimagepick1();

            } else {

            }
        }
    }


    private void AddvendorcallApi() {
        Log.e("idproofFile", "AddvendorcallApi: " + idproofFile);
        Log.e("circleimageprofile", "AddvendorcallApi: " + circleimageprofile);
        final ProgressDialog progressDialog = new ProgressDialog(AddVendorActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        AndroidNetworking.upload(BaseUrl + add_vendor)
                .addMultipartParameter("name", et_Name.getText().toString())
                .addMultipartParameter("email", et_email.getText().toString())
                .addMultipartParameter("password", et_pswrd.getText().toString())
                .addMultipartParameter("mobile", et_MobileNumber.getText().toString())
                .addMultipartParameter("address", et_address.getText().toString())
                .addMultipartParameter("fcm_id", "")
                .addMultipartFile( "idproof", idproofFile)
                .addMultipartParameter("shopname", et_shopname.getText().toString())
                .addMultipartParameter("city", et_city.getText().toString())
                .addMultipartParameter("pin_code", et_pincode.getText().toString())
                .addMultipartFile("shopimage", circleimageprofile)

                .setTag("edit profile list")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        try {
                            progressDialog.dismiss();
                            System.out.println("vendorsignup=====    " + jsonObject.toString());
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                JSONObject obj1 = jsonObject.getJSONObject("data");
                                String id = obj1.getString("id");
                                Intent intent  = new Intent(getApplicationContext(),  AddBankDetails.class);
                                intent.putExtra("id", id );
                                startActivity(intent);




                                Toast.makeText(AddVendorActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                            } else {
if (result.equalsIgnoreCase("false")){
    Toast.makeText(AddVendorActivity.this, msg, Toast.LENGTH_LONG).show();

}
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        Log.e("error = ", "" + error);
                    }
                });
    }

}