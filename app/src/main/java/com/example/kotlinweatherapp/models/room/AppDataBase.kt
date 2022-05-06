package com.example.kotlinweatherapp.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.pojos.WeatherResponse

@TypeConverters(Converters::class)
@Database(entities = [WeatherResponse::class , Alarm::class , FavouriteObject::class], version = 1, exportSchema = false)
 abstract class AppDataBase : RoomDatabase() {

    abstract fun favDao(): FavouritesDao?
    abstract fun alarmDao(): AlarmDao?
    abstract fun mainObjDao(): HomeWeatherDao?

    companion object{
        private var INSTANCE: AppDataBase? = null

        @Synchronized
        fun getDBInstance(context: Context): AppDataBase {
            return INSTANCE ?:Room.databaseBuilder(context.applicationContext, AppDataBase::class.java , "myDBName").allowMainThreadQueries().build()
        }
    }
}