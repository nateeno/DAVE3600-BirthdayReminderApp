package com.example.dave3600_2_s374923;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SmsManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import java.util.List;

public class BirthdayCheckService extends IntentService {
    public BirthdayCheckService() {
        super("BirthdayCheckService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSmsServiceEnabled = preferences.getBoolean("sms_service", false);
        String defaultSmsMessage = preferences.getString("sms_message", "Happy Birthday!");

        if (!isSmsServiceEnabled) return;

        FriendsDataSource datasource = new FriendsDataSource(this);
        datasource.open();
        List<Friend> friendsWithBirthdayToday = datasource.getFriendsWithBirthdayToday();
        for (Friend friend : friendsWithBirthdayToday) {
            String message = defaultSmsMessage.replace("{name}", friend.getName());
            sendSms(friend.getPhone(), message);
        }
        datasource.close();
    }

    private void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        showNotification("SMS sent to " + phoneNumber);
    }

    private void showNotification(String message) {
        String channelId = "birthday_sms_channel";
        String channelName = "Birthday SMS Notifications";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Birthday SMS")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        // Check and request POST_NOTIFICATIONS permission if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission if it has not been granted
                Intent requestPermissionIntent = new Intent(this, PermissionRequestActivity.class);
                requestPermissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(requestPermissionIntent);
                return;
            }
        }

        notificationManagerCompat.notify(1, builder.build());
    }
}