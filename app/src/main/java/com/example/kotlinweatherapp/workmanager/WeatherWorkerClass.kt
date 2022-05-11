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
import androidx.work.*

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
    CoroutineWorker(context, workerParams) {
    var cnt = 0

    init {
        cnt++
    }

    override suspend fun doWork(): Result {
        val data: Data = inputData
        //m7taga al flag
        val name: String? = data.getString(WorkerUtilsClass.ALARM_NAME)
        val alarmId: String? = data.getString(WorkerUtilsClass.ALARM_ID)
        val doseTime: String? = data.getString(WorkerUtilsClass.ALARM_DOSE_TIME)
        val endDate: String? = data.getString(WorkerUtilsClass.ALARM_END_DATE)


        //TODO: 2a2awam al service
        //AlarmSevice.startService(context, "Foreground Service is running..." , alarmId!!)
        Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot:Before Calling the Function" )
        val msg = makeAPICallAndCheckWeatherIsThereAlertsOrNot(alarmId!!)
        Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot:After Calling the Function" )
        Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot:Before displaying the notification" )
        displayNotification(cnt.toString(), name!!, msg)

        Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot:After Calling the Function" )

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


    suspend fun makeAPICallAndCheckWeatherIsThereAlertsOrNot(alarmId: String): String {
        Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot: inside Methodddddddddd" )
        var stringMsg: String = ""
        var event: String = ""
        var alarmObj: Alarm? = null
        //aroo7 al db a get al alarm da
        //a request beeh 3al api
        //a ret al response wa check fe alerts wala al array fady law fady ha set al notification b every thing is fine
        //law msh fadya hamshy 3al list wa7ed wa7ed wa geeb al event law == al sbb al fal array ha3rdha
        //law la2 hatala3 nothing there bardo
        //mansash a set al isAlarm watala3 al notification 3ala anaha alarm wala notification

        val instance = Repo.getInstance(
            weatherRetrofitClient.getInstance(),
            ConcreateLocalSource(applicationContext),
            applicationContext
        )


        var weatherObjOverNetwork: WeatherResponse? = null
        Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot: weather obj: ${weatherObjOverNetwork}" )

        //var job = CoroutineScope(Dispatchers.IO).launch {
            alarmObj = instance.getAlarmObj(UUID.fromString(alarmId))
            Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot::${alarmObj} " )

        //}
        //CoroutineScope(Dispatchers.IO).launch {
        //    job.join()
            weatherObjOverNetwork =
                instance.getOneWeatherObjOverNetworkWithLatAndLong(
                    applicationContext,
                    alarmObj?.latLng!!
                )
            Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot: ${weatherObjOverNetwork}" )

        //}

        //CoroutineScope(Dispatchers.Main).launch {
        //    job.join()
            //TODO: al object byrg3 b null  -- wal time byb2a b 0 --> fal 7eta bta3at am w pm fal worker
            val b = weatherObjOverNetwork
            Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot: $b" )
            if (!(b == null)) {
                Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot:first Not null or empty" )

                if(!(b.alerts.isNullOrEmpty()))
                         {
                             Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot:first second Not null or empty" )

                             for (item in weatherObjOverNetwork?.alerts!!) {
                                event = item.event
                                if (event == alarmObj?.reasonOfAlarm.toString()) { //the alert matches
                                    Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot:no need to be carefull" )
                                    stringMsg = "You Need To be Careful there is $event coming"
                                } else { //the alert doesn't match
                                    Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot: second fine" )
                                    stringMsg = "Every thing is fine there is no $event Today "
                                }
                            } //there is no alerts
                             Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot: third fine" )

                             Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot:No alerts Found" )


                         }
                //the list is empty
                else {
                    stringMsg = "Every thing is fine there is no $event Today "
                    Log.e("TAG", "makeAPICallAndCheckWeatherIsThereAlertsOrNot : the list is empty" )
                }
            }
        //}
        return stringMsg
    }
    
}