package com.example.dave3600_2_s374923;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.ViewHolder> {

    private List<Friend> friends;

    public BirthdayAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public BirthdayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View friendView = inflater.inflate(R.layout.friend_name_item, parent, false);
        return new ViewHolder(friendView);
    }

    @Override
    public void onBindViewHolder(BirthdayAdapter.ViewHolder viewHolder, int position) {
        Friend friend = friends.get(position);
        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(friend.getName());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}