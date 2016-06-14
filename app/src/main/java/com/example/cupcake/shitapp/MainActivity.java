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
import java.util.jar.Manifest;


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
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION},0);
        wm.startScan();
        List<ScanResult> pList=wm.getScanResults();
        //String result="There are "+pList.size()+" wifi\n";
        /*for(int i = 0 ; i < pList.size() ; i++){
            result=result+pList.get(i).SSID+"\n";
        }*/
        //wi=wm.getConnectionInfo();
        //result=result+"Current link speed= "+wi.getLinkSpeed()+"\n";
        String result="";
        //wm.disconnect();
        int netId=0;
        int speed=-1;
        String wifi="";
        List<WifiConfiguration> wcList=wm.getConfiguredNetworks();
        for(int i = 0 ; i < wcList.size();i++){
            WifiConfiguration w=wcList.get(i);
            int nid=w.networkId;
            String ssid=w.SSID;
            wm.enableNetwork(nid,false);
            wi=wm.getConnectionInfo();
            int currSpeed=wi.getLinkSpeed();
            result=result+"Wifi "+i+":"+ssid+"\n"+"netId="+nid+",speed="+currSpeed+"\n";
            if(currSpeed > speed){
                netId=nid;
                speed=currSpeed;
                wifi=ssid;
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
