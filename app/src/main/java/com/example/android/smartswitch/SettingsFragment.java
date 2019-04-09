package com.example.android.smartswitch;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.pref_general,s);

        Preference logoutPref = findPreference(getString(R.string.key_logout));
        logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                logout();
                return false;
            }
        });

        Preference timerOnPref = findPreference(getString(R.string.key_timer_on));
        timerOnPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                pickDate();
                return false;
            }
        });

        Preference timerOffPref = findPreference(getString(R.string.key_timer_off));
        timerOffPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                pickDate();
                return false;
            }
        });


    }

    private void logout(){

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(),LoginActivity.class);
        startActivity(intent);

    }

    private void pickDate(){

        int mHour;
        int mMinute;

        Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String stringMinute = (minute < 10 ? "0" : "") + minute;

                Preference preference = findPreference(getString(R.string.key_timer_on));
                preference.setTitle(hourOfDay + ":" + stringMinute);

            }
        }, mHour, mMinute, false);
        timePicker.show();

    }



}