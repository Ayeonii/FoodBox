package com.example.dldke.foodbox;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient;
import com.example.dldke.foodbox.Activity.DeepLinkActivity;
import com.example.dldke.foodbox.Activity.MainActivity;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import static android.support.constraint.Constraints.TAG;

public class PushListenerService extends GcmListenerService {
    public static final String LOGTAG = PushListenerService.class.getSimpleName();

    // Intent action used in local broadcast
    public static final String ACTION_PUSH_NOTIFICATION = "push-notification";
    // Intent keys
    public static final String INTENT_SNS_NOTIFICATION_FROM = "from";
    public static final String INTENT_SNS_NOTIFICATION_DATA = "data";

    /**
     * Helper method to extract push message from bundle.
     *
     * @param data bundle
     * @return message string from push notification
     */
    public static String getMessage(Bundle data) {
        // If a push notification is sent as plain
        // text, then the message appears in "default".
            // Otherwise it's in the "message" for JSON format.
            return data.containsKey("default") ? data.getString("default") : data.getString(
            "message", "");
            }

    private void broadcast(final String from, final Bundle data) {
            Intent intent = new Intent(ACTION_PUSH_NOTIFICATION);
            intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, from);
            intent.putExtra(INTENT_SNS_NOTIFICATION_DATA, data);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }

    @Override
    public void onMessageReceived(final String from, final Bundle data) {
            Log.d(LOGTAG, "From:" + from);
            Log.d(LOGTAG, "Data:" + data.toString());
    AWSConfiguration cf = new AWSConfiguration(this);
    AWSCredentialsProvider cp = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return null;
            }

            @Override
            public void refresh() {

            }
        };

    NotificationClient notificationClient;
    PinpointManager pinpointManager;
    if(DeepLinkActivity.pinpointManager == null){

        AWSMobileClient.getInstance().initialize(this).execute();
        PinpointConfiguration pc = new PinpointConfiguration(
                getApplicationContext(),
                cp,
                cf
        );
        //PinpointConfiguration pinpointConfig = new PinpointConfiguration(
        //       getApplicationContext(),
        //        AWSMobileClient.getInstance().getCredentialsProvider(),
        //       AWSMobileClient.getInstance().getConfiguration());

        pinpointManager = new PinpointManager(pc);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String deviceToken =
                            InstanceID.getInstance(PushListenerService.this).getToken(
                                    "163609238969",
                                    GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                    Log.e("NotError", deviceToken);
                    pinpointManager.getNotificationClient()
                            .registerGCMDeviceToken(deviceToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        notificationClient = pinpointManager.getNotificationClient();
    }
    else{
        notificationClient = DeepLinkActivity.pinpointManager.getNotificationClient();
    }


    NotificationClient.CampaignPushResult pushResult =
            notificationClient.handleGCMCampaignPush(from, data, this.getClass());

    if (!NotificationClient.CampaignPushResult.NOT_HANDLED.equals(pushResult)) {
        // The push message was due to a Pinpoint campaign.
        // If the app was in the background, a local notification was added
        // in the notification center. If the app was in the foreground, an
        // event was recorded indicating the app was in the foreground,
        // for the demo, we will broadcast the notification to let the main
        // activity display it in a dialog.
        if (NotificationClient.CampaignPushResult.APP_IN_FOREGROUND.equals(pushResult)) {
            // Create a message that will display the raw
            //data of the campaign push in a dialog.
                data.putString(" message", String.format("Received Campaign Push:\n%s", data.toString()));
                broadcast(from, data);
            }
            return;
        }
    }
}
