package com.example.dave3600_2_s374923;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BirthdayCheckReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("BirthdayCheckReceiver", "Alarm received, starting BirthdayCheckService.");
        Intent serviceIntent = new Intent(context, BirthdayCheckService.class);
        context.startService(serviceIntent);
    }
}