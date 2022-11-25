package com.healthcare.dogtraining.ui.ADOPTION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Adapter.Adapter_attachments_images;
import com.healthcare.dogtraining.Adapter.ImagesAdapteradoptiondetails;
import com.healthcare.dogtraining.Model.GetImagesModel;
import com.healthcare.dogtraining.Model.SubjectData;
import com.healthcare.dogtraining.MyAdoptionDetailsActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.utils.Utility;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.addAdoption;
import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;
import static com.healthcare.dogtraining.API.Base_Url.getAdoptbyid;
import static com.healthcare.dogtraining.API.Base_Url.updateAdoptions;

public class EditAdoptiondETAILSActivity extends AppCompatActivity {
    private static final String TAG = "AdoptionFormActivity";
    private final ArrayList<Uri> arrayList = new ArrayList<>();
    private final List<String> FilenamList = new ArrayList<>();
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    EditText et_petname, et_petage, et_breedname, et_petweight, et_description, et_address,
            et_mobile;
    Button btn_submit;
    CircleImageView image;
    File destination;
    String userChoosenTask;
    String filenew1, Dob;
    Session1 session1;
    RadioButton male, female;
    String gender = "";
    Uri pickedImage;
    RecyclerView recyclerview;
    File file;
    DatePickerDialog datePickerDialog;
    ArrayList<SubjectData> imagearrayList = new ArrayList<SubjectData>();
    Adapter_attachments_images adapter_attachments_images;
    RelativeLayout progress_view;
String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editadoption_details);
        progress_view = findViewById(R.id.progress_view);
        et_petname = findViewById(R.id.et_petname);
        et_petage = findViewById(R.id.et_petage);
        et_breedname = findViewById(R.id.et_breedname);
        et_petweight = findViewById(R.id.et_petweight);
        et_description = findViewById(R.id.et_description);
        btn_submit = findViewById(R.id.btn_submit);
        image = findViewById(R.id.image);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        recyclerview = findViewById(R.id.recyclerview);
        session1 = new Session1(EditAdoptiondETAILSActivity.this);
        et_mobile = findViewById(R.id.et_mobile);
        et_address = findViewById(R.id.et_address);



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRunTimePermission();
            }
        });

        if (getIntent() != null) {
            ID  = getIntent().getStringExtra("ID");
            Log.e(TAG, "onCreate:ID ><><><><<><    "+ID );
        }
        GetAdoptByIdApi(ID);
        et_petage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final java.util.Calendar c = java.util.Calendar.getInstance();
                int mYear = c.get(java.util.Calendar.YEAR); // current year
                int mMonth = c.get(java.util.Calendar.MONTH); // current month
                int mDay = c.get(java.util.Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(EditAdoptiondETAILSActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                et_petage.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                Dob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }

                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });


        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    gender = "Male";

                }
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    gender = "Female";

                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (!et_petname.getText().toString().isEmpty()
                        && !et_petage.getText().toString().isEmpty()
                        && !et_breedname.getText().toString().isEmpty()
                        && !et_petweight.getText().toString().isEmpty()
                        && !et_description.getText().toString().isEmpty()
                        &&  gender != ""
                ) {

                    if (filenew1 != null) {
//                        AdoptionformApi(Dob);


                        addAdoptionwith_image();
                    } else {

                        addAdoption();
                    }

                } else {

                    Toast.makeText(EditAdoptiondETAILSActivity.this, "Please filled all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private JSONArray GetAllItemJsonArray() {
        JSONArray passArray = new JSONArray();
        if (FilenamList.size() > 0) {
            for (int i = 0; i < FilenamList.size(); i++) {
                JSONObject jObjP = new JSONObject();

                try {

                    jObjP.put("Image", FilenamList.get(i));

                    passArray.put(jObjP);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


        return passArray;
    }


    public void AdoptionformApi(String dob) {

        progress_view.setVisibility(View.VISIBLE);
        ApiService apiInterface = APIClient.getClient().create(ApiService.class);

        MultipartBody.Part[] surveyImagesParts = null;

        surveyImagesParts = new MultipartBody.Part[FilenamList.size()];
        for (int index = 0; index < FilenamList.size(); index++) {
            File file = new File(FilenamList.get(index));
            System.out.println("file**********     " + imagearrayList.get(index).toString());
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
            surveyImagesParts[index] = MultipartBody.Part.createFormData("images[]", file.getName(), surveyBody);


        }

        RequestBody USER_ID = RequestBody.create(MediaType.parse("text/plain"), session1.getUserId());
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_petname.getText().toString());
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"), et_petage.getText().toString());
        RequestBody Gender = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody breed = RequestBody.create(MediaType.parse("text/plain"), et_breedname.getText().toString());
        RequestBody weight = RequestBody.create(MediaType.parse("text/plain"), et_petweight.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), et_description.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), et_address.getText().toString());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), et_mobile.getText().toString());
        Call<ResponseMessage> call = apiInterface.AdoptionForm(USER_ID, name, age, Gender, breed, weight, description, address, mobile, surveyImagesParts);
        Log.e(TAG, ":AdoptionformApi USER_ID ---  " + session1.getUserId());

        Log.e(TAG, "AdoptionformApi: name ---  " + et_petname.getText().toString());
        Log.e(TAG, "AdoptionformApi: age ---  " + et_petage.getText().toString());
        Log.e(TAG, "AdoptionformApi: Gender ---  " + gender);

        Log.e(TAG, "AdoptionformApi: breed ---  " + et_breedname.getText().toString());
        Log.e(TAG, "AdoptionformApi: weight ---  " + et_petweight.getText().toString());
        Log.e(TAG, "AdoptionformApi: description ---  " + et_description.getText().toString());
        Log.e(TAG, "AdoptionformApi: address ---  " + et_address.getText().toString());
        Log.e(TAG, "AdoptionformApi: mobile ---  " + et_mobile.getText().toString());
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                progress_view.setVisibility(View.INVISIBLE);

                System.out.println("RESULT----       " + response.body().getResult());
                if (response.body().getResult().equals("true")) {

                    finish();

                } else {

                    Toast.makeText(EditAdoptiondETAILSActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                progress_view.setVisibility(View.INVISIBLE);
                Log.e("ccheckResponce", " :" + t.getMessage());
                Toast.makeText(EditAdoptiondETAILSActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            selectImage();
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditAdoptiondETAILSActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditAdoptiondETAILSActivity.this);


                if (items[item].equals("Choose from Library")) {
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
        }
    }


    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = EditAdoptiondETAILSActivity.this.getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            destination = new File(cursor.getString(cursor.getColumnIndex(filePath[0])));
            cursor.close();


            if (destination != null) {
                filenew1 = destination.getAbsolutePath();
                System.out.println("destinationonSelectFromGalleryResult=====        " + destination.getAbsolutePath());
                FilenamList.add(filenew1);
                System.out.println("FilenamList " + FilenamList.toString());


            } else {
                Toast.makeText(EditAdoptiondETAILSActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
            }

            try {
                bm = MediaStore.Images.Media.getBitmap(EditAdoptiondETAILSActivity.this.getContentResolver(), data.getData());

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        try {


            arrayList.add(pickedImage);

            LinkedHashSet<Uri> lhs = new LinkedHashSet<Uri>();

            lhs.addAll(arrayList);
            arrayList.clear();

            arrayList.addAll(lhs);

            imagearrayList.clear();


            for (int i = 0; i < arrayList.size(); i++) {

                String get = (arrayList.get(i)).toString();

                SubjectData allCommunityModel1 = new SubjectData(get);

                System.out.println("Arraylist#####   " + get);
                imagearrayList.add(allCommunityModel1);
                System.out.println("<><====" + imagearrayList.size());

                adapter_attachments_images = new Adapter_attachments_images(imagearrayList, getApplicationContext());
                RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getApplicationContext());
                recyclerview.setLayoutManager(mLayoutManger);
                recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(adapter_attachments_images);
                adapter_attachments_images.notifyDataSetChanged();


            }


        } catch (Exception e) {
        }

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
                            Toast.makeText(EditAdoptiondETAILSActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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

    private void addAdoption() {
        final ProgressDialog progressDialog = new ProgressDialog(EditAdoptiondETAILSActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        AndroidNetworking.upload(BaseUrl+updateAdoptions)
                .addMultipartParameter("id",ID )
                .addMultipartParameter("user_id",session1.getUserId() )
                .addMultipartParameter("user_id",session1.getUserId() )
                .addMultipartParameter("name",et_petname.getText().toString() )
                .addMultipartParameter("age", et_petage.getText().toString() )
                .addMultipartParameter("gender",gender )
                .addMultipartParameter("breed", et_breedname.getText().toString() )
                .addMultipartParameter("weight",et_petweight.getText().toString() )
                .addMultipartParameter("description",et_description.getText().toString() )
                .addMultipartParameter("address", et_address.getText().toString() )
                .addMultipartParameter("mobile",  et_mobile.getText().toString() )



                .setTag("addAdoption")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        try {
                            progressDialog.dismiss();
                            Log.e("addAdoption",jsonObject.toString());
                            System.out.println("addAdoption=====    "+jsonObject.toString());
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");

                            if (result.equalsIgnoreCase("true")) {

                                finish();

                                Toast.makeText( EditAdoptiondETAILSActivity.this,msg, Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText( EditAdoptiondETAILSActivity.this,msg, Toast.LENGTH_LONG).show();


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
    private void addAdoptionwith_image() {
        final ProgressDialog progressDialog = new ProgressDialog(EditAdoptiondETAILSActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        AndroidNetworking.upload(BaseUrl+updateAdoptions)
                .addMultipartParameter("id",ID )
                .addMultipartParameter("user_id",session1.getUserId() )
                .addMultipartParameter("user_id",session1.getUserId() )
                .addMultipartParameter("name",et_petname.getText().toString() )
                .addMultipartParameter("age", et_petage.getText().toString() )
                .addMultipartParameter("gender",gender )
                .addMultipartParameter("breed", et_breedname.getText().toString() )
                .addMultipartParameter("weight",et_petweight.getText().toString() )
                .addMultipartParameter("description",et_description.getText().toString() )
                .addMultipartParameter("address", et_address.getText().toString() )
                .addMultipartParameter("mobile",  et_mobile.getText().toString() )



                .setTag("addAdoption")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        try {
                            progressDialog.dismiss();
                            Log.e("addAdoption",jsonObject.toString());
                            System.out.println("addAdoption=====    "+jsonObject.toString());
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");

                            if (result.equalsIgnoreCase("true")) {

                                finish();

                                Toast.makeText( EditAdoptiondETAILSActivity.this,msg, Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText( EditAdoptiondETAILSActivity.this,msg, Toast.LENGTH_LONG).show();


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








    private void GetAdoptByIdApi(String ID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + getAdoptbyid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progress_view.setVisibility(View.INVISIBLE);
                            JSONObject obj = new JSONObject(response);

                            Log.e(TAG, "onResponse:getAdoptbyid "+response );
                            String result = obj.getString("result");


                            if (result.equals("true")) {
                                JSONObject userJson = obj.getJSONObject("data");
                                String id = userJson.getString("id");
                                String user_id = userJson.getString("user_id");
                                String name = userJson.getString("name");
                                String age = userJson.getString("age");
                                String gender = userJson.getString("gender");
                                String breed = userJson.getString("breed");
                                String weight = userJson.getString("weight");
                                String description = userJson.getString("description");
                                String status = userJson.getString("status");
                                String created_at = userJson.getString("created_at");
                                String update_at = userJson.getString("update_at");
                                String adoption_address = userJson.getString("adoption_address");
                                String adoption_mobile = userJson.getString("adoption_mobile");
//                                mobile = userJson.getString("mobile");



                                et_petage.setText(age);
                                et_petweight.setText(weight);
                                et_petname.setText(name);
                                et_description.setText(description);
                                et_breedname.setText(breed);
//                                tv_gender.setText(gender);
                                et_address.setText(adoption_address);
                                et_mobile.setText(adoption_mobile);
                                progress_view.setVisibility(View.INVISIBLE);
                                String images = userJson.getString("imagess");

                                Glide.with(EditAdoptiondETAILSActivity.this).load(categoryimagepasth +images).into( image);

                                JSONArray array = userJson.getJSONArray("image");



                                for (int i = 0; i < array.length(); i++) {






                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress_view.setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_view.setVisibility(View.INVISIBLE);
                        Toast.makeText(EditAdoptiondETAILSActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", ID);
                Log.e(TAG, "getParams: "+params );
                return params;
            }
        };

        VolleySingleton.getInstance(EditAdoptiondETAILSActivity.this).addToRequestQueue(stringRequest);
    }


}