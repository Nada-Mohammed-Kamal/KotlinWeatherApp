package com.example.kotlinweatherapp.alarmscreen.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.repo.RepoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmFragViewModel(private val repository: RepoInterface, private var context : Context) :
    ViewModel() {


//    var listOfAlarms : LiveData<List< Alarm>>? = _listOfAlarms

    init {
        getAlarms()
    }
    fun getAlarms() : LiveData<List< Alarm>>{
        return repository.getAllAlarms()
        //    return repository.allStoredAlarms
    }

    fun deleteAlarm(alarm : Alarm){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("FromDeleteAlarmVM", "$alarm", )
            repository.deleteAlarm(alarm)
        }
    }

    fun addAlarm(alarm : Alarm){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("FromAddAlarmVM", "$alarm", )
            repository.insertAlarm(alarm)
        }
    }
}