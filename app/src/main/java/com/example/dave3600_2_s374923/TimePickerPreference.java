package com.example.dave3600_2_s374923;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.preference.DialogPreference;

public class TimePickerPreference extends DialogPreference {
    private int lastHour = 0;
    private int lastMinute = 0;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    public void setTime(int hour, int minute) {
        lastHour = hour;
        lastMinute = minute;
        String time = String.format("%02d:%02d", lastHour, lastMinute);
        persistString(time);
    }

    public int getHour() {
        return lastHour;
    }

    public int getMinute() {
        return lastMinute;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        String time;
        if (restorePersistedValue) {
            time = getPersistedString((String) defaultValue);
        } else {
            time = (String) defaultValue;
        }
        lastHour = getHour(time);
        lastMinute = getMinute(time);
    }

    public static int getHour(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[0]);
    }

    public static int getMinute(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[1]);
    }
}