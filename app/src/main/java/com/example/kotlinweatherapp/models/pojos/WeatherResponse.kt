package com.example.kotlinweatherapp.models.pojos

import android.location.Location
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class WeatherResponse (

    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: CurrentWeatherModel,
    val hourly: List<CurrentWeatherModel>,//48
    val daily: List<DailyWeatherModel>//8
     ):Serializable {
    var alerts : List<Alert>? =  listOf<Alert>()
    var isFav : Boolean = false
    @PrimaryKey
    @NonNull
    var loc : String = ""
    //var isFav : Boolean = false
     }
