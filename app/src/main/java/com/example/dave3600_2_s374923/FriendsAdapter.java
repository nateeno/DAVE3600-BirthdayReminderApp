package com.example.dave3600_2_s374923;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private List<Friend> friends;
    private FriendsDataSource datasource;
    private MainActivity mainActivity;

    public FriendsAdapter(List<Friend> friends, FriendsDataSource datasource, MainActivity mainActivity) {
        this.friends = friends != null ? friends : new ArrayList<>();
        this.datasource = datasource;
        this.mainActivity = mainActivity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView phoneTextView;
        public TextView birthdayTextView;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name);
            phoneTextView = itemView.findViewById(R.id.phone);
            birthdayTextView = itemView.findViewById(R.id.birthday);
            editButton = itemView.findViewById(R.id.edit_friend_button);
            deleteButton = itemView.findViewById(R.id.delete_friend_button);
        }
    }

    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View friendView = inflater.inflate(R.layout.friend_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(friendView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        if (friends == null || friends.isEmpty()) {
            return; // Avoid null pointer issues
        }

        Friend friend = friends.get(position);

        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(friend.getName());
        TextView phoneTextView = viewHolder.phoneTextView;
        phoneTextView.setText(friend.getPhone());
        TextView birthdayTextView = viewHolder.birthdayTextView;
        birthdayTextView.setText(friend.getBirthday());

        Button editButton = viewHolder.editButton;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Edit Friend");

                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_edit_friend, null);
                EditText nameInput = dialogView.findViewById(R.id.edit_name);
                EditText phoneInput = dialogView.findViewById(R.id.edit_phone);
                EditText birthdayInput = dialogView.findViewById(R.id.edit_birthday);

                nameInput.setText(friend.getName());
                phoneInput.setText(friend.getPhone());
                birthdayInput.setText(friend.getBirthday());

                builder.setView(dialogView);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = nameInput.getText().toString();
                        String phone = phoneInput.getText().toString();
                        String birthday = birthdayInput.getText().toString();

                        friend.setName(name);
                        friend.setPhone(phone);
                        friend.setBirthday(birthday);

                        datasource.updateFriend(friend);
                        notifyItemChanged(position);
                        mainActivity.refreshBirthdayTodayRecyclerView();
                    }
                });

                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button deleteButton = viewHolder.deleteButton;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datasource.deleteFriend(friend);
                friends.remove(position);
                notifyItemRemoved(position);
                mainActivity.refreshBirthdayTodayRecyclerView();

                notifyItemRangeChanged(position, getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }
}