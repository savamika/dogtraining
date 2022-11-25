package com.healthcare.dogtraining.ui.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.healthcare.dogtraining.MainActivity;
import com.healthcare.dogtraining.Model.ChatMessage;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.TRAININGCOURSE.PurchasedPlanActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.random.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Video_url;
import static com.healthcare.dogtraining.API.Base_Url.chat_video;
import static com.healthcare.dogtraining.API.Base_Url.faq;
import static com.healthcare.dogtraining.API.Base_Url.user_chat;
import static com.healthcare.dogtraining.VendorSignup.AddVendorActivity.bitmapToFile;

public class ChatActivity extends AppCompatActivity {
    public static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 109;
    public static final int RequestPermissionCode = 1;
    private static final String VIDEO_DIRECTORY = "/demonuts";
    private static final int STORAGE_PERMISSION_CODE = 123;
    RecyclerView recyclerview, userrecyclerview;
    Session1 session1;
    List<GetFaqModel> getFaqModels = new ArrayList<>();
    List<GetAdminChatModel> getAdminChatModels = new ArrayList<>();
    List<ChatMessage> chatMessageList = new ArrayList<>();
    FaqAdapter adapter;
    RelativeLayout btn_send;
    ImageView btn_camera, btn_video;
    EditText text_send;
    GetAdminChatAdapter adminChatAdapter;
    String planname;
    TextView tv_commandname;
    File file, filenew;
    Uri UploadPdfUri;
    String filename;
    PickImageDialog dialog;
    String useriddeivce, type = "", command_id="Help";
    String selectedVideoPath;
    File destination;
    int post1 = 0, post2 = 0;
    Uri contentURI;
    RelativeLayout relativelayout;
    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String plan_order_id, RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    MediaPlayer mediaPlayer;
    ImageView ic_mic_startrecord, ic_mic_stoprecord, backbutton;
    RelativeLayout progress_view;
    private int valueCode = 0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, GALLERY = 2;
    private DatabaseReference mReference;

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
        if (getIntent() != null) {
            try {
                planname = getIntent().getStringExtra("planname");
                command_id = getIntent().getStringExtra("command_id");
                plan_order_id = getIntent().getStringExtra("plan_order_id");
                Log.e("TAG", "onCreate: " + plan_order_id);
                if (planname == null) {
                    tv_commandname.setText("Chat");

                } else {
                    tv_commandname.setText(planname);
                }

                System.out.println("<><><>====command_id" + command_id + "planname" + planname);
            } catch (Exception e) {
            }

        } else {
            tv_commandname.setText("Chat");
        }
        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);


        session1 = new Session1(ChatActivity.this);
        useriddeivce = session1.getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessageList.clear();
                for (DataSnapshot chatt : snapshot.getChildren()) {

                    String message = chatt.child("message").getValue(String.class);
                    String senderID = chatt.child("senderID").getValue(String.class);
                    String receiveerID = chatt.child("receiveerID").getValue(String.class);
                    String username = chatt.child("username").getValue(String.class);
                    String image = chatt.child("image").getValue(String.class);
                    String video = chatt.child("video").getValue(String.class);
                    String time = chatt.child("time").getValue(String.class);
                    String Recording = chatt.child("Recording").getValue(String.class);
                    String status = chatt.child("status").getValue(String.class);
                    String planname = chatt.child("planname").getValue(String.class);
                    String showcommand_id = chatt.child("command_id").getValue(String.class);
                    String plan_order_id = chatt.child("plan_order_id").getValue(String.class);

                    // FireCons.currentuser=currentuser;


                    Log.e("-->>useriddeivce", "onDataChange: " + useriddeivce);
                    Log.e("-->>message", "onDataChange: " + image);
                    Log.e("-->>message", "onDataChange: " + message);
                    Log.e("-->>kisko", "onDataChange: " + receiveerID);
                    Log.e("-->>kisne", "onDataChange: " + senderID);


                    //  Log.e("-->>message", "onDataChange: "+messagename );
                    //  Log.e("-->>message", "onDataChange: "+currentuser);

                    // System.out.println("<><><><>>>FireCons.currentuser"+FireCons.currentuser);




                        if (command_id.equalsIgnoreCase(showcommand_id)) {
                            ChatMessage chatMessage = new ChatMessage(senderID, receiveerID, message, username, image, video, time, Recording, "", planname, showcommand_id, plan_order_id);
                            Log.e("insertID", "onDataChange: " + message);

                            chatMessageList.add(chatMessage);

                            System.out.println("conditiionif");

                            adminChatAdapter = new GetAdminChatAdapter(ChatActivity.this, chatMessageList);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ChatActivity.this);
                            userrecyclerview.setLayoutManager(mLayoutManger);
                            userrecyclerview.setLayoutManager(new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false));
                            userrecyclerview.setItemAnimator(new DefaultItemAnimator());
                            userrecyclerview.setItemAnimator(new DefaultItemAnimator());
                            userrecyclerview.setItemViewCacheSize(getAdminChatModels.size());
                            userrecyclerview.setAdapter(adminChatAdapter);
                            adminChatAdapter.notifyDataSetChanged();
                            userrecyclerview.setItemViewCacheSize(chatMessageList.size());
                            userrecyclerview.scrollToPosition(userrecyclerview.getAdapter().getItemCount() - 1);
                            progress_view.setVisibility(View.INVISIBLE);
                            recyclerview.setVisibility(View.GONE);
                            progressDialog.dismiss();

                        }
                        progress_view.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        Log.e("size", "onCreate: " + chatMessageList.size());
        if (chatMessageList.size() == 0) {
            progressDialog.dismiss();
            recyclerview.setVisibility(View.VISIBLE);
        } else {
            progressDialog.dismiss();
            recyclerview.setVisibility(View.GONE);
        }
        //  getChatAdminApi();
        GEtFaqlistApi();
        checkRunTimePermission();


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post1 = 0;
                post2 = 1;
                type = "image";
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            startActivityForResult(intent, 500);
                        }
                        else if (options[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 501);
                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
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
               /* Intent intent = new Intent(ChatActivity.this, DummyActivity.class);
                startActivity(intent);*/
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm");
                Log.e("TAG", "onClick: " + dateFormat.format(new Date()));
                String time = dateFormat.format(new Date());
if (command_id.equalsIgnoreCase("Help")){
    sendmessage(text_send.getText().toString());
    Log.e("TAG", ":onClick "+command_id );
    Log.e("TAG", ":onClick "+text_send.getText().toString() );
    if (!text_send.getText().toString().equalsIgnoreCase("")) {
        recyclerview.setVisibility(View.GONE);
        String messagesend = text_send.getText().toString();
        FirebaseDatabase.getInstance()
                .getReference()
                .child("chat")
                .push()
                .setValue(new ChatMessage(session1.getUserId(), "", messagesend, session1.getUser_name(), "", "", time, "", "", planname, command_id, plan_order_id));
        text_send.setText("");

    }

}else {
    if (!text_send.getText().toString().equalsIgnoreCase("")) {
        recyclerview.setVisibility(View.GONE);
        String messagesend = text_send.getText().toString();
        FirebaseDatabase.getInstance()
                .getReference()
                .child("chat")
                .push()
                .setValue(new ChatMessage(session1.getUserId(), "", messagesend, session1.getUser_name(), "", "", time, "", "", planname, command_id, plan_order_id));
        text_send.setText("");
        //  sendmessage(text_send.getText().toString());
    }

}

            }
        });


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
                    Toast.makeText(ChatActivity.this, "Recording started",
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
                    Log.e("file", "onClick: " + file);
                    AndroidNetworking.upload(BaseUrl + user_chat)
                            .addMultipartParameter("user_id", session1.getUserId())
                            .addMultipartParameter("message", text_send.getText().toString())
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

                                            //   getChatAdminApi();

                                        } else {

                                            Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_LONG).show();
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


                Toast.makeText(ChatActivity.this, "Recording Completed",
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
                Toast.makeText(ChatActivity.this, "Recording Playing",
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
        if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{permission}, requestCode);
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
        Log.e("resultCode", "onActivityResult: " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            System.out.println("<><>===gfjgkfgkdfg" + resultCode);

            if (requestCode == 0) {
                Bitmap bmThumbnail;
                contentURI = data.getData();
                Log.e("contentURI", "onActivityResult: "+contentURI );
                selectedVideoPath = getPath(contentURI);
                file = new File(selectedVideoPath);
                saveVideoToInternalStorage(selectedVideoPath);
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(selectedVideoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                String videoString = convert(bmThumbnail);
                Log.e("bmThumbnail", "onActivityResult: " + videoString);
                sendvideo(file,videoString);

            } else if (requestCode == 1) {
                if (data != null) {
                    Bitmap bmThumbnail;
                    contentURI = data.getData();
                    Log.e("contentURI", "onActivityResult: "+contentURI );

                    selectedVideoPath = getPath(contentURI);
                    file = new File(selectedVideoPath);

                    Log.e("<><ndnng", selectedVideoPath);

                    System.out.println("<><><=====filefffff" + file);
                    System.out.println("<><><=====selectedVideoPath" + selectedVideoPath);


                    saveVideoToInternalStorage(selectedVideoPath);
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(selectedVideoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                    Log.e("TAG", "onActivityResult: " + bmThumbnail);


                    String videoString = convert(bmThumbnail);

                    sendvideo(file,videoString);


                    // img_show.setImageBitmap(bmThumbnail);
                }
            }

         /*   if (post2 == 0) {

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
            }*/
           /* else if (requestCode == SELECT_FILE) {

                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA) {

                onCaptureImageResult(data);

            }
            else if (requestCode == GALLERY) {
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
            }*/
            else if (requestCode == 500) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 124, 124, true);
                Log.e("bitmap1", "onActivityResult: " + bitmap1);
                String IMageString = convert(bitmap1);
                Log.e("TAG", "onPickResult: " + IMageString);
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("chat")
                        .push()
                        .setValue(new ChatMessage(session1.getUserId(), "", "Image", session1.getUser_name(), IMageString, "", "time", "", "", planname, command_id, plan_order_id));


            }
            else if (requestCode == 501) {

                Bitmap bitmap = null;
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(ChatActivity.this.getContentResolver(), data.getData());
                    Log.e("bitmap1", "onActivityResult: " + bitmap);
                    String IMageString = convert(bitmap);
                    Log.e("TAG", "onPickResult: " + IMageString);
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("chat")
                            .push()
                            .setValue(new ChatMessage(session1.getUserId(), "", "Image", session1.getUser_name(), IMageString, "", "time", "", "", planname, command_id, plan_order_id));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }
    }





    public void ConvertToString(Uri uri,String ImageThumbnail){
      String  uriString = uri.toString();
        Log.d("data", "onActivityResult: uri"+uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);
            Log.d("data", "onActivityResult: bytes size="+bytes.length);
            Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes,Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes,Base64.DEFAULT);
           String Document=Base64.encodeToString(bytes,Base64.DEFAULT);
            Log.e("Document--=---=", "ConvertToString: "+Document );





        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
                Toast.makeText(ChatActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
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


    private void uploadvideo(Uri contentURI) {
        if (contentURI != null) {
            // save the selected video in Firebase storage
            final StorageReference reference = FirebaseStorage.getInstance().getReference().child("chat").child("Files/" + System.currentTimeMillis() + "." + getfiletype(contentURI));
            reference.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    Log.e("TAG", "onSuccess: "+uriTask );
                    while (!uriTask.isSuccessful()) ;
                  /*  // get the link of video
                    String downloadUri = uriTask.getResult().toString();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("chat");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("videolink", downloadUri);
                    reference1.child("" + System.currentTimeMillis()).setValue(map);*/
                    // Video uploaded successfully
                    // Dismiss dialog

                    Toast.makeText(ChatActivity.this, "Video Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded

                    Toast.makeText(ChatActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    // show the progress bar
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                }
            });
        }
    }
    private String getfiletype(Uri videouri) {
        ContentResolver r = getContentResolver();
        // get the file type ,in this case its mp4
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri));
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
            Cursor cursor = ChatActivity.this.getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            destination = new File(cursor.getString(cursor.getColumnIndex(filePath[0])));
            cursor.close();

            if (destination != null) {
                filenew = new File(destination.getAbsolutePath());
                //  Log.e("galleryimag", filenew.toString());

            } else {
                Toast.makeText(ChatActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
            }

            try {
                bm = MediaStore.Images.Media.getBitmap(ChatActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // img_show.setImageBitmap(bm);
        }


    }


    private String getPath(Uri uri) {

        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = ChatActivity.this.getContentResolver().query(uri, projection, null, null, null);
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
                            Toast.makeText(ChatActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChatActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "Permission Denied ", Toast.LENGTH_LONG).show();
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
        final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        if (file != null) {
            AndroidNetworking.upload(BaseUrl + user_chat)
                    .addMultipartParameter("user_id", session1.getUserId())
                    .addMultipartParameter("message", string)
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
                                    // getChatAdminApi();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_LONG).show();
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
            Log.e("sendmessage", "sendmessage: "+text_send.getText().toString() );
            AndroidNetworking.upload(BaseUrl + user_chat)
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

                                    //  getChatAdminApi();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_LONG).show();


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

    public String convert(Bitmap bitmap) {
        Log.e("TAG", "convert: " + bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
                Log.e("TAG", "onPickResult: " + r);
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm");
                Log.e("TAG", "onClick: " + dateFormat.format(new Date()));
                String time = dateFormat.format(new Date());


                if (r.getError() == null) {
                    Log.e("TAG", "onPickResult: " + r.getBitmap());

                    String IMageString = convert(r.getBitmap());
                    Log.e("TAG", "onPickResult: " + IMageString);
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("chat")
                            .push()
                            .setValue(new ChatMessage(session1.getUserId(), "", "Image", session1.getUser_name(), IMageString, "", time, "", "", planname, command_id, plan_order_id));

                    file = bitmapToFile(ChatActivity.this, r.getBitmap());
                    Log.e("file", "" + file);
                    System.out.println("<><>====");
                    String filename = file.getName();
                    Log.e("filweName = ", filename);
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(ChatActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(ChatActivity.this);


    }

    /*private void getChatAdminApi() {

        String url = BaseUrl + user_chat_list;
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", session1.getUserId())
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

                            adminChatAdapter = new GetAdminChatAdapter(ChatActivity.this, getAdminChatModels);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ChatActivity.this);
                            userrecyclerview.setLayoutManager(mLayoutManger);
                            userrecyclerview.setLayoutManager(new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false));
                            userrecyclerview.setItemAnimator(new DefaultItemAnimator());
                            userrecyclerview.setItemAnimator(new DefaultItemAnimator());
                            userrecyclerview.setItemViewCacheSize(getAdminChatModels.size());
                            userrecyclerview.setAdapter(adminChatAdapter);
                            adminChatAdapter.notifyDataSetChanged();
                            userrecyclerview.scrollToPosition(userrecyclerview.getAdapter().getItemCount() - 1);
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


    }*/


    private void sendvideo(File file,String Thumbnail) {
        final String ed_send = text_send.getText().toString();
        AndroidNetworking.upload(BaseUrl+chat_video)
                .addMultipartFile("video",file)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Log.e("sendvideo", "sendvideo " + jsonObject);
                            String result = jsonObject.getString("result");
                            Log.e("{TAG}", "result: " + result);
                            if (result.equalsIgnoreCase("true")) {
                                JSONObject data=jsonObject.getJSONObject("datas");
                                String video_file=data.getString("video_file");
                                Log.e("TAG", "onResponse: "+video_file );

                                FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("chat")
                                        .push()
                                        .setValue(new ChatMessage(session1.getUserId(), "", "Vid", session1.getUser_name(), Thumbnail, Video_url+video_file, "", "", "", planname, command_id, plan_order_id));


                            } else {
                                //categorytext.setVisibility(View.GONE);


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            //  categorytext.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ///categorytext.setVisibility(View.GONE);

                        Log.e("error_my_join", anError.toString());
                    }
                });
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
                            adapter = new FaqAdapter(getFaqModels, ChatActivity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ChatActivity.this);
                            recyclerview.setLayoutManager(mLayoutManger);
                            recyclerview.setLayoutManager(new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false));
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
        ActivityCompat.requestPermissions(ChatActivity.this, new
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