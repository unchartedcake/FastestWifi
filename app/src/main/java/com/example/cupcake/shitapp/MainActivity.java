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
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.Socket;
import java.util.List;
import java.util.HashMap;
import java.util.jar.Manifest;
import java.lang.Thread;
import java.util.Date;


public class MainActivity extends Activity {
    private Toolbar toolbar;
    private WifiManager wm;
    private WifiInfo wi;
    final String TAG=MainActivity.class.getSimpleName();
    static boolean flag,avalFlag;
    static String result;

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

    @Override
    public void onStart(){
        super.onStart();
        wm=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!wm.isWifiEnabled()){
            wm.setWifiEnabled(true);
        }

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
        long maxTime=10000000;
        String wifi="";
        String bssid="";
        for(int i=0;i<pList.size();i++){
            final String currSSID=pList.get(i).SSID;
            if(!map.containsKey(currSSID)){
                continue;
            }
            int currNid=map.get(currSSID);
            boolean suc=wm.enableNetwork(currNid,true);
            if(!suc){
                continue;
            }

            long elapsTime=10000;
            try {
                flag=true;
                avalFlag=false;
                Date d=new Date();
                long startTime=d.getTime();
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        }catch(Exception e){
                            Log.d(TAG,"Wifi:"+currSSID+";Fail to sleep;error="+e);
                        }
                        wi=wm.getConnectionInfo();
                        int size=20000;
                        try{
                            Socket soc = new Socket("140.112.30.32", 8787);
                            byte[] buf=new byte[2000];
                            while(size > 0) {
                                int byteRead = soc.getInputStream().read(buf, 0, 2000);
                                size = size - byteRead;
                            }
                            flag=false;
                            avalFlag=true;
                        }catch(Exception e){
                            Log.d(TAG,"Wifi:"+currSSID+";"+size+"bytes left"+";error="+e);
                            result=result+currSSID+" failed->";
                            flag=false;
                        }
                    }
                });
                thread.start();
                while(flag){}
                Date d2=new Date();
                long finTime=d2.getTime();
                elapsTime=finTime-startTime;
                result=result+currSSID+":"+elapsTime+";bssid="+pList.get(i).BSSID+"\n";
            }catch (Exception e){
                Log.d(TAG,"error="+e);
            }

            if(avalFlag && elapsTime < maxTime){
                netId=currNid;
                maxTime=elapsTime;
                wifi=currSSID;
                bssid=pList.get(i).BSSID;
            }
            wm.disableNetwork(currNid);
        }
        wm.enableNetwork(netId,true);
        result=result+"Connect to "+bssid+":"+wifi+"\n";
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
