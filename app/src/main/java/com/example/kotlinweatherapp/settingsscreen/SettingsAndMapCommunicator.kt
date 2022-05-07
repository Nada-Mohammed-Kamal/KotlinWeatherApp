package com.example.kotlinweatherapp.settingsscreen

import com.google.android.gms.maps.model.LatLng

interface SettingsAndMapCommunicator {
    fun addTOFav(location : LatLng  )
}