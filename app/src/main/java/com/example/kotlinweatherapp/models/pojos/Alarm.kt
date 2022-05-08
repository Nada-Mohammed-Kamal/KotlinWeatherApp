package com.example.kotlinweatherapp.models.pojos

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinweatherapp.workmanager.CalcTimes
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

@Entity
class Alarm (

    var title : String  ,
    var startDateText : String ,
    var endDateText : String ,
    var alarmTimeText: String ,
    var typeBool : Boolean ,
    var reasonOfAlarmText : String,
    var latLng: LatLng
    ) : Serializable {

    @PrimaryKey
    @NonNull
    var id : UUID = UUID.randomUUID()


    var startDate : Date? = stringToDate(startDateText , "MM/dd/yy")
    var endDate : Date? = stringToDate(endDateText , "MM/dd/yy")

    var alarmTime: CustomTime? = stringToTime(alarmTimeText)

    var type : AlarmType? = null



    var reasonOfAlarm : ReasonOfTheAlarm = getReasonOfTheAlarm(reasonOfAlarmText)
        init {
            if(typeBool){
                type = AlarmType.Alert
            } else {
                type = AlarmType.Notification
            }
        }


    fun clcTimes() : List<Date?>{
        return CalcTimes.getDaysBetweenDates(startDate, endDate)
    }

    private fun stringToDate(aDate: String?, aFormat: String): Date? {
        if (aDate == null) return null
        val pos = ParsePosition(0)
        val simpleDateFormat = SimpleDateFormat(aFormat)
        return simpleDateFormat.parse(aDate, pos)
    }

    // Wind , Rain , Snow , Cloud , ThunderStorm , MistOrFog
    private fun getReasonOfTheAlarm(reasonOfAlarm : String) : ReasonOfTheAlarm{
        when (reasonOfAlarm) {
            "Wind" -> {
                return ReasonOfTheAlarm.Wind
            }
            "Rain" -> {
                return ReasonOfTheAlarm.Rain
            }
            "Snow" -> {
                return ReasonOfTheAlarm.Snow
            }
            "Cloud" -> {
                return ReasonOfTheAlarm.Cloud
            }
            "Thunder Storm" -> {
                return ReasonOfTheAlarm.ThunderStorm
            }
            "Mist/Fog" -> {
                return ReasonOfTheAlarm.MistOrFog
            }
            else -> {
                return ReasonOfTheAlarm.Wind
            }
        }
    }


    private fun stringToTime(aTime: String?): CustomTime? {
        if (aTime == null) return null
        val separated = aTime.split(":").toTypedArray()
        val hour = separated[0].toInt()
        val secondPart = separated[1]
        val separateTheAmAndPm = secondPart.toString().split(" ").toTypedArray()
        val minutes = separateTheAmAndPm[0].toInt()
        val amOrPm = separateTheAmAndPm[1]
        return CustomTime(hour, minutes, amOrPm)
    }




}