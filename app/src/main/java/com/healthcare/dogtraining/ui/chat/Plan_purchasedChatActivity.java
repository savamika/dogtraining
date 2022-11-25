package com.healthcare.dogtraining.ui.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.baoyz.widget.PullRefreshLayout;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.utils.ImageRotator;
import com.healthcare.dogtraining.utils.ImageVideoUtils;
import com.healthcare.dogtraining.utils.VolleySingleton;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.random.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.faq;
import static com.healthcare.dogtraining.API.Base_Url.user_chat;
import static com.healthcare.dogtraining.API.Base_Url.user_chat_at_plan;
import static com.healthcare.dogtraining.API.Base_Url.user_chat_list;
import static com.healthcare.dogtraining.API.Base_Url.user_plan_chat_list;
import static com.healthcare.dogtraining.VendorSignup.AddVendorActivity.bitmapToFile;

public class Plan_purchasedChatActivity extends AppCompatActivity {
    RecyclerView recyclerview, userrecyclerview;
    Session1 session1;
    List<GetFaqModel> getFaqModels = new ArrayList<>();
    List<GetAdminChatModel> getAdminChatModels = new ArrayList<>();
    FaqAdapter adapter;
    RelativeLayout btn_send;
    ImageView  btn_camera, btn_video;
    EditText text_send;
    GetAdminChatAdapter adminChatAdapter;
    String planname;
    TextView tv_commandname;
    File file, filenew;
    Uri UploadPdfUri;
    String filename;
    PickImageDialog dialog;
    String type = "", command_id;
    String selectedVideoPath;
    File destination;
    int post1 = 0, post2 = 0;
    private int valueCode = 0;
    Uri contentURI;
    private static final String VIDEO_DIRECTORY = "/demonuts";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, GALLERY = 2;
    public static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 109;
    private static final int STORAGE_PERMISSION_CODE = 123;
    RelativeLayout relativelayout;
    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    ImageView ic_mic_startrecord, ic_mic_stoprecord,backbutton;
    RelativeLayout progress_view;
 String plan_order_id;
    PullRefreshLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_chat);

        progress_view = findViewById(R.id.progress_view);
        backbutton = findViewById(R.id.backbutton);
        recyclerview = findViewById(R.id.recyclerview);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userrecyclerview = findViewById(R.id.userrecyclerview);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        tv_commandname = findViewById(R.id.tv_commandname);
        btn_camera = findViewById(R.id.btn_camera);
        btn_video = findViewById(R.id.btn_video);
        relativelayout = findViewById(R.id.relativelayout);
        buttonStart = (Button) findViewById(R.id.button);
        buttonStop = (Button) findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button) findViewById(R.id.button4);
        ic_mic_startrecord = (ImageView) findViewById(R.id.ic_mic_startrecord);
        ic_mic_stoprecord = (ImageView) findViewById(R.id.ic_mic_stoprecord);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);


        session1 = new Session1(Plan_purchasedChatActivity.this);
        GEtFaqlistApi();
        GetChatusreApi();

        checkRunTimePermission();

        if (getIntent() != null) {
            try {
                planname = getIntent().getStringExtra("planname");
                command_id = getIntent().getStringExtra("command_id");
                plan_order_id = getIntent().getStringExtra("plan_order_id");
                Log.e("plan_order_id", "onCreate: "+plan_order_id );
                if (planname==null){
                    tv_commandname.setText("Chat");
                }else {
                    tv_commandname.setText(planname);
                }

                System.out.println("<><><>====command_id" + command_id + "planname" + planname);
            } catch (Exception e) {
            }
        }else{
            tv_commandname.setText("Chat");
        }


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post1 = 0;
                post2 = 1;
                type = "image";
                selectImage1();
            }
        });

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "video";
                post1 = 1;
                post2 = 0;

                showPictureDialog();
                // checkRunTimePermission();
            }
        });

        relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Plan_purchasedChatActivity.this, DummyActivity.class);
                startActivity(intent);
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text_send.getText().toString().equalsIgnoreCase("")) {
                    sendmessage(text_send.getText().toString());
                }
            }
        });

        getChatAdminApi();

        random = new Random() {
            @Override
            public int nextBits(int i) {
                return 0;
            }
        };


        ic_mic_startrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ic_mic_stoprecord.setVisibility(View.VISIBLE);
                ic_mic_startrecord.setVisibility(View.GONE);
                if (checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    Toast.makeText(Plan_purchasedChatActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                    file = new File(AudioSavePathInDevice);
                    System.out.println("<><midgfdifdfvid" + AudioSavePathInDevice);
                } else {
                    requestPermission();
                }

            }
        });

        ic_mic_stoprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ic_mic_stoprecord.setVisibility(View.GONE);
                ic_mic_startrecord.setVisibility(View.VISIBLE);
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                file = new File(AudioSavePathInDevice);
                if (file != null) {

                    AndroidNetworking.upload(BaseUrl + user_chat_at_plan)
                            .addMultipartParameter("user_id", session1.getUserId())
                            .addMultipartParameter("message", text_send.getText().toString())
                            .addMultipartParameter("plan_id", command_id)
                            .addMultipartParameter("plan_order_id", plan_order_id)
                            .addMultipartFile("file", file)

                            .setTag("chat")
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void onResponse(JSONObject jsonObject) {

                                    try {

                                        Log.e("Sendmesage", jsonObject.toString());
                                        System.out.println("Sendmesagefile=====    " + jsonObject.toString());
                                        String result = jsonObject.getString("result");
                                        String msg = jsonObject.getString("msg");

                                        if (result.equalsIgnoreCase("true")) {
                                            text_send.getText().clear();

                                            getChatAdminApi();

                                        } else {

                                            Toast.makeText(Plan_purchasedChatActivity.this, msg, Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        // progressDialog.dismiss();
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError error) {
                                    // progressDialog.dismiss();
                                    Log.e("error = ", "" + error);
                                }
                            });

                }


                Toast.makeText(Plan_purchasedChatActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();

                    System.out.println("<><fkgrfgxxxkgjkfg" + AudioSavePathInDevice);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(Plan_purchasedChatActivity.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });

         layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatAdminApi();
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select video from gallery",
                "Record video from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseVideoFromGallary();
                                break;
                            case 1:
                                takeVideoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    private void takeVideoFromCamera() {
        Intent intentCaptureVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intentCaptureVideo.resolveActivity(getPackageManager()) != null) {
            intentCaptureVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 53);//120 sec = 2min //10000sec //10 min
            intentCaptureVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intentCaptureVideo, 0);
        }
    }

    private void chooseVideoFromGallary() {
        if (ActivityCompat.checkSelfPermission(Plan_purchasedChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale), REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_video)), 1);
        }
    }


    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.permission_title_rationale));
            builder.setMessage(rationale);
            builder.setPositiveButton(getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Plan_purchasedChatActivity.this, new String[]{permission}, requestCode);
                        }
                    });
            builder.setNegativeButton(getString(R.string.cancel), null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }


    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {


            if (post1 == 1) {
                //post1 = 1;

                post2 = 0;
                type = "video";
                showPictureDialog();

            } else if (post2 == 1) {
                // post1 = 0;
                selectImage1();
                post2 = 1;
                type = "image";


            }

        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            System.out.println("<><>===gfjgkfgkdfg" + resultCode);

            if (data != null) {
                Bitmap bmThumbnail;
                contentURI = data.getData();


                selectedVideoPath = getPath(contentURI);
                file = new File(selectedVideoPath);

                Log.e("<><ndnng", selectedVideoPath);

                System.out.println("<><><=====filefffff" + file);
                System.out.println("<><><=====selectedVideoPath" + selectedVideoPath);


                saveVideoToInternalStorage(selectedVideoPath);
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(selectedVideoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                // img_show.setImageBitmap(bmThumbnail);
            }

            if (requestCode == GALLERY) {
                if (data != null) {
                    Bitmap bmThumbnail;
                    contentURI = data.getData();


                    selectedVideoPath = getPath(contentURI);
                    file = new File(selectedVideoPath);

                    Log.e("<><ndnng", selectedVideoPath);

                    System.out.println("<><><=====filefffff" + file);
                    System.out.println("<><><=====selectedVideoPath" + selectedVideoPath);


                    saveVideoToInternalStorage(selectedVideoPath);
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(selectedVideoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                    // img_show.setImageBitmap(bmThumbnail);
                }
            }

            if (post2 == 0) {

                System.out.println("<><>===post2" + post2);

                if (requestCode == valueCode) {
                    if (data != null) {
                        Uri contentURI = data.getData();
                        String selectedVideoPath = getPath(contentURI);
                        File videoFile = new File(selectedVideoPath);
                        String finalVideoFilePath = ImageVideoUtils.generatePath(contentURI, this);
                        Bitmap thumbBitmap = ImageVideoUtils.getVidioThumbnail(finalVideoFilePath); //ImageVideoUtil.getCompressBitmap();

                        int rotation = ImageRotator.getRotation(this, contentURI, true);
                        thumbBitmap = ImageRotator.rotate(thumbBitmap, rotation);

                        if (thumbBitmap != null) {
                            if (valueCode == 0) {
                                file = videoFile;

                                System.out.println("<><><dddddddd=====file" + file);
                            }

                        }

                    }


                } else if (requestCode == 1) {

                    Uri selectedImageUri = data.getData();
                    String filemanagerstring = getPath1(selectedImageUri);

                    try {


                        Bitmap bmThumbnail;
                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filemanagerstring, MediaStore.Images.Thumbnails.MINI_KIND);
                        //img_show.setImageBitmap(bmThumbnail);
                        file = new File(filemanagerstring);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } else if (requestCode == SELECT_FILE) {

                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {

                onCaptureImageResult(data);

            } else if (requestCode == GALLERY) {
                if (data != null) {
                    Bitmap bmThumbnail;
                    contentURI = data.getData();


                    selectedVideoPath = getPath(contentURI);
                    file = new File(selectedVideoPath);

                    Log.e("<><ndnng", selectedVideoPath);

                    System.out.println("<><><=====filefffff" + file);
                    System.out.println("<><><=====selectedVideoPath" + selectedVideoPath);


                    saveVideoToInternalStorage(selectedVideoPath);
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(selectedVideoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                    // img_show.setImageBitmap(bmThumbnail);
                }
            }


        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            //destination.createNewFile();
            fo = new FileOutputStream(destination);
            if (destination != null) {
                filenew = new File(destination.getAbsolutePath());

            } else {
                Toast.makeText(Plan_purchasedChatActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            Log.e("file", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("file", e.toString());
            e.printStackTrace();
        }
        // img_show.setImageBitmap(thumbnail);


    }


    private String getPath1(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {

            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = Plan_purchasedChatActivity.this.getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            destination = new File(cursor.getString(cursor.getColumnIndex(filePath[0])));
            cursor.close();

            if (destination != null) {
                filenew = new File(destination.getAbsolutePath());
                //  Log.e("galleryimag", filenew.toString());

            } else {
                Toast.makeText(Plan_purchasedChatActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
            }

            try {
                bm = MediaStore.Images.Media.getBitmap(Plan_purchasedChatActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // img_show.setImageBitmap(bm);
        }


    }


    private String getPath(Uri uri) {

        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = Plan_purchasedChatActivity.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {

            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;


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
                            Toast.makeText(Plan_purchasedChatActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            try {

            } catch (Exception e) {

            }

            if (isPermitted) {

                if (post1 == 1)
                    showPictureDialog();
                else if (post2 == 1) {
                    selectImage1();
                }
                // selectImage();
            } else {
            }
        }

        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(Plan_purchasedChatActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Plan_purchasedChatActivity.this, "Permission Denied ", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    private void saveVideoToInternalStorage(String filePath) {

        File newfile;

        try {

            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");

            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }

            if (currentFile.exists()) {

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            } else {
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void sendmessage(String string) {
        System.out.println("<><====userid" + session1.getUserId());
        System.out.println("<><====message" + text_send.getText().toString());
        System.out.println("<><====image" + file);
        System.out.println("<><====image" + command_id);
        Log.e("command_id", "sendmessage: "+command_id );
        final ProgressDialog progressDialog = new ProgressDialog(Plan_purchasedChatActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        if (file != null) {
            AndroidNetworking.upload(BaseUrl + user_chat_at_plan)
                    .addMultipartParameter("user_id", session1.getUserId())
                    .addMultipartParameter("message", text_send.getText().toString())

                    .addMultipartParameter("plan_id", command_id)
                    .addMultipartParameter("plan_order_id", plan_order_id)
                    .addMultipartFile("file", file)

                    .setTag("chat")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            try {
                                progressDialog.dismiss();
                                //Log.e(" post home", " " + jsonObject);
                                Log.e("Sendmesage", jsonObject.toString());
                                System.out.println("Sendmesagefile=====    " + jsonObject.toString());
                                String result = jsonObject.getString("result");
                                String msg = jsonObject.getString("msg");

                                if (result.equalsIgnoreCase("true")) {
                                    text_send.getText().clear();
                                    recyclerview.setVisibility(View.GONE);
                                    getChatAdminApi();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Plan_purchasedChatActivity.this, msg, Toast.LENGTH_LONG).show();
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

        } else {
            AndroidNetworking.upload(BaseUrl + user_chat_at_plan)

                    .addMultipartParameter("plan_order_id", plan_order_id)
                    .addMultipartParameter("plan_id", command_id)
                    .addMultipartParameter("user_id", session1.getUserId())
                    .addMultipartParameter("message", text_send.getText().toString())
//                    .addMultipartFile("files", file)

                    .setTag("chat")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            try {
                                progressDialog.dismiss();
                                //Log.e(" post home", " " + jsonObject);
                                Log.e("Sendmesage", jsonObject.toString());
                                System.out.println("Sendmesage=====    " + jsonObject.toString());
                                String result = jsonObject.getString("result");
                                String msg = jsonObject.getString("msg");

                                if (result.equalsIgnoreCase("true")) {

                                    text_send.getText().clear();

                                    getChatAdminApi();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Plan_purchasedChatActivity.this, msg, Toast.LENGTH_LONG).show();


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

    private void selectImage1() {
        PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        PickImageDialog finalDialog1 = dialog;
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                finalDialog1.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {
                    file = bitmapToFile(Plan_purchasedChatActivity.this, r.getBitmap());
                    Log.e("file", "" + file);
                    System.out.println("<><>====");
                    String filename = file.getName();
                    Log.e("filweName = ", filename);
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(Plan_purchasedChatActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(Plan_purchasedChatActivity.this);


    }

    private void getChatAdminApi() {
        progress_view.setVisibility(View.VISIBLE);
        Log.e("plan_order_id", "getChatAdminApi: "+plan_order_id);
        String url = BaseUrl + user_plan_chat_list;
        AndroidNetworking.post(url)
                .addBodyParameter("plan_id", command_id)
                .addBodyParameter("user_id", session1.getUserId())
                .addBodyParameter("plan_order_id", plan_order_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("jsonObject-->", "onResponse: "+jsonObject);

                        try {
                            getAdminChatModels.clear();
                            String result = jsonObject.getString("result");
                            if (result.equalsIgnoreCase("false")){
                                recyclerview.setVisibility(View.VISIBLE);
                            }else{
                                recyclerview.setVisibility(View.GONE);
                            }
                             String msg = jsonObject.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    GetAdminChatModel allCommunityModel = new GetAdminChatModel
                                            (dataObject.getString("id"),
                                                    dataObject.getString("admin"),
                                                    dataObject.getString("user_id"),
                                                    dataObject.getString("message"),
                                                    dataObject.getString("chat_image"),
                                                    dataObject.getString("chat_video"),
                                                    dataObject.getString("chat_audio"),
                                                    dataObject.getString("type"),
                                                    dataObject.getString("types"),
                                                    dataObject.getString("date_time"),
                                                    dataObject.getString("fullname"),
                                                    dataObject.getString("image"));
                                    getAdminChatModels.add(allCommunityModel);

                                }

                            } else {

                            }

                          /*  adminChatAdapter = new GetAdminChatAdapter(Plan_purchasedChatActivity.this, getAdminChatModels);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(Plan_purchasedChatActivity.this);
                            userrecyclerview.setLayoutManager(mLayoutManger);
                            userrecyclerview.setLayoutManager(new LinearLayoutManager(Plan_purchasedChatActivity.this, RecyclerView.VERTICAL, false));
                            userrecyclerview.setItemAnimator(new DefaultItemAnimator());
                            userrecyclerview.setItemAnimator(new DefaultItemAnimator());
                            userrecyclerview.setItemViewCacheSize(getAdminChatModels.size());
                            userrecyclerview.setAdapter(adminChatAdapter);
                            adminChatAdapter.notifyDataSetChanged();
                            userrecyclerview.scrollToPosition(userrecyclerview.getAdapter().getItemCount() - 1);
                            progress_view.setVisibility(View.INVISIBLE);
                            layout.setRefreshing(false);*/

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


    private void send_msg_to_admin(String toString) {
        final String ed_send = text_send.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + user_chat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            System.out.print("<><===namimi" + obj);
                            if (result.equalsIgnoreCase("true")) {
                                text_send.getText().clear();

                                getChatAdminApi();

                            } else {

                                Toast.makeText(Plan_purchasedChatActivity.this, msg, Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session1.getUserId());
                params.put("message", ed_send);

                return params;
            }
        };

        VolleySingleton.getInstance(Plan_purchasedChatActivity.this).addToRequestQueue(stringRequest);
    }


    private void GetChatusreApi() {


    }

    private void GEtFaqlistApi() {

        String url = BaseUrl + faq;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", session1.getUserId())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        System.out.print("");
                        try {
                            String result = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            System.out.print("complslksjdksd<><>===" + jsonObject);
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    GetFaqModel allCommunityModel = new GetFaqModel
                                            (dataObject.getString("id"),
                                                    dataObject.getString("question"),
                                                    dataObject.getString("answer"),
                                                    dataObject.getString("status")

                                            );
                                    getFaqModels.add(allCommunityModel);
                                }
                            } else {

                            }
                            adapter = new FaqAdapter(getFaqModels, Plan_purchasedChatActivity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(Plan_purchasedChatActivity.this);
                            recyclerview.setLayoutManager(mLayoutManger);
                            recyclerview.setLayoutManager(new LinearLayoutManager(Plan_purchasedChatActivity.this, RecyclerView.VERTICAL, false));
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progress_view.setVisibility(View.INVISIBLE);
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


    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Plan_purchasedChatActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(DummyActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DummyActivity.this,"Permission Denied ",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
*/
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}