package com.example.kotlinweatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AlarmSevice() : Service() , LifecycleOwner {

    private val mLifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }


    fun start() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun getLifecycle(): Lifecycle = mLifecycleRegistry


    private val CHANNEL_ID = "ForegroundService Kotlin"
    companion object {
        lateinit var myContext : Context
        lateinit var alarmid : String
        fun startService(context: Context, message: String , alarmId: String) {
            myContext = context
            alarmid = alarmId
            val startIntent = Intent(context, AlarmSevice::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, AlarmSevice::class.java)
            context.stopService(stopIntent)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        val makeAPICallAndCheckWeatherIsThereAlertsOrNot = makeAPICallAndCheckWeatherIsThereAlertsOrNot(
            alarmid)
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(makeAPICallAndCheckWeatherIsThereAlertsOrNot)
            .setContentText(input)
            .setSmallIcon(R.drawable.default_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        //stopSelf();
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }


    fun makeAPICallAndCheckWeatherIsThereAlertsOrNot(alarmId: String): String {
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


        var weatherObjOverNetwork: LiveData<List<WeatherResponse>>
        var job = CoroutineScope(Dispatchers.IO).launch {
            alarmObj = instance.getAlarmObj(UUID.fromString(alarmId))
        }
        CoroutineScope(Dispatchers.Main).launch {
            job.join()

            weatherObjOverNetwork =
                instance.getWeatherObjOverNetworkWithLatAndLong(applicationContext, alarmObj?.latLng!!)
            //TODO: al object byrg3 b null  -- wal time byb2a b 0 --> fal 7eta bta3at am w pm fal worker
            val b =
                weatherObjOverNetwork.observe(this@AlarmSevice, androidx.lifecycle.Observer {
                    if (it.isNotEmpty()) {
                        if(!(it[0]?.alerts.isNullOrEmpty())){
                            if (it[0]?.alerts?.isNotEmpty()!!) {
                                for (item in weatherObjOverNetwork.value?.get(0)?.alerts!!) {
                                    event = item.event
                                    if (event == alarmObj?.reasonOfAlarm.toString()) { //the alert matches
                                        stringMsg = "You Need To be Careful there is $event coming"
                                    } else { //the alert doesn't match
                                        stringMsg = "Every thing is fine there is no $event Today "
                                    }
                                } //there is no alerts
                            }
                        }

                    }//the list is empty
                    else {
                        stringMsg = "Every thing is fine there is no $event Today "
                    }
                })
        }
        return stringMsg
    }
}