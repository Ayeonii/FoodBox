package com.example.dldke.foodbox;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationDetails;
import com.example.dldke.foodbox.Activity.MainActivity;
import com.example.dldke.foodbox.Activity.SplashActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.MyRecipe.MyRecipeBoxActivity;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.api.client.json.Json;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PushListenerService extends FirebaseMessagingService {
    public static final String TAG = PushListenerService.class.getSimpleName();

    // Intent action used in local broadcast
    public static final String ACTION_PUSH_NOTIFICATION = "push-notification";
    // Intent keys
    public static final String INTENT_SNS_NOTIFICATION_FROM = "from";
    public static final String INTENT_SNS_NOTIFICATION_DATA = "data";



    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(TAG, "Registering push notifications token: " + token);
        MainActivity.getPinpointManager(getApplicationContext()).getNotificationClient().registerDeviceToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Message: " + remoteMessage.getData());

        final NotificationClient notificationClient = MainActivity.getPinpointManager(getApplicationContext()).getNotificationClient();

        final NotificationDetails notificationDetails = NotificationDetails.builder()
                .from(remoteMessage.getFrom())
                .mapData(remoteMessage.getData())
                .intentAction(NotificationClient.FCM_INTENT_ACTION)
                .build();

        NotificationClient.CampaignPushResult pushResult = notificationClient.handleCampaignPush(notificationDetails);

        if (!NotificationClient.CampaignPushResult.NOT_HANDLED.equals(pushResult)) {
            /**
             The push message was due to a Pinpoint campaign.
             If the app was in the background, a local notification was added
             in the notification center. If the app was in the foreground, an
             event was recorded indicating the app was in the foreground,
             for the demo, we will broadcast the notification to let the main
             activity display it in a dialog.
             */
            if (NotificationClient.CampaignPushResult.APP_IN_FOREGROUND.equals(pushResult)) {
                /* Create a message that will display the raw data of the campaign push in a dialog. */
                final HashMap<String, String> dataMap = new HashMap<>(remoteMessage.getData());
                broadcast(remoteMessage.getFrom(), dataMap);

            }
            else if (NotificationClient.CampaignPushResult.SILENT.equals(pushResult)) {
                /* Create a message that will display the raw data of the campaign push in a dialog. */
                final HashMap<String, String> dataMap = new HashMap<>(remoteMessage.getData());
                Log.e("Silent","들어온거야만거야");
                sendNotification(remoteMessage);
                //broadcast(remoteMessage.getFrom(), dataMap);


            }

            return;
        }
    }

    private void broadcast(final String from, final HashMap<String, String> dataMap) {
        Intent intent = new Intent(ACTION_PUSH_NOTIFICATION);
        intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, from);
        intent.putExtra(INTENT_SNS_NOTIFICATION_DATA, dataMap);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Helper method to extract push message from bundle.
     *
     * @param data bundle
     * @return message string from push notification
     */
    public static String getMessage(HashMap<String,String> data , String type) {
        //return data.containsKey("default") ? data.getString("default") : data.getString("message", "");
        try{
            JSONObject json = new JSONObject(data.get("pinpoint.jsonBody"));
            return json.getString(type);
        }
        catch (Exception e){
            Log.d("error","error");
            return "error";
        }
    }


    private void sendNotification(RemoteMessage remoteMessage) {
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = null;
        HashMap<String, String> dataMap = new HashMap<>(remoteMessage.getData());
        String message = getMessage(dataMap,"data");
        String title = getMessage(dataMap,"title");
        String locate = getMessage(dataMap,"locate");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("locate",locate);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        builder.setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.e("Nofi제발","여긴왔어?");
        //intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, remoteMessage.getFrom());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setContentTitle(remoteMessage.getData().get("title"))
                .setContentTitle("Hello")
                //.setContentText(remoteMessage.getData().get("body"))
                .setContentText("Hello??")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);


        notificationManager.notify(0, notificationBuilder.build());
        */
    }

}
