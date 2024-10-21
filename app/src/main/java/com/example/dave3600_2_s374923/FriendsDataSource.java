package com.example.dave3600_2_s374923;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FriendsDataSource {

    // SQLiteDatabase and DatabaseHelper objects
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    // Database fields
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_PHONE,
            DatabaseHelper.COLUMN_BIRTHDAY };

    public FriendsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Friend createFriend(String name, String phone, String birthday) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_BIRTHDAY, birthday);
        long insertId = database.insert(DatabaseHelper.TABLE_FRIENDS, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_FRIENDS, allColumns,
                DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Friend newFriend = cursorToFriend(cursor);
        cursor.close();
        return newFriend;
    }

    public void deleteFriend(Friend friend) {
        long id = friend.getId();
        System.out.println("Friend deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_FRIENDS, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Friend> getAllFriends() {
        List<Friend> friends = new ArrayList<Friend>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_FRIENDS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Friend friend = cursorToFriend(cursor);
            friends.add(friend);
            cursor.moveToNext();
        }
        // close the cursor
        cursor.close();
        return friends;
    }

    private Friend cursorToFriend(Cursor cursor) {
        Friend friend = new Friend();
        friend.setId(cursor.getLong(0));
        friend.setName(cursor.getString(1));
        friend.setPhone(cursor.getString(2));
        friend.setBirthday(cursor.getString(3));
        return friend;
    }

    public void updateFriend(Friend friend) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, friend.getName());
        values.put(DatabaseHelper.COLUMN_PHONE, friend.getPhone());
        values.put(DatabaseHelper.COLUMN_BIRTHDAY, friend.getBirthday());

        String selection = DatabaseHelper.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(friend.getId()) };

        database.update(DatabaseHelper.TABLE_FRIENDS, values, selection, selectionArgs);
    }

    // Method to get friends with today's birthday
    public List<Friend> getFriendsWithBirthdayToday() {
        List<Friend> friendsWithBirthdayToday = new ArrayList<>();
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        Cursor cursor = database.query(DatabaseHelper.TABLE_FRIENDS,
                allColumns, DatabaseHelper.COLUMN_BIRTHDAY + " = ?", new String[]{today}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Friend friend = cursorToFriend(cursor);
            friendsWithBirthdayToday.add(friend);
            cursor.moveToNext();
        }
        cursor.close();
        return friendsWithBirthdayToday;
    }
}