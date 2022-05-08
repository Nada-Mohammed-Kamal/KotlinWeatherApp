package com.example.kotlinweatherapp.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

import androidx.work.Data

import androidx.work.OneTimeWorkRequest

import androidx.work.WorkManager

import androidx.work.Worker

import androidx.work.WorkerParameters
import com.example.kotlinweatherapp.AlarmSevice
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WeatherWorkerClass(var context: Context, var workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    var cnt = 0

    init {
        cnt++
    }


    override fun doWork(): Result {
        val data: Data = inputData
        //m7taga al flag
        val name: String? = data.getString(WorkerUtilsClass.ALARM_NAME)
        val alarmId: String? = data.getString(WorkerUtilsClass.ALARM_ID)
        val doseTime: String? = data.getString(WorkerUtilsClass.ALARM_DOSE_TIME)
        val endDate: String? = data.getString(WorkerUtilsClass.ALARM_END_DATE)


        //TODO: 2a2awam al service
        AlarmSevice.startService(context, "Foreground Service is running..." , alarmId!!)

        //displayNotification(cnt.toString(), name!!, msg)
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.time = Date()
        val nxtDate = cal.time
        var _endDate: Date? = null
        try {
            _endDate = sdf.parse(endDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.i("M3lsh", "doWork: nxtDate: $nxtDate")
        Log.i("M3lsh", "doWork: condition " + nxtDate.before(_endDate))
        WorkerUtilsClass.deleteRequestFromWorkManagerByReq(workerParams.id)
        if (nxtDate.before(_endDate)) {
            Log.i("M3lsh", "doWork: ")
            val request: OneTimeWorkRequest =
                OneTimeWorkRequest.Builder(WeatherWorkerClass::class.java)
                    .setInitialDelay(24, TimeUnit.DAYS)
                    .setInputData(workerParams.inputData)
                    .build()
            WorkerUtilsClass.pushNewRequestID(
                request.getId(),
                //TODO msh mota2akeda mn dose time de
                UUID.fromString(alarmId).toString(), doseTime!!
            )
            WorkManager.getInstance().enqueue(request)
        }
        return Result.success()
    }

    private fun displayNotification(id: String, task: String, desc: String) {
        val channelId = "channel-01"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = channelName
            val channel = NotificationChannel(channelId, name, importance)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.default_icon)
            .setContentTitle(task)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(context)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(Integer.valueOf(id), builder.build())
        Log.e("M3lsh", "displaying notification")
    }





}