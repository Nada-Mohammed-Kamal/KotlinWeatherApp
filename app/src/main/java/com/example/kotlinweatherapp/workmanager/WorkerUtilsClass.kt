package com.example.kotlinweatherapp.workmanager

import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.CustomTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import androidx.work.Data.Builder

import java.util.concurrent.TimeUnit

class WorkerUtilsClass {

    companion object{
        val TAG = "M3lsh"
        val ALARM_ID = "MED_ID"
        val ALARM_DOSE_TIME = "MED_DOSE_TIME"
        val ALARM_NAME = "MED_NAME"
        val ALARM_END_DATE = "MED_END_DATE"
        val ALARM_OR_NOTIFICATION = "ALARM_OR_NOTIFICATION"

        var wmRequestIds = ArrayList<String>()

        fun findDifference(start_date: String?, end_date: String?): Long {
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            var result = ""
            var difference_In_Seconds: Long = 0
            var difference_In_Minutes: Long = 0
            var difference_In_Hours: Long = 0
            var difference_In_Years: Long = 0
            try {
                val d1 = sdf.parse(start_date)
                val d2 = sdf.parse(end_date)
                val difference_In_Time = d2.time - d1.time
                difference_In_Seconds = difference_In_Time / 1000 % 60
                difference_In_Minutes = difference_In_Time / (1000 * 60) % 60
                difference_In_Hours = difference_In_Time / (1000 * 60 * 60) % 24
                difference_In_Years = difference_In_Time / (1000L * 60 * 60 * 24 * 365)
                print("Difference " + "between two dates is: ")
                val difference_In_Days = difference_In_Time / (1000 * 60 * 60 * 24) % 365
                result = (difference_In_Years
                    .toString() + " years, "
                        + difference_In_Days
                        + " days, "
                        + difference_In_Hours
                        + " hours, "
                        + difference_In_Minutes
                        + " minutes, "
                        + difference_In_Seconds
                        + " seconds")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return difference_In_Hours * 60 * 60 + difference_In_Minutes * 60 + difference_In_Seconds
        }

        fun convertCustomTimeIntoTimeString(ct: CustomTime): String? {
            var hour: String = java.lang.String.valueOf(ct.hour)
            var minute: String = java.lang.String.valueOf(ct.minute)
            if (hour.length == 1) {
                hour = "0$hour"
            }
            if (minute.length == 1) {
                minute = "0$minute"
            }
            return "$hour:$minute:00"
        }

        fun pushNewRequestID(reqID: UUID, alarmID: String, time: String) {
            //at2aked mn al time da
            wmRequestIds.add(reqID.toString() + "_" + alarmID + "_" + time)
        }

        fun addRequestsToWorkManager(alarm : Alarm) {
            var customTime: CustomTime? = null
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val timeNow = sdf.format(Calendar.getInstance().time)
            val endDate = sdf.format(alarm.endDate)
            Log.i(TAG, "Current Date: $timeNow")
            Log.i(TAG, "End Date: $endDate")
//            for (ct in alarm.clcTimes()) {
//                ct?.time =
//                when {
//                    ct!!.hours > 12 -> {
//                        customTime = CustomTime(ct!!.hours, ct.minutes, "pm")
//                    }
//                    ct!!.hours < 12 -> {
//                        customTime = CustomTime(ct!!.hours , ct.minutes , "am" )
//                    }
//                }
                var timeBefore = alarm.alarmTime!!
                if(timeBefore.amOrPm == "am") {
                    if(timeBefore.hour == 12){
                        timeBefore.hour = 0
                    }
                }else{
                    timeBefore.hour = timeBefore.hour + 12
                }
                println("$timeBefore")
                val time = convertCustomTimeIntoTimeString(timeBefore)

                val data: Data = Builder()
                    .putString(ALARM_NAME, alarm.title)
                    .putString(ALARM_ID, alarm.id.toString())
                    .putString(ALARM_DOSE_TIME, time)
                    .putString(ALARM_END_DATE, endDate)
                    .putBoolean(ALARM_OR_NOTIFICATION , alarm.typeBool)
                    .build()
                val startDate = (SimpleDateFormat("dd-MM-yyyy").format(alarm.startDate)
                    .toString() + " "
                        + time)
                val res = findDifference(timeNow, startDate)
                Log.i(TAG, "Start Date: $startDate")
                Log.i(TAG, "StartDate - Current Date = $res")
                val request: OneTimeWorkRequest = OneTimeWorkRequest.Builder(WeatherWorkerClass::class.java)
                    .setInitialDelay(res, TimeUnit.SECONDS)
                    .setInputData(data)
                    .build()
                pushNewRequestID(request.id, alarm.id.toString(), time!!)
                WorkManager.getInstance().enqueue(request)
            }

        fun deleteRequestFromWorkManagerByReq(reqID: UUID) {
            Log.i(TAG, "deleteRequestFromWorkManagerByReq: Size before: " + wmRequestIds.size)
            var toBeDeleted: String? = null

            val it1: Iterator<String> = wmRequestIds.iterator()
            while (it1.hasNext()) {
                val str = it1.next()
                val splitted = str.split("_").toTypedArray()
                if (UUID.fromString(splitted[0]) == reqID) {
                    toBeDeleted = str
                    break
                }
            }

            for (str in wmRequestIds) {
                val splitted = str.split("_").toTypedArray()
                if (UUID.fromString(splitted[0]) == reqID) {
                    toBeDeleted = str
                    break
                }
            }
            if (toBeDeleted != null) {
                Log.i(TAG, "deleteRequestFromWorkManagerByReq: Not Null Condition")
                wmRequestIds.remove(toBeDeleted)
            }
            Log.i(TAG, "deleteRequestFromWorkManagerByReq: Size after: " + wmRequestIds.size)
        }
    }
}


