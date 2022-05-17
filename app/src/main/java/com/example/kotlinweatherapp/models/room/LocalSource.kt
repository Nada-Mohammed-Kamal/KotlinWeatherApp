package com.example.kotlinweatherapp.models.room

import android.location.Location
import androidx.lifecycle.LiveData
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import java.util.*

interface LocalSource {

    //vars
    val allStoredWeatherResponses: LiveData<List<WeatherResponse>>
    val allStoredAlarms : LiveData<List<Alarm>>
    val allStoredFavourites : LiveData<List<FavouriteObject>>


    //Home Obj
    //fun getAllHomeWeatherObs(): LiveData<List<WeatherResponse>>
    fun insertHomeObj(weatherObj: WeatherResponse?)
    fun deleteHomeWeatherObj(weatherObj: WeatherResponse?)

    fun deleteHomeWeatherObjByLatLongId()

    fun updateHomeWeatherObj(weatherObj: WeatherResponse?)
    fun getHomeWeatherObj(location: String?): LiveData<WeatherResponse>

    //Alarms
    //fun getAllAlarms(): LiveData<List<Alarm>>
    fun insertAlarm(alarm: Alarm?)
    fun deleteAlarm(alarm: Alarm?)
    fun updateAlarm(alarm: Alarm?)
    fun getAlarmObj(id: UUID?): Alarm?

    fun getAllAlarms() : LiveData<List<Alarm>>


    //Favourites
    fun getAllFavouriteWeatherObs(): LiveData<List<FavouriteObject>>
    fun insertFavObj(favObj: FavouriteObject?)
    fun deleteFavWeatherObj(favObj: FavouriteObject?)
    fun updateFavWeatherObj(favObj: FavouriteObject?)
    fun getFavWeatherObj(location: Location?): FavouriteObject?


}
