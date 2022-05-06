package com.example.kotlinweatherapp.models.retrofit

import android.content.Context
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import retrofit2.Response

interface RemoteSource {
    suspend fun getWeatherObjOverNetwork(context : Context) : WeatherResponse?
}