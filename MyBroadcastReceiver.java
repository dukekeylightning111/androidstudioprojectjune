package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public interface WifiStateListener {
        void onWifiStateChange(boolean isWifiOn);
    }
    private WifiStateListener listener;
    public MyBroadcastReceiver(WifiStateListener listener) {
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifiStateExtra) {
            case WifiManager.WIFI_STATE_ENABLED:
                Toast.makeText(context, "WIFI פועל", Toast.LENGTH_LONG).show();
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                Toast.makeText(context, "WIFI אינו פועל", Toast.LENGTH_LONG).show();
                break;
        }
    }
}