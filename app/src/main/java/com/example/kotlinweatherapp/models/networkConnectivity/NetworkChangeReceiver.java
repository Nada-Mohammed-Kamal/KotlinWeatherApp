package com.example.kotlinweatherapp.models.networkConnectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static boolean isThereInternetConnection = false;

    public NetworkChangeReceiver(Context context){

    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = NetworkUtility.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtility.NETWORK_STATUS_NOT_CONNECTED) {
                isThereInternetConnection =false;
            } else {
                isThereInternetConnection = true;
            }
        }
    }

}