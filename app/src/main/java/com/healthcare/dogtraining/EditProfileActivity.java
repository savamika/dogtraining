package com.healthcare.dogtraining;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.utils.Session1;
import com.healthcare.dogtraining.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Image_Path;
import static com.healthcare.dogtraining.API.Base_Url.Update_profile;
import static com.healthcare.dogtraining.API.Base_Url.getMyProfile;

public class EditProfileActivity extends AppCompatActivity {
    EditText et_name, et_phone,et_mail,et_about,et_adress;
    CircleImageView profileimage;
    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    File destination;
    String filenew1;
    Session1 session1;
    String userid;
    LinearLayout update_profile;
    String name,phn,image,email;

    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        et_name=findViewById(R.id.et_name);
        et_phone=findViewById(R.id.et_phone);
        et_mail=findViewById(R.id.et_mail);
        profileimage=findViewById(R.id.image);
        update_profile=findViewById(R.id.update_profile);
        session1=new Session1(EditProfileActivity.this);
        getProfile();
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRunTimePermission();

            }
        });

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfileApi();
            }
        });

    }

               private void getProfile() {
               System.out.println("<><===userid"+session1.getUserId());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl +getMyProfile,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject= new JSONObject(response);

                                String msg=jsonObject.getString("msg");
                                System.out.println("<><===getprofile"+jsonObject);
                                if (jsonObject.optString("result").equals("true")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    String id=jsonObject1.getString("id");
                                    String fullname=jsonObject1.getString("fullname");
                                    String email=jsonObject1.getString("email");
                                    String mobile=jsonObject1.getString("mobile");
                                    String pet_name=jsonObject1.getString("pet_name");
                                    String pet_age=jsonObject1.getString("pet_age");
                                    String pet_bareed=jsonObject1.getString("pet_bareed");
                                    String image=jsonObject1.getString("image");
                                    try {

                                        et_name.setText(fullname);
                                        et_phone.setText(mobile);
                                        et_mail.setText(email);
                                        Glide.with(EditProfileActivity.this)
                                                .load(Image_Path + image)
                                                //.placeholder(R.drawable.dogprofile)
                                                .apply(new RequestOptions()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .dontAnimate()
                                                        .centerCrop()
                                                        .dontTransform())

                                                .skipMemoryCache(true)
                                                .into(profileimage);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {

                                    Toast.makeText(EditProfileActivity.this, msg, Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EditProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", session1.getUserId());
                    System.out.println("id=======       "+session1.getUserId());
                    return params;
                }
            };
            rQueue = Volley.newRequestQueue(EditProfileActivity.this);
            rQueue.add(stringRequest);


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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission( EditProfileActivity.this);


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

        System.out.println("onCaptureImageResult=====        "+ String.valueOf(destination));

        FileOutputStream fo;
        try {
            //destination.createNewFile();
            fo = new FileOutputStream(destination);
            if (destination != null) {
                filenew1 = destination.getAbsolutePath();
                // Toast.makeText(getActivity(), "path is"+destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText( EditProfileActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileimage.setImageBitmap(thumbnail);
    }


    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor =  EditProfileActivity.this.getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            destination = new File(cursor.getString(cursor.getColumnIndex(filePath[0])));
            cursor.close();
            if (destination != null) {
                filenew1 = destination.getAbsolutePath();
                System.out.println("onSelectFromGalleryResult=====        "+ destination.getAbsolutePath());


            } else {
                Toast.makeText( EditProfileActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
            }

            try {
                bm = MediaStore.Images.Media.getBitmap( EditProfileActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("onSelectFromGalleryResult=====        "+bm.toString());

        profileimage.setImageBitmap(bm);
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
                            Toast.makeText( EditProfileActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
                            // alertView();
                        }
                    }
                }
            }

            try {
                //selectImage();
            }catch (Exception e){

            }

            if (isPermitted){
                selectImage();

            }else {

            }

            }
    }



        private void UpdateProfileApi() {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
               progressDialog.setMessage("Processing...");
               progressDialog.show();

              // System.out.println("Path=======        "+destination.getAbsolutePath());

               AndroidNetworking.upload(BaseUrl+Update_profile)
                       .addMultipartParameter("user_id",session1.getUserId() )
                       .addMultipartParameter("name",et_name.getText().toString())
                       .addMultipartParameter("email",et_mail.getText().toString())
                       .addMultipartParameter("mobile",et_phone.getText().toString())
                       .addMultipartParameter("any_pet","")
                       .addMultipartParameter("pet_name","")
                       .addMultipartParameter("pet_age","")
                       .addMultipartParameter("pet_bareed","")
                       .addMultipartFile("image",destination)

                       .setTag("edit profile list")
                       .setPriority(Priority.LOW)
                       .build()
                       .getAsJSONObject(new JSONObjectRequestListener() {
                           @SuppressLint("WrongConstant")
                           @Override
                           public void onResponse(JSONObject jsonObject) {

                               try {
                                   progressDialog.dismiss();
                                   Log.e("update profile",jsonObject.toString());
                                   System.out.println("update profile=====    "+jsonObject.toString());
                                   String result = jsonObject.getString("result");
                                   String msg = jsonObject.getString("msg");

                                   if (result.equalsIgnoreCase("true")) {

                                       finish();

                                       Toast.makeText( EditProfileActivity.this,msg, Toast.LENGTH_LONG).show();


                                   } else {

                                       Toast.makeText( EditProfileActivity.this,msg, Toast.LENGTH_LONG).show();


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
