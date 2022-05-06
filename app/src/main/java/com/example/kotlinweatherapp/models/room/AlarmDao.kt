package com.example.kotlinweatherapp.models.room

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kotlinweatherapp.models.pojos.Alarm
import retrofit2.http.GET
import java.util.*

@Dao
interface AlarmDao {
    @get:Query("SELECT * FROM Alarm")
     val getAllAlarms: LiveData<List<Alarm>>

    @Query("SELECT * FROM Alarm")
    fun getAllAlarms() : LiveData<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarm: Alarm?)

    @Delete
    fun deleteAlarm(alarm: Alarm?)

    @Update
    fun updateAlarm(alarm: Alarm?)

    @Query("SELECT * FROM Alarm WHERE id LIKE :id")
    fun getAlarmObj(id: UUID?): Alarm?
}