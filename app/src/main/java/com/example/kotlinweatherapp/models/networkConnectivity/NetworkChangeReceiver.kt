package com.example.kotlinweatherapp.models.networkConnectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkChangeReceiver : BroadcastReceiver() {

    companion object{
        var isThereInternetConnection = false

    }


    override fun onReceive(context: Context?, intent: Intent) {
        val status = NetworkUtility.getConnectivityStatusString(context!!)
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            isThereInternetConnection = status != NetworkUtility.NETWORK_STATUS_NOT_CONNECTED
        }
    }
}