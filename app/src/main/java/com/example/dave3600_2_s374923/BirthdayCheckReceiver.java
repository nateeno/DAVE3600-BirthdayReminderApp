package com.example.dave3600_2_s374923;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BirthdayCheckReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, BirthdayCheckService.class);
        context.startService(serviceIntent);
    }
}