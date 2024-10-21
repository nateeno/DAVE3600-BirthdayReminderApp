package com.example.dave3600_2_s374923;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import java.util.List;

public class BirthdayCheckService extends IntentService {
    public BirthdayCheckService() {
        super("BirthdayCheckService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FriendsDataSource datasource = new FriendsDataSource(this);
        datasource.open();
        List<Friend> friendsWithBirthdayToday = datasource.getFriendsWithBirthdayToday();
        for (Friend friend : friendsWithBirthdayToday) {
            String message = "Happy Birthday, " + friend.getName() + "!";
            sendSms(friend.getPhone(), message);
        }
        datasource.close();
    }

    private void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}