package com.example.cupcake.shitapp;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        SharedPreferences sp = preference.getSharedPreferences();
        boolean ON_OFF = sp.getBoolean("checkbox_preference", false);
        Log.i("lenve", ON_OFF + "");
        String text = sp.getString("edittext_preference", "");
        Log.i("lenve", text + "");
        String listtext = sp.getString("list_preference", "");
        Log.i("lenve", listtext + "");
        boolean next_screen = sp.getBoolean("next_screen_checkbox_preference", false);
        Log.i("lenve", next_screen + "");
        return true;
    }
}
