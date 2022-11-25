package com.healthcare.dogtraining.ui.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.healthcare.dogtraining.HomeActivity;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;
import com.healthcare.dogtraining.ui.TRAININGCOURSE.PurchasedPlanActivity;
import com.healthcare.dogtraining.utils.Constants;
import com.healthcare.dogtraining.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Session1 session;
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("token_fcm_service", s);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        session=new Session1(getApplicationContext());

        if(remoteMessage.getData().size() > 0){

            Log.e(TAG, "Data_Payload: " + remoteMessage.getData().toString());

            try {

                handleDataMessage(remoteMessage);

            } catch (Exception e) {

                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Log.e(TAG, "title_body: " + title+" "+body);

            handleNotification(title,body);
        }

    }


    private void handleNotification(String title, String body) {


        System.out.println("<><><");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            String format = s.format(new Date());

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Log.e("rr22","pppp22");
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Constants.CHANNEL_NAME);
                pushNotification.putExtra("message", body);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.showNotificationMessage(title, body, "", resultIntent);
                notificationUtils.playNotificationSound();
            }else{
                Log.e("rrrr","pppp");
                sendNoti(title, body);
                // If the app is in background, firebase itself handles the notification


            }
        }

    }

    private void sendNoti(String title, String body) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("NOTIFICATION", "NOTIFICATION");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        String channelId = "Default";
        b.setAutoCancel(false);
        b.setDefaults(Notification.DEFAULT_ALL);
        b.setWhen(System.currentTimeMillis());
        b.setSmallIcon(R.drawable.dogacc);
        b.setContentTitle(title);
        b.setContentText(body);
        b.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
        b.setContentIntent(contentIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, b.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void handleDataMessage(RemoteMessage remoteMessage) {
        // Log.e(TAG, "push_json: " + json.toString());
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON OBJECT", object.toString());
            String title = object.getString("title");

            System.out.println("check title" + title);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                session = new Session1(this);


                    Log.e("type splash = ", "" + session.isLoggedIn());
                    Intent intent = new Intent(this, PurchasedPlanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                    String channelId = "Default";
                    NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)

                            .setSmallIcon(R.drawable.dogacc)
                            .setContentTitle(title)

                            .setAutoCancel(true).setContentIntent(pendingIntent);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                    }

                    manager.notify(generateRandom(), builder.build());

                 }else
            {
                    Log.e("type splash = ", "" + session.isLoggedIn());
                    Intent intent = new Intent(this, PurchasedPlanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    String channelId = "Default";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                            // .setLargeIcon(bitmap)
                            .setSmallIcon(R.drawable.dogacc)
                            .setContentTitle(title)

                            .setAutoCancel(true).setContentIntent(pendingIntent);

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                    }
                    manager.notify(generateRandom(), builder.build());


            }

        } catch (JSONException e) {
            Log.e("error", "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e("error", "Exception: " + e.getMessage());
        }
    }

    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }



}


