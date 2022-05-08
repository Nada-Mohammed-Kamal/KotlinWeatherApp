package com.example.kotlinweatherapp.models.room

import android.location.Location
import android.text.format.Time
import androidx.room.TypeConverter
import com.example.kotlinweatherapp.models.pojos.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class Converters {

    //1)
//    @TypeConverter
//    fun convertCurrentWeatherModelFromStringToList(value: String?): List<CurrentWeatherModel> {
//        val listType: Type = object : TypeToken<List<CurrentWeatherModel?>?>() {}.getType()
//        return Gson().fromJson<List<CurrentWeatherModel>>(value, listType)
//    }
//
//    @TypeConverter
//    fun convertCurrentWeatherModelListToString(list: List<CurrentWeatherModel?>?): String {
//        val gson = Gson()
//        return gson.toJson(list)
//    }

    //2)
    //list<Alarms>
    @TypeConverter
    fun convertDailyWeatherModelFromStringToList(value: String?): CurrentWeatherModel {
        val listType: Type = object : TypeToken<CurrentWeatherModel?>() {}.getType()
        return Gson().fromJson<CurrentWeatherModel>(value, listType)
    }

    @TypeConverter
    fun convertCurrentWeatherModelToString(list: CurrentWeatherModel?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun convertLatLongStringToList(value: String?): LatLng {
        val listType: Type = object : TypeToken<LatLng?>() {}.getType()
        return Gson().fromJson<LatLng>(value, listType)
    }

    @TypeConverter
    fun convertLatLongToString(list: LatLng?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun convertCustomTimeModelFromStringToList(value: String?): CustomTime {
        val listType: Type = object : TypeToken<CustomTime?>() {}.getType()
        return Gson().fromJson<CustomTime>(value, listType)
    }

    @TypeConverter
    fun convertCustomTimeModelToString(list: CustomTime?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun convertListOfCurrentWeatherModel(value: String?): List<CurrentWeatherModel> {
        val listType: Type = object : TypeToken<List<CurrentWeatherModel?>?>() {}.getType()
        return Gson().fromJson<List<CurrentWeatherModel>>(value, listType)
    }

    @TypeConverter
    fun convertListOfWeatherModelFromListToString(list: List<CurrentWeatherModel?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun convertListOfDailyWeatherModel(value: String?): List<DailyWeatherModel> {
        val listType: Type = object : TypeToken<List<DailyWeatherModel?>?>() {}.getType()
        return Gson().fromJson<List<DailyWeatherModel>>(value, listType)
    }

    @TypeConverter
    fun convertListOfDailyWeatherModelFromListToString(list: List<DailyWeatherModel?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    //3)
    //List<WeatherStatus>
    @TypeConverter
    fun convertCurrentWeatherModel(value: String?): List<WeatherStatus> {
        val listType: Type = object : TypeToken<List<WeatherStatus?>?>() {}.getType()
        return Gson().fromJson<List<WeatherStatus>>(value, listType)
    }

    //covert from / to alerts
    @TypeConverter
    fun fromAlertToString(alertList: List<Alert>?) : String{
        val gson = Gson()
        return gson.toJson(alertList)
    }

    @TypeConverter
    fun fromStringToAlert(alertListString: String) : List<Alert>?{
        val gson = Gson()
        if(alertListString == null){
            return Collections.emptyList()
        }else{
            var list = object : TypeToken<List<Alert?>?>(){}.type
            return gson.fromJson(alertListString, list)
        }
    }

    @TypeConverter
    fun convertDailyWeatherModelFromListToString(list: List<WeatherStatus?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    //4)location
    @TypeConverter
    fun toLocation(locationString: String?): Location? {
        return try {
            Gson().fromJson(locationString, Location::class.java)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toLocationString(location: Location?): String? {
        return Gson().toJson(location)
    }


    //5)date
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }


    @TypeConverter
    fun fromUUID(uuid: UUID): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String?): UUID? {
        return UUID.fromString(string)
    }

}
