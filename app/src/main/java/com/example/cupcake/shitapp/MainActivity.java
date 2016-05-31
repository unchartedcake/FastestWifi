package com.example.cupcake.shitapp;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class MainActivity extends Activity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView wifiNow = (TextView) findViewById(R.id.tvWifi);
        wifiNow.setText("你的網路是 " + getSSID());
        initToolBar();
        getFragmentManager().beginTransaction()
                .replace(R.id.llPF, new PrefsFragment()).commit();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Fastest Wifi");
        toolbar.setTitleTextColor(0xFFFFFFFF);
    }

    public String getSSID() {
        WifiManager wifiManager = (WifiManager) getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        return info.getSSID ();
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
