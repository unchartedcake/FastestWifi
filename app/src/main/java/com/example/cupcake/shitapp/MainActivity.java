package com.example.cupcake.shitapp;

import android.app.Activity;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class MainActivity extends Activity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        getFragmentManager().beginTransaction()
                .replace(R.id.llPF, new PrefsFragment()).commit();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Fastest Wifi");
        toolbar.setTitleTextColor(0xFFFFFFFF);
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
