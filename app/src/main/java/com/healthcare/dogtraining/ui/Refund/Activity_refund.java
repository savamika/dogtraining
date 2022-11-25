package com.healthcare.dogtraining.ui.Refund;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.utils.Session1;
import com.healthcare.dogtraining.utils.Utility;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.refund_request;

public class Activity_refund extends AppCompatActivity {
    private static final String TAG = "Activity_refund";
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    String userChoosenTask;
    File destination=null;
    String filenew1;
    Session1 session1;
    ImageView ivimage;
    String id, prid,description;
    Button SIGNUPTYPE;
EditText et_reason;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
session1=new Session1(getApplicationContext());
        ivimage = findViewById(R.id.ivimage);
        SIGNUPTYPE = findViewById(R.id.SIGNUPTYPE);
        et_reason = findViewById(R.id.et_reason);
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            prid = getIntent().getStringExtra("prid");

        }
        ivimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRunTimePermission();

            }
        });


        SIGNUPTYPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description=et_reason.getText().toString();
                if (description.equalsIgnoreCase("")){
                    et_reason.setError("Reason");
                    Toast.makeText(Activity_refund.this, "Please provide any reason", Toast.LENGTH_SHORT).show();

                }else if (destination==null){
                    Log.e(TAG, "refund_request----     " );
                    refund_request();
                }
                else {
                    Log.e(TAG, "ImagerefundApi----      " );
                    ImagerefundApi();
                }

            }
        });
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
            selectImage();
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_refund.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Activity_refund.this);


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        Log.e("", String.valueOf(destination));

        System.out.println("onCaptureImageResult=====        " + destination);

        FileOutputStream fo;
        try {
            //destination.createNewFile();
            fo = new FileOutputStream(destination);
            if (destination != null) {
                filenew1 = destination.getAbsolutePath();
                // Toast.makeText(getActivity(), "path is"+destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Activity_refund.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivimage.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = Activity_refund.this.getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            destination = new File(cursor.getString(cursor.getColumnIndex(filePath[0])));
            cursor.close();
            if (destination != null) {
                filenew1 = destination.getAbsolutePath();
                System.out.println("onSelectFromGalleryResult=====        " + destination.getAbsolutePath());


            } else {
                Toast.makeText(Activity_refund.this, "something wrong", Toast.LENGTH_SHORT).show();
            }

            try {
                bm = MediaStore.Images.Media.getBitmap(Activity_refund.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("onSelectFromGalleryResult=====        " + bm.toString());

        ivimage.setImageBitmap(bm);
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
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            Toast.makeText(Activity_refund.this, "Permission required", Toast.LENGTH_SHORT).show();
                            // alertView();
                        }
                    }
                }
            }

            try {
                //selectImage();
            } catch (Exception e) {

            }

            if (isPermitted) {
                selectImage();

            } else {

            }

        }
    }


    private void refund_request() {
        final ProgressDialog progressDialog = new ProgressDialog(Activity_refund.this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + refund_request
                ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            progressDialog.dismiss();
                            //converting response to json object

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")) {


                            } else {


                            }
                        } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Log.e("Api", "error: " + error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session1.getUserId());
                params.put("id", id);
                params.put("product_id", prid);
                params.put("description", description);

                System.out.println(TAG + "  param-      " + params);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
//image upload 


    private void ImagerefundApi() {
        final ProgressDialog progressDialog = new ProgressDialog(Activity_refund.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        Log.e(TAG, "onResponse:id "+id );
        Log.e(TAG, "onResponse:prid "+prid );
        Log.e(TAG, "onResponse:description "+description );
        Log.e(TAG, "onResponse:destination "+destination );
        Log.e(TAG, "onResponse:session1 "+session1.getUserId() );
        AndroidNetworking.upload(BaseUrl + refund_request)

                .addMultipartParameter("user_id", session1.getUserId())

                .addMultipartParameter("id", id)
                .addMultipartParameter("product_id", prid)
                .addMultipartParameter("description", description)
                .addMultipartFile( "image", destination)

                 .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        try {
                            progressDialog.dismiss();
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");


                            if (result.equalsIgnoreCase("true")) {

                                finish();

                                Toast.makeText(Activity_refund.this, msg, Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText(Activity_refund.this, msg, Toast.LENGTH_LONG).show();


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
