package com.example.kotlinweatherapp.models.room

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kotlinweatherapp.models.pojos.FavouriteObject

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM FavouriteObject")
    fun getAllFavouriteWeatherObs(): LiveData<List<FavouriteObject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavObj(favObj: FavouriteObject?)

    @Delete
    fun deleteFavWeatherObj(favObj: FavouriteObject?)

    @Update
    fun updateFavWeatherObj(favObj: FavouriteObject?)

    @Query("SELECT * FROM FavouriteObject WHERE locationId LIKE :location")
    fun getFavWeatherObj(location: Location?): FavouriteObject?
}