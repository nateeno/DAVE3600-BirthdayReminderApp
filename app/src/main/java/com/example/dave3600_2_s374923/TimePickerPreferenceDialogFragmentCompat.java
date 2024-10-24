package com.example.dave3600_2_s374923;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import androidx.preference.PreferenceDialogFragmentCompat;

public class TimePickerPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private TimePicker timePicker;

    public static TimePickerPreferenceDialogFragmentCompat newInstance(String key) {
        final TimePickerPreferenceDialogFragmentCompat fragment = new TimePickerPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected View onCreateDialogView(Context context) {
        timePicker = new TimePicker(context);
        return timePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        TimePickerPreference preference = (TimePickerPreference) getPreference();
        timePicker.setCurrentHour(preference.getHour());
        timePicker.setCurrentMinute(preference.getMinute());
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();
            TimePickerPreference preference = (TimePickerPreference) getPreference();
            preference.setTime(hour, minute);
        }
    }
}