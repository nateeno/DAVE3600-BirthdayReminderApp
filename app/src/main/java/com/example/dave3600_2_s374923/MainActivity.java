package com.example.dave3600_2_s374923;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_SEND_SMS = 1;
    private FriendsDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new FriendsDataSource(this);
        datasource.open();

        // Request SMS permission if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_SEND_SMS);
        }

        Button birthdayButton = findViewById(R.id.birthday);
        birthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month + 1; // Month is 0-based
                                String date = day + "-" + month + "-" + year;
                                birthdayButton.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        Button addFriendButton = findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameInput = findViewById(R.id.name);
                EditText phoneInput = findViewById(R.id.phone);
                Button birthdayButton = findViewById(R.id.birthday);

                String name = nameInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String birthday = birthdayButton.getText().toString();

                String regex = "^[0-9]{8}$";
                if (name.isEmpty() || phone.isEmpty() || birthday.equals("Birthday") || !phone.matches(regex)) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show();
                } else {
                    datasource.createFriend(name, phone, birthday);

                    nameInput.setText("");
                    phoneInput.setText("");
                    birthdayButton.setText("Birthday");

                    refreshRecyclerView();
                }
            }
        });

        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        refreshRecyclerView();
        scheduleDailyBirthdayCheck(); // Schedule the daily birthday check
    }

    // Schedule a daily check for birthdays
    private void scheduleDailyBirthdayCheck() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BirthdayCheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the alarm to start at approximately 8:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval constants
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can send SMS now
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to send an SMS using SmsManager
    private void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(this, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
    }

    // Method to check for today's birthdays and send SMS
    private void checkAndSendBirthdayMessages() {
        List<Friend> friendsWithBirthdayToday = datasource.getFriendsWithBirthdayToday();
        for (Friend friend : friendsWithBirthdayToday) {
            String message = "Happy Birthday, " + friend.getName() + "!";
            sendSms(friend.getPhone(), message);
        }
    }

    private void refreshRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.friends_recycler_view);
        List<Friend> friends = datasource.getAllFriends();
        FriendsAdapter adapter = new FriendsAdapter(friends, datasource);  // Pass datasource to the adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}