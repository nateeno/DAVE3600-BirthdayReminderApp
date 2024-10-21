package com.example.dave3600_2_s374923;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FriendsDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new FriendsDataSource(this);
        datasource.open();

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
                if(name.isEmpty() || phone.isEmpty() || birthday.equals("Birthday") || !phone.matches(regex)) {
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