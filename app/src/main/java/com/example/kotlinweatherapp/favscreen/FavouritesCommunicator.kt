package com.example.kotlinweatherapp.favscreen

import android.content.Context
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject

interface FavouritesCommunicator {
    fun getItemAtIndexToDelete(index : Int) : FavouriteObject
    //fun viewDialog(context : Context , alarmObj : Alarm)
}