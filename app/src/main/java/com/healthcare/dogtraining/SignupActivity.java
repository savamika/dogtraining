package com.healthcare.dogtraining;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.healthcare.dogtraining.API.Base_Url;
import com.healthcare.dogtraining.Model.CityListModel;
import com.healthcare.dogtraining.Model.StateListModel;
import com.healthcare.dogtraining.gps.GPSTracker;
import com.healthcare.dogtraining.utils.PrefrenceManager;
import com.healthcare.dogtraining.utils.Utility;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Get_City;
import static com.healthcare.dogtraining.API.Base_Url.Get_State;
import static com.healthcare.dogtraining.API.Base_Url.Signup_Api;
import static com.healthcare.dogtraining.API.Base_Url.Update_profile;

public class SignupActivity extends AppCompatActivity {
    int MAP_BUTTON_REQUEST_CODE = 100;
    private Double lat;
    private Double lng;
    private GPSTracker tracker;
    Button SIGNUPTYPE;
    Spinner state,city,zipcode;
    EditText et_Name,et_email,et_MobileNumber,et_password,et_zipcode;
    ArrayList<StateListModel> stateListModels = new ArrayList<>();
    ArrayList<String> statelist = new ArrayList<>();
    String state_name,spin_state_id,state_id,city_name_,Spin_city_id,city_id;
    private Object Base_Url;
    ArrayList<CityListModel> cityListModels = new ArrayList<>();
    ArrayList<String> citylist = new ArrayList<>();
    RadioButton radio_yes,radio_no;
    String radio="";
    Session1 session1;
    PrefrenceManager prefrenceManager;
    String FCM_ID;
    CircleImageView userimage;
    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    File profileimagefile,destination;
    String filenew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        SIGNUPTYPE= findViewById(R.id.SIGNUPTYPE);
        et_Name= findViewById(R.id.et_Name);
        et_email= findViewById(R.id.et_email);
        et_MobileNumber= findViewById(R.id.et_MobileNumber);
        et_password= findViewById(R.id.et_password);
        et_zipcode= findViewById(R.id.et_zipcode);
        state= findViewById(R.id.state);
        city= findViewById(R.id.city);
        radio_yes= findViewById(R.id.radio_yes);
        radio_no= findViewById(R.id.radio_no);
        userimage= findViewById(R.id.userimage);
        tracker=new GPSTracker(SignupActivity.this);
        FCM_ID=  prefrenceManager.getTokenId(SignupActivity.this);

        LocationManager locationManager = (LocationManager)SignupActivity.this.getSystemService(LOCATION_SERVICE);
        session1=new Session1(SignupActivity.this);
        StateApi();

