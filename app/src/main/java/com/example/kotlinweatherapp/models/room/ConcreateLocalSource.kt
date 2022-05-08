package com.example.kotlinweatherapp.models.room

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import java.util.*

class ConcreateLocalSource(context : Context) : LocalSource {

    //vars
    private var favDao : FavouritesDao? = null
    private var homeWeatherObjDao : HomeWeatherDao? = null
    private var alarmDao : AlarmDao?
    override val allStoredAlarms: LiveData<List<Alarm>>


    init {
        val db : AppDataBase = AppDataBase.getDBInstance(context)
        favDao = db.favDao()
        homeWeatherObjDao = db.mainObjDao()
        alarmDao = db.alarmDao()
        allStoredAlarms = alarmDao?.getAllAlarms!!

    }

    override val allStoredWeatherResponses: LiveData<List<WeatherResponse>> = homeWeatherObjDao?.getAllHomeWeatherObs()!!
    override val allStoredFavourites: LiveData<List<FavouriteObject>> = favDao?.getAllFavouriteWeatherObs()!!



    override fun insertHomeObj(weatherObj: WeatherResponse?) {
        homeWeatherObjDao?.insertHomeObj(weatherObj)
    }

    override fun deleteHomeWeatherObj(weatherObj: WeatherResponse?) {
        homeWeatherObjDao?.deleteHomeWeatherObj(weatherObj)
    }

    override fun deleteHomeWeatherObjByLatLongId() {
        homeWeatherObjDao?.deleteHomeWeatherObjByLatLongId()
    }

    override fun deleteFavWeatherObj(favObj: FavouriteObject?) {
        favDao?.deleteFavWeatherObj(favObj)
    }

    override fun updateHomeWeatherObj(weatherObj: WeatherResponse?) {
        homeWeatherObjDao?.updateHomeWeatherObj(weatherObj)
    }

    override fun updateFavWeatherObj(favObj: FavouriteObject?) {
        favDao?.updateFavWeatherObj(favObj)
    }

    override fun getHomeWeatherObj(location: String?): LiveData<WeatherResponse> {
        return homeWeatherObjDao?.getHomeWeatherObj(location)!!
    }

    override fun insertAlarm(alarm: Alarm?) {
        alarmDao?.insertAlarm(alarm)
    }

    override fun deleteAlarm(alarm: Alarm?) {
        alarmDao?.deleteAlarm(alarm)
    }

    override fun updateAlarm(alarm: Alarm?) {
        alarmDao?.updateAlarm(alarm)
    }

    override fun getAlarmObj(id: UUID?): Alarm? {
        return alarmDao?.getAlarmObj(id)
    }

    override fun getAllAlarms(): LiveData<List<Alarm>> {
        return alarmDao?.getAllAlarms()!!
    }

    override fun getFavWeatherObj(location: Location?): FavouriteObject? {
        return favDao?.getFavWeatherObj(location)
    }

    override fun insertFavObj(favObj: FavouriteObject?) {
        favDao?.insertFavObj(favObj)
    }
}