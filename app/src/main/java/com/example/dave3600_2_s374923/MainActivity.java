package com.example.dave3600_2_s374923;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FriendsDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new FriendsDataSource(this);
        datasource.open();

        Button addFriendButton = findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameInput = findViewById(R.id.name);
                EditText phoneInput = findViewById(R.id.phone);
                EditText birthdayInput = findViewById(R.id.birthday);

                String name = nameInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String birthday = birthdayInput.getText().toString();

                datasource.createFriend(name, phone, birthday);

                nameInput.setText("");
                phoneInput.setText("");
                birthdayInput.setText("");

                refreshRecyclerView();
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