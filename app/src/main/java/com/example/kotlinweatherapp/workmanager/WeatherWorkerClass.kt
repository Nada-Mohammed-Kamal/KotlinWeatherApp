package com.example.kotlinweatherapp.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import android.provider.Settings

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource

import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.system.Os.close

import android.widget.EditText

import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import java.io.FileDescriptor
import android.net.Uri
import android.system.Os.if_nametoindex
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WeatherWorkerClass(var context: Context, var workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    var customNotificationDialogView: View? = null
    var mp : MediaPlayer? = null
    var cnt = 0

    init {
        cnt++
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doWork(): Result {
        val data: Data = inputData
        //m7taga al flag
        val name: String? = data.getString(WorkerUtilsClass.ALARM_NAME)
        val alarmId: String? = data.getString(WorkerUtilsClass.ALARM_ID)
        val doseTime: String? = data.getString(WorkerUtilsClass.ALARM_DOSE_TIME)
        val endDate: String? = data.getString(WorkerUtilsClass.ALARM_END_DATE)

        //feen al start????


        val alarmOrNotification : Boolean? = data.getBoolean((WorkerUtilsClass.ALARM_OR_NOTIFICATION) , true)

        val msg = makeAPICallAndCheckWeatherIsThereAlertsOrNot(alarmId!!)
        if (alarmOrNotification == true){
            if (Settings.canDrawOverlays(context)) {
                GlobalScope.launch(Dispatchers.Main) {
                    displayNotification(cnt.toString(), name!!, msg)
                    setMyWindowManger(name , msg)
                }
            }
        } else {
            displayNotification(cnt.toString(), name!!, msg)
        }

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

        Log.i("TIMEINWORKMANAGER", "doWork: nxtDate: $nxtDate")
        Log.i("TIMEINWORKMANAGER", "doWork: condition " + nxtDate.before(_endDate))


        WorkerUtilsClass.deleteRequestFromWorkManagerByReq(workerParams.id)
        if (nxtDate.before(_endDate)) {
            val request: OneTimeWorkRequest =
                OneTimeWorkRequest.Builder(WeatherWorkerClass::class.java)

                    //////hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee

                    .setInitialDelay(24, TimeUnit.HOURS)
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
            .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.alarmsound))
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)

        val sound: Uri =
            Uri.parse("android.resource://" + context.packageName.toString() + "/" + R.raw.alarmsound)
        builder.setSound(sound)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(Integer.valueOf(id), builder.build())
        Log.e("M3lsh", "displaying notification")
    }

    private suspend fun makeAPICallAndCheckWeatherIsThereAlertsOrNot(alarmId: String): String {
        var stringMsg = ""
        var event = ""
        var alarmObj: Alarm?
        val instance = Repo.getInstance(
            weatherRetrofitClient.getInstance(),
            ConcreateLocalSource(applicationContext),
            applicationContext
        )
        var weatherObjOverNetwork: WeatherResponse? = null
            alarmObj = instance.getAlarmObj(UUID.fromString(alarmId))
            weatherObjOverNetwork =
                instance.getOneWeatherObjOverNetworkWithLatAndLong(
                    applicationContext,
                    alarmObj?.latLng!!
                )
            //TODO: al time byb2a b 0 --> fal 7eta bta3at am w pm fal worker
            val b = weatherObjOverNetwork
            if (b != null) {
                if(!(b.alerts.isNullOrEmpty()))
                         {
                             for (item in weatherObjOverNetwork?.alerts!!) {
                                event = item.tags[0]
                                 stringMsg = if (event == alarmObj?.reasonOfAlarm.toString()) { //the alert matches
                                     "You Need To be Careful there is $event coming"
                                 } else { //the alert doesn't match
                                     "Every thing is fine Today "
                                 }
                            } //there is no alerts
                         }
                //the list is empty
                else {
                    stringMsg = "Every thing is fine Today "
                }
            }
        return stringMsg
    }

    private fun setMyWindowManger(name : String, des : String) {

        val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

         customNotificationDialogView =
            inflater.inflate(R.layout.notification_dialog, null)

        initView(customNotificationDialogView!! ,name ,des )
        if(mp == null){
            mp = MediaPlayer.create(applicationContext , R.raw.alarmsound);

        }
        mp?.start();


        val LAYOUT_FLAG: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            LayoutParams.TYPE_PHONE
        }
        var windowManager=
            applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = (applicationContext.resources.displayMetrics.widthPixels * 0.85).toInt()

        val params = LayoutParams(
            width,
            LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            LayoutParams.FLAG_KEEP_SCREEN_ON or LayoutParams.FLAG_LAYOUT_IN_SCREEN or LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
        CoroutineScope(Dispatchers.Main).launch {
            windowManager.addView(customNotificationDialogView, params)
        }
    }

    private fun initView(customNotificationDialogView: View , name : String , des : String) {

        val alarmName =  customNotificationDialogView.findViewById<TextView>(R.id.alarmDialogNameId)
        val alarmDesc =  customNotificationDialogView.findViewById<TextView>(R.id.AlarmDialogMsgId)
        val okBtn = customNotificationDialogView.findViewById<Button>(R.id.AlarmDialogOkayButtonId)
        alarmName.text = name
        alarmDesc.text = des
        okBtn.setOnClickListener {
            mp?.release()
            close()
        }
    }


    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(
                customNotificationDialogView
            )
            customNotificationDialogView!!.invalidate()
            (customNotificationDialogView!!.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }



}