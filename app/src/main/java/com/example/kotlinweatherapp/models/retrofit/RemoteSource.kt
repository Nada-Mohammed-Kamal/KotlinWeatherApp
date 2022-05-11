package com.example.kotlinweatherapp.models.retrofit

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response

interface RemoteSource {
    suspend fun getWeatherObjOverNetwork(context : Context) : WeatherResponse?

    suspend fun getWeatherObjOverNetworkWithLatAndLong(context : Context , latLng: LatLng) : WeatherResponse?

    suspend fun getOneWeatherObjOverNetworkWithLatAndLong(context: Context, latLng: LatLng): WeatherResponse?


    }