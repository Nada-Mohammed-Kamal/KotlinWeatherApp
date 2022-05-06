package com.example.kotlinweatherapp.models.room

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kotlinweatherapp.models.pojos.WeatherResponse

@Dao
interface HomeWeatherDao {
    @Query("SELECT * FROM WeatherResponse")
    fun getAllHomeWeatherObs(): LiveData<List<WeatherResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHomeObj(weatherObj: WeatherResponse?)

    @Delete
    fun deleteHomeWeatherObj(weatherObj: WeatherResponse?)

    @Update
    fun updateHomeWeatherObj(weatherObj: WeatherResponse?)

    @Query("SELECT * FROM weatherresponse WHERE loc = :location")
    fun getHomeWeatherObj(location: String?): LiveData<WeatherResponse>
}