        if (tracker.canGetLocation() == true) {
            lat = tracker.getLatitude();
            lng = tracker.getLongitude();
            Log.e("current_lat ", " " + String.valueOf(lat));
            Log.e("current_Lon ", " " + String.valueOf(lng));
            getAddress(lat,lng);
           }else if (tracker.canGetLocation() == false) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Toast.makeText(SignupActivity.this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
            }else{
                showGPSDisabledAlertToUser();
            }
            }



            state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state_id = stateListModels.get(i).getSpin_state_id();
                CityApi(state_id);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city_id = cityListModels.get(i).getSpin_city_id();
                System.out.println("<><cityidpriyaa"+city_id);


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        radio_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio="NO";
            }
        });
        radio_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio="Yes";
            }
        });

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRunTimePermission();
            }
        });

           SIGNUPTYPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = et_Name.getText().toString().trim();
                final String phone = et_MobileNumber.getText().toString().trim();
                final String email = et_email.getText().toString().trim();
                final String password = et_password.getText().toString().trim();
                final String zipcode = et_zipcode.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    et_Name.setError("Please enter username");
                    et_Name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    et_email.setError("Please enter your email");
                    et_email.requestFocus();
                    return;

                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_email.setError("Enter a valid email");
                    et_email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    et_password.setError("Enter a password");
                    et_password.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(zipcode)) {
                    et_zipcode.setError("Enter a Postcode");
                    et_zipcode.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    et_MobileNumber.setError("Enter a mobile");
                    et_MobileNumber.requestFocus();
                    return;
                }
                if (state_id == null) {
                    Toast.makeText(SignupActivity.this, "select state name", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (city_id == null) {
                    Toast.makeText(SignupActivity.this, "select city name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_MobileNumber.getText().toString().length() <= 10) {
                    if (radio_yes.isChecked() || radio_no.isChecked()) {

                        if(radio.equals("NO")){

                          if(destination!=null){

                              CallSignUpApi(username, email, password, zipcode, phone, state_id, city_id);

                              }

                          else{

                              Toast.makeText(SignupActivity.this, "Please select image", Toast.LENGTH_SHORT).show();

                          }
                        } else{

                            if(destination!=null){
                                Intent intent = new Intent(SignupActivity.this, SignupdetailsActivity.class);
                                intent.putExtra("INTENT", "Signup");
                                intent.putExtra("Et_name", username);
                                intent.putExtra("Et_email", email);
                                intent.putExtra("Et_mobile", phone);
                                intent.putExtra("Et_password", password);
                                intent.putExtra("AnyPet", radio);
                                intent.putExtra("Stateid", state_id);
                                intent.putExtra("Cityid", city_id);
                                intent.putExtra("Zipcode", zipcode);
                                intent.putExtra("imagefile", filenew);
                                startActivity(intent);
                                finish();

                                }
                            else{

                                Toast.makeText(SignupActivity.this, "Please select image", Toast.LENGTH_SHORT).show();

                                }
                                 }
                       }else{

                        Toast.makeText(SignupActivity.this, "Please select pet options", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(SignupActivity.this, "Please enter 10 digit mobile no."+et_MobileNumber.getText().toString().length(), Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
                selectImage();
            }

        }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SignupActivity.this);


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
           Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                try {
                    onSelectFromGalleryResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    public static File bitmapToFile(Context mContext, Bitmap bitmap) {
        try {
            String name = System.currentTimeMillis() + ".png";
            File file = new File(mContext.getCacheDir(), name);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, bos);
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
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        Log.e("TAG", "onCaptureImageResult: "+thumbnail );
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        destination =bitmapToFile(SignupActivity.this,thumbnail);
        Log.e("Profile", "onCaptureImageResult: "+destination );

        userimage.setImageBitmap(thumbnail);
    }




    private void onSelectFromGalleryResult(Intent data) throws IOException {

        Bitmap thumbnail =MediaStore.Images.Media.getBitmap(SignupActivity.this.getContentResolver(), data.getData());
        Log.e("TAG", "onCaptureImageResult: "+thumbnail );
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        destination =bitmapToFile(SignupActivity.this,thumbnail);
        Log.e("Profile", "onCaptureImageResult: "+destination );

        userimage.setImageBitmap(thumbnail);
       /* Bitmap bm = null;
        if (data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = SignupActivity.this.getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            destination = new File(cursor.getString(cursor.getColumnIndex(filePath[0])));
            cursor.close();
            if (destination != null) {
                filenew = destination.getAbsolutePath();
                Log.e("galleryimag",filenew.toString());
            } else {
                Toast.makeText(SignupActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
            try {
                bm = MediaStore.Images.Media.getBitmap(SignupActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userimage.setImageBitmap(bm);*/
    }


           private void CallSignUpApi(String username, String email, String password, String zipcode, String phone, String state_id, String city_id) {
          /* final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + Signup_Api ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("signup data", response.toString());
                            try {
                                progressDialog.dismiss();
                                JSONObject obj = new JSONObject(response);
                                String result = obj.getString("result");
                                String msg = obj.getString("msg");

                                System.out.println("<><==callsignup"+obj);

                                if (result.equalsIgnoreCase("true")) {
                                    JSONObject obj1 = obj.getJSONObject("data");
                                    String id=obj1.getString("id");
                                    String fullname=obj1.getString("fullname");
                                    String email=obj1.getString("email");
                                    String mobile=obj1.getString("mobile");

                                    session1.setLogin(true);
                                    session1.setUserId(id);
                                    session1.setMobile(mobile,email);
                                    session1.setUser_name(fullname);

                                    Toast.makeText(SignupActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

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
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", username);
                    params.put("email", email);
                    params.put("mobile", phone);
                    params.put("password", password);
                    params.put("any_pet", radio);
                    params.put("fcm_id", FCM_ID);
                    params.put("latitude", lat.toString());
                    params.put("longitude", lng.toString());
                    params.put("city_id", city_id);
                    params.put("state_id", state_id);
                    params.put("pincode", zipcode);
                    params.put("pet_name", "");
                    params.put("pet_age", "");
                    params.put("pet_bareed", "");

                    System.out.println("<><>===params"+params);
                    return params;
                }
            };
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);*/

          final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                   progressDialog.setMessage("Processing...");
                   progressDialog.show();
                   System.out.println("Path=======        "+destination.getAbsolutePath());
                   AndroidNetworking.upload(BaseUrl+Signup_Api)
                           .addMultipartParameter("name",username)
                           .addMultipartParameter("email",email)
                           .addMultipartParameter("mobile",phone)
                           .addMultipartParameter("password",password)
                           .addMultipartParameter("any_pet",radio)
                           .addMultipartParameter("fcm_id",FCM_ID)
                           .addMultipartParameter("latitude",lat.toString())
                           .addMultipartParameter("longitude",lng.toString())
                           .addMultipartParameter("city_id",city_id)
                           .addMultipartParameter("state_id",state_id)
                           .addMultipartParameter("pincode",zipcode)
                           .addMultipartFile("image",destination)
                           .addMultipartParameter("pet_name","")
                           .addMultipartParameter("pet_age","")
                           .addMultipartParameter("pet_bareed","")


                           .setTag("edit profile list")
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

                                       System.out.println("<><====jsonobject"+jsonObject);

                                       if (result.equalsIgnoreCase("true")) {
                                           JSONObject obj1 = jsonObject.getJSONObject("data");
                                           String id=obj1.getString("id");
                                           String fullname=obj1.getString("fullname");
                                           String email=obj1.getString("email");
                                           String mobile=obj1.getString("mobile");

                                           session1.setLogin(true);
                                           session1.setUserId(id);
                                           session1.setMobile(mobile,email);
                                           session1.setUser_name(fullname);

                                           Toast.makeText(SignupActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                           Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                           startActivity(intent);
                                           finish();
                                       } else {

                                           Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


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



















           private void CityApi(String state_id) {
           final ProgressDialog loading = new ProgressDialog(SignupActivity.this);
           loading.setMessage("Please Wait...");
           loading.show();
           StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl+Get_City,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loading.dismiss();
                            JSONObject eventObject = new JSONObject(response);
                            String result = eventObject.getString("result");
                            System.out.println("<><><===city"+eventObject.toString());
                            if (result.equals("true")) {
                                JSONArray jsonArray = eventObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    city_name_ = jsonObject1.getString("city_name");
                                    Spin_city_id = jsonObject1.getString("city_id");
                                    String Spinstateid = jsonObject1.getString("state_id");
                                    citylist.add(city_name_);
                                    cityListModels.add(new CityListModel(city_name_, Spin_city_id));
                                }
                                city.setAdapter(new ArrayAdapter<String>(SignupActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item, citylist));

                            } else {
                                Toast.makeText(SignupActivity.this, "response not true", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("Tag", e.getMessage());

                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state_id", state_id);
                return params;
                }
                };
           RequestQueue requestQueue = Volley.newRequestQueue(this);
           requestQueue.add(stringRequest);
    }






           private void StateApi() {
            final ProgressDialog loading = new ProgressDialog(SignupActivity.this);
            loading.setMessage("Please Wait...");
            loading.show();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseUrl+Get_State,
                    new Response.Listener<String>() {
                       @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("<><state"+response);
                                loading.dismiss();
                                JSONObject eventObject = new JSONObject(response);
                                String result = eventObject.getString("result");
                                if (result.equals("Success")) {
                                    JSONArray jsonArray = eventObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        state_name = jsonObject1.getString("name");
                                        spin_state_id = jsonObject1.getString("id");

                                        statelist.add(state_name);
                                        stateListModels.add(new StateListModel(state_name,spin_state_id));
                                    }

                                    state.setAdapter(new ArrayAdapter<String>(SignupActivity.this,
                                            android.R.layout.simple_spinner_dropdown_item, statelist));

                                } else {
                                    Toast.makeText(SignupActivity.this, "response not true", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.d("Tag", e.getMessage());

                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    return map;
                }
                };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
                 }













       private void getAddress(Double lat, Double lng) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(SignupActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            //et_Address.setText(address);
        } catch (Exception e){

        }


    }

       private void showGPSDisabledAlertToUser() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SignupActivity.this);
                alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Goto Settings Page To Enable GPS",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){
                                        Intent callGPSSettingIntent = new Intent(
                                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        SignupActivity.this.startActivity(callGPSSettingIntent);
                                    }
                                });
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });
                android.app.AlertDialog alert = alertDialogBuilder.create();
                alert.show();
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
                            Toast.makeText(SignupActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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
                // getNumber(getActivity().getContentResolver());
            }else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }
        }
    }



}