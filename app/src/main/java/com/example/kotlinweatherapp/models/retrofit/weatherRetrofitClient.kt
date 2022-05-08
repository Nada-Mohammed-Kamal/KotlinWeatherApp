package com.example.kotlinweatherapp.models.retrofit

import android.content.Context
import android.util.Log
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response

class weatherRetrofitClient private  constructor(): RemoteSource {
    override suspend fun getWeatherObjOverNetwork(context : Context): WeatherResponse? {
        var weatherObjResponse : WeatherResponse? = null
        val retrofitService = RetrofitService.getInstance()
        weatherObjResponse = retrofitService.getWeatherObjFromRetrofit(SharedPrefsHelper.getLatitude(context) , SharedPrefsHelper.getLongitude(context) ,"c22e271e9ebc0d0e0e406902c6b750ee", SharedPrefsHelper.getTempUnit(context) , SharedPrefsHelper.getLang(context)).body()
        //weatherObjResponse = response
        Log.i("san", "getWeatherObjOverNetwork: $weatherObjResponse")
        return weatherObjResponse
    }

    override suspend fun getWeatherObjOverNetworkWithLatAndLong(
        context: Context,
        latLng: LatLng
    ): WeatherResponse? {
        var weatherObjResponse : WeatherResponse? = null
        val retrofitService = RetrofitService.getInstance()
        weatherObjResponse = retrofitService.getWeatherObjFromRetrofit(latLng.latitude.toString() ,latLng.longitude.toString() ,"c22e271e9ebc0d0e0e406902c6b750ee", SharedPrefsHelper.getTempUnit(context), SharedPrefsHelper.getLang(context)).body()
        //weatherObjResponse = response
        Log.i("san", "getWeatherObjOverNetwork: $weatherObjResponse")
        return weatherObjResponse
    }

    companion object{
        private var instance : weatherRetrofitClient? = null
        fun getInstance(): weatherRetrofitClient {
            return instance ?: weatherRetrofitClient()
        }
    }
}