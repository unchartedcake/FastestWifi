package com.example.cupcake.shitapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.HashMap;
import java.util.jar.Manifest;
import java.lang.Thread;
import java.util.Date;


public class MainActivity extends Activity {
    private Toolbar toolbar;
    private WifiManager wm;
    private WifiInfo wi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView wifiNow = (TextView) findViewById(R.id.tvWifi);
        wifiNow.setText("你的網路是 " + getSSID());
        initToolBar();
        getFragmentManager().beginTransaction()
                .replace(R.id.llPF, new PrefsFragment()).commit();
        wm=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!wm.isWifiEnabled()){
            wm.setWifiEnabled(true);
        }

        String result="";
        HashMap<String,Integer> map=new HashMap<>();
        List<WifiConfiguration> wcList=wm.getConfiguredNetworks();
        for(int i = 0 ; i < wcList.size();i++){
            WifiConfiguration w=wcList.get(i);
            int nid=w.networkId;
            String ssid=w.SSID.substring(1,w.SSID.length()-1);
            map.put(ssid,nid);
        }

        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION},0);
        wm.startScan();
        List<ScanResult> pList=wm.getScanResults();
        int netId=0;
        int speed=-1;
        String wifi="";
        for(int i=0;i<pList.size();i++){
            String currSSID=pList.get(i).SSID;
            if(!map.containsKey(currSSID)){
                continue;
            }
            int currNid=map.get(currSSID);
            boolean suc=wm.enableNetwork(currNid,true);
            if(!suc){
                continue;
            }
            try {
                Thread.sleep(1000);
            }catch (Exception e){

            }
            wi=wm.getConnectionInfo();
            int currSpeed=wi.getLinkSpeed();
            if(currSpeed > speed){
                netId=currNid;
                speed=currSpeed;
                wifi=currSSID;
            }
        }
        wm.enableNetwork(netId,true);
        result=result+"Connect to "+wifi+"\n";
        TextView test=(TextView) findViewById(R.id.debug);
        test.setText(result);
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